package ra.controller;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import ra.jwt.JwtTokenProvider;
import ra.model.entity.ERole;
import ra.model.entity.Product;
import ra.model.entity.Roles;
import ra.model.entity.Users;
import ra.model.sevice.ProductService;
import ra.model.sevice.RoleSevice;
import ra.model.sevice.UserService;
import ra.payload.request.ChangePasswordRequest;
import ra.payload.request.LoginRequest;
import ra.payload.request.SignupRequest;
import ra.payload.response.JwtResponse;
import ra.payload.response.MessageResponse;
import ra.security.CustomUserDetails;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/v1/auth")
public class    UserController {
    @Autowired
    private ProductService productSevice;

    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private JwtTokenProvider tokenProvider;
    @Autowired
    private UserService userService;
    @Autowired
    private RoleSevice roleService;
    @Autowired
    private PasswordEncoder encoder;

    @GetMapping("/user/{userId}")
    public Users getProductById(@PathVariable("userId") int userId) {
        return userService.findByUserId(userId);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("lock/{userId}")
    public ResponseEntity<?> lock(@PathVariable ("userId") int userId){
        Users user = userService.findByUserId(userId);
        user.setUserStatus(false);
        userService.saveOrUpdate(user);
        return ResponseEntity.ok(new MessageResponse("Lock User successfully"));

    }
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("unlock/{userId}")
    public ResponseEntity<?> unLock(@PathVariable ("userId") int userId){
        Users user = userService.findByUserId(userId);
        user.setUserStatus(true);
        userService.saveOrUpdate(user);
        return ResponseEntity.ok(new MessageResponse("Lock User successfully"));

    }

    @PostMapping("/signup")
    public ResponseEntity<?> registerUser(@RequestBody SignupRequest signupRequest) {
        if (userService.existsByUserName(signupRequest.getUserName())) {
            return ResponseEntity.badRequest().body(new MessageResponse("Error: Usermame đã có"));
        }
        if (userService.existsByEmail(signupRequest.getEmail())) {
            return ResponseEntity.badRequest().body(new MessageResponse("Error: Email đã có"));
        }
        Users user = new Users();
        user.setUserName(signupRequest.getUserName());
        user.setPassword(encoder.encode(signupRequest.getPassword()));
        user.setEmail(signupRequest.getEmail());
        user.setPhone(signupRequest.getPhone());
        user.setUserStatus(true);
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        Date dateNow = new Date();
        String strNow = sdf.format(dateNow);
        try {
            user.setCreated(sdf.parse(strNow));
        }catch (Exception ex){
            ex.printStackTrace();
        }
        Set<String> strRoles = signupRequest.getListRoles();
        Set<Roles> listRoles = new HashSet<>();
        if (strRoles==null){
            //User quyen mac dinh
            Roles userRole = roleService.findByRoleName(ERole.ROLE_USER).orElseThrow(()->new RuntimeException("Error: Role không tìm thấy"));
            listRoles.add(userRole);
        }else {
            strRoles.forEach(role->{
                switch (role){
                    case "admin":
                        Roles adminRole = roleService.findByRoleName(ERole.ROLE_ADMIN)
                                .orElseThrow(()->new RuntimeException("Error: Role không tìm thấy"));
                        listRoles.add(adminRole);
                    case "moderator":
                        Roles modRole = roleService.findByRoleName(ERole.ROLE_MODERATOR)
                                .orElseThrow(()->new RuntimeException("Error: Role không tìm thấy"));
                        listRoles.add(modRole);
                    case "user":
                        Roles userRole = roleService.findByRoleName(ERole.ROLE_USER)
                                .orElseThrow(()->new RuntimeException("Error: Role không tìm thấy"));
                        listRoles.add(userRole);
                }
            });
        }
        user.setListRoles(listRoles);
        userService.saveOrUpdate(user);
        return ResponseEntity.ok(new MessageResponse(" Người dùng đã đăng ký thành công"));
    }
    @PostMapping("/signin")
    public ResponseEntity<?> loginUser(@RequestBody LoginRequest loginRequest){
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(loginRequest.getUserName(),loginRequest.getPassword())
            );
            SecurityContextHolder.getContext().setAuthentication(authentication);
            CustomUserDetails customUserDetail = (CustomUserDetails) authentication.getPrincipal();
            //Sinh JWT tra ve client
            String jwt = tokenProvider.generateToken(customUserDetail);
            //Lay cac quyen cua userUser_Role
            List<String> listRoles = customUserDetail.getAuthorities().stream()
                    .map(item->item.getAuthority()).collect(Collectors.toList());
            return ResponseEntity.ok(new JwtResponse(jwt,customUserDetail.getUsername(),customUserDetail.getEmail(),
                    customUserDetail.getPhone(),listRoles));
        }catch (Exception e){
            return ResponseEntity.ok("Tên đăng nhâp và mật khẩu sai hoặc chưa đúng ");
        }

    }

    @PutMapping("addWishList/{productId}")
    public ResponseEntity<?> addToWishList(@PathVariable("productId") int productId){
        Product product = productSevice.finById(productId);
        CustomUserDetails customUserDetails = (CustomUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Users user = userService.findByUserId(customUserDetails.getUserId());
        user.getListProduct().add(product);
        try{
            user = userService.saveOrUpdate(user);
            return ResponseEntity.ok("Sản phảm đã được thêm vào danh sách yêu thích ");
        }catch (Exception e){
            e.printStackTrace();
            return ResponseEntity.ok("Chưa thêm được vào danh sách yêu thích");
        }
    }

    @DeleteMapping ("removeWishlist/{productId}")
    public ResponseEntity<?> removeWishlist(@PathVariable("productId")int productId){
        CustomUserDetails customUserDetails = (CustomUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Users user = userService.findByUserId(customUserDetails.getUserId());
        for (Product product:user.getListProduct()){
            if (product.getProductId()==productId){
                user.getListProduct().remove(productSevice.finById(productId));
                break;
            }
        }
        try {
            user = userService.saveOrUpdate(user);
            return  ResponseEntity.ok("Đã loại bỏ khỏi danh sách yêu thích ");
        }catch (Exception e){
            e.printStackTrace();
            return ResponseEntity.ok("Đang bị lỗi trong quá trình xủ lý thử lại sau");
        }
    }

    @PreAuthorize("hasRole('USER')")
    @PostMapping("changePassword")
    public ResponseEntity<?> changePassWord(@RequestBody ChangePasswordRequest changePasswordRequest){
        CustomUserDetails customUserDetails = (CustomUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Users users = userService.findByUserId(customUserDetails.getUserId());
        boolean check = encoder.matches(changePasswordRequest.getOldPassword(),users.getPassword());
        if (check){
            users.setPassword(encoder.encode(changePasswordRequest.getNewPassword()));
            try {
                userService.saveOrUpdate(users);
            }catch (Exception e){
                e.printStackTrace();
                check = false;
            }
        }else {
            return ResponseEntity.ok("Mật khẩu cũ không chính xác!");
        }
        if (check){
            return ResponseEntity.ok("Đổi mật khẩu thành công");
        }else {
            return ResponseEntity.ok("Có lỗi trong quá trình xử lỹ vui lòng thử lại!!");
        }
    }


}