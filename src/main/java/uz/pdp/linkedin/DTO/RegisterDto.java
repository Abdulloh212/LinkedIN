package uz.pdp.linkedin.DTO;

import lombok.Value;

@Value
public class RegisterDto {
    Integer attachmentId;
    String fullname;
    String telephone;
    String email;
    String password;
    String address;
    String Role;
}
