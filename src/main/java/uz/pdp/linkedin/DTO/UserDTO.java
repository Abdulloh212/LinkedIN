package uz.pdp.linkedin.DTO;

import lombok.Value;
import uz.pdp.linkedin.Entity.Role;

import java.util.List;

@Value
public class UserDTO {
     Integer id;
     String fullname;
     String telephone;
     String email;
     Integer photo;
     List<Role>roles;
}
