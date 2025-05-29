package uz.pdp.linkedin.Controllers;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import uz.pdp.linkedin.DTO.CheckEmailDto;
import uz.pdp.linkedin.DTO.LoginDTO;
import uz.pdp.linkedin.DTO.RegisterDto;
import uz.pdp.linkedin.DTO.ResetPasswordDto;
import uz.pdp.linkedin.Entity.Enums.RoleName;
import uz.pdp.linkedin.Entity.Role;
import uz.pdp.linkedin.Entity.User;
import uz.pdp.linkedin.Repo.AttachmentRepository;
import uz.pdp.linkedin.Repo.RoleRepository;
import uz.pdp.linkedin.Repo.UserRepository;
import uz.pdp.linkedin.Servises.JwtService;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api")
public class AuthController {

    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final PasswordEncoder passwordEncoder;
    private final RoleRepository roleRepository;
    private final UserRepository userRepository;
    private final AttachmentRepository attachmentRepository;

    public AuthController(JwtService jwtService,
                          AuthenticationManager authenticationManager,
                          PasswordEncoder passwordEncoder,
                          RoleRepository roleRepository,
                          UserRepository userRepository,
                          AttachmentRepository attachmentRepository) {
        this.jwtService = jwtService;
        this.authenticationManager = authenticationManager;
        this.passwordEncoder = passwordEncoder;
        this.roleRepository = roleRepository;
        this.userRepository = userRepository;
        this.attachmentRepository = attachmentRepository;
    }

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody LoginDTO request) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword())
            );
            User user = (User) authentication.getPrincipal();
            String token = jwtService.generateToken(user);
            return ResponseEntity.ok(token);
        } catch (AuthenticationException e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Email yoki parol noto‘g‘ri");
        }
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody RegisterDto request) {
        if (userRepository.findByEmail(request.getEmail()).isPresent()) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Bu email allaqachon ro‘yxatdan o‘tgan");
        }
        User user = new User();
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));

        user.setPhoto(attachmentRepository.findById(request.getAttachmentId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Rasm topilmadi")));

        user.setRoles(List.of(roleRepository.findByRoleName(RoleName.ROLE_USER)));
        user.setPhone(request.getTelephone());
        user.setFullName(request.getFullname());
        user.setAddress(request.getAddress());
        if (request.getRole().equals("USER")){
            List<Role> roles = user.getRoles();
            roles.add(roleRepository.findByRoleName(RoleName.ROLE_USER));
            user.setRoles(roles);
        }
        if (request.getRole().equals("EDUCATION")){
            List<Role> roles = user.getRoles();
            roles.add(roleRepository.findByRoleName(RoleName.ROLE_EDUCATION));
            user.setRoles(roles);
        }
        if (request.getRole().equals("COMPANY")){
            List<Role> roles = user.getRoles();
            roles.add(roleRepository.findByRoleName(RoleName.ROLE_COMPANY));
            user.setRoles(roles);
        }
        userRepository.save(user);
        return ResponseEntity.ok("Ro‘yxatdan o‘tish muvaffaqiyatli");
    }
    @PostMapping("/check-email")
    public ResponseEntity<?> checkEmail(@RequestBody CheckEmailDto request) {
        boolean exists = userRepository.findByEmail(request.getEmail()).isPresent();
        return exists ? ResponseEntity.ok().build() : ResponseEntity.notFound().build();
    }

    @PostMapping("/reset-password")
    public ResponseEntity<?> resetPassword(@RequestBody ResetPasswordDto request) {
        Optional<User> optionalUser = userRepository.findByEmail(request.getEmail());

        if (optionalUser.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Bunday foydalanuvchi topilmadi");
        }

        User user = optionalUser.get();
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        userRepository.save(user);

        return ResponseEntity.ok("Parol muvaffaqiyatli yangilandi");
    }
}
