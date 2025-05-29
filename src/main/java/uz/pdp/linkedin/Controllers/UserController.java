package uz.pdp.linkedin.Controllers;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import uz.pdp.linkedin.DTO.UserDTO;
import uz.pdp.linkedin.Entity.User;
import uz.pdp.linkedin.Repo.UserRepository;
import uz.pdp.linkedin.Servises.JwtService;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/user")
public class UserController {
    private final JwtService jwtService;
    private final UserRepository userRepository;

    public UserController(JwtService jwtService, UserRepository userRepository) {
        this.jwtService = jwtService;
        this.userRepository = userRepository;
    }
    @PostMapping("/connect")
    public ResponseEntity<?> createConnection(@RequestBody Integer id, HttpServletRequest request) {
        Optional<User> optionalTargetUser = userRepository.findById(id);
        if (optionalTargetUser.isEmpty()) {
            return ResponseEntity.badRequest().body("User not found");
        }

        String authHeader = request.getHeader("Authorization");
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return ResponseEntity.status(401).body("Unauthorized");
        }

        String token = authHeader.replace("Bearer ", "");
        User currentUser = jwtService.getUserFromToken(token);

        if (currentUser.getId().equals(id)) {
            return ResponseEntity.badRequest().body("You cannot connect with yourself.");
        }

        User targetUser = optionalTargetUser.get();

        if (currentUser.getConnections() == null) {
            currentUser.setConnections(new ArrayList<>());
        }

        if (!currentUser.getConnections().contains(targetUser)) {
            currentUser.getConnections().add(targetUser);
        } else {
            return ResponseEntity.badRequest().body("You are already connected with this user.");
        }

        userRepository.save(currentUser);

        return ResponseEntity.ok("Connection added successfully");
    }

    @GetMapping("/withToken")
    public HttpEntity<?> getUser(HttpServletRequest request) {
        String authorizationHeader = request.getHeader("Authorization");
        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            String token = authorizationHeader.replace("Bearer ", "");
            User usernameFromToken = jwtService.getUserFromToken(token);
            UserDTO userDTO = new UserDTO(
                    usernameFromToken.getId(),
                    usernameFromToken.getFullName(),
                    usernameFromToken.getPhone(),
                    usernameFromToken.getEmail(),
                    usernameFromToken.getPhoto().getId(),
                    usernameFromToken.getRoles()
            );
            return ResponseEntity.ok(userDTO);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/id")
    public HttpEntity<?> get(@RequestParam Integer userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("Foydalanuvchi topilmadi"));
        UserDTO userDTO = new UserDTO(
                user.getId(),
                user.getFullName(),
                user.getPhone(),
                user.getEmail(),
                user.getPhoto() != null ? user.getPhoto().getId() : null,user.getRoles()
        );
        return ResponseEntity.ok(userDTO);
    }

    @GetMapping
    public ResponseEntity<List<User>> getAllUsers(@RequestParam(required = false) String search) {
        List<User> users;

        if (search != null && !search.trim().isEmpty()) {
            users = userRepository.findByFullNameContainingIgnoreCase(search.trim());
        } else {
            users = userRepository.findAll();
        }
        return ResponseEntity.ok(users);
    }

}
