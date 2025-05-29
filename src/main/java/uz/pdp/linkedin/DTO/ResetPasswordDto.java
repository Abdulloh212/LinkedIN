package uz.pdp.linkedin.DTO;

import lombok.Value;

@Value
public class ResetPasswordDto {
    String email;
    String password;
}
