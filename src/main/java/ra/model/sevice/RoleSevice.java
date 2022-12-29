package ra.model.sevice;

import ra.model.entity.ERole;
import ra.model.entity.Roles;

import java.util.Optional;

public interface RoleSevice {
    Optional<Roles> findByRoleName(ERole roleName);
}
