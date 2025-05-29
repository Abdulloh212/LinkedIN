package uz.pdp.linkedin.Controllers;

import jakarta.mail.MessagingException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import uz.pdp.linkedin.Config.EmailService;

import java.util.HashMap;
import java.util.Map;


@RestController
@RequestMapping("/api")
public class VerificationController {

    private final EmailService emailService;
    private final Map<String, Integer> attemptsMap = new HashMap<>();

    public VerificationController(EmailService emailService) {
        this.emailService = emailService;
    }

    @PostMapping("/send-code")
    public ResponseEntity<?> sendVerificationCode(@RequestParam String email) {
        try {
            emailService.sendVerificationCode(email);
            return ResponseEntity.ok("Verification code sent successfully to " + email);
        } catch (MessagingException e) {
            return ResponseEntity.status(500).body("Failed to send email: " + e.getMessage());
        }
    }

    @PostMapping("/verify-code")
    public ResponseEntity<?> verifyCode(@RequestParam String code, @RequestParam String email) {
        if (code == null || email == null || code.length() != 6) {
            return ResponseEntity.status(400).body(Map.of("valid", false, "message", "Invalid verification code"));
        }

        int attempts = attemptsMap.getOrDefault(email, 0);
        if (attempts >= 3) {
            return ResponseEntity.status(403).body(Map.of("valid", false, "message", "Too many failed attempts. Try again later."));
        }

        String storedCode = emailService.verifycode.get(email);
        if (storedCode == null || !storedCode.equals(code)) {
            attemptsMap.put(email, attempts + 1);
            return ResponseEntity.status(400).body(Map.of("valid", false, "message", "Invalid verification code"));
        }

        attemptsMap.remove(email);
        emailService.verifycode.remove(email);
        return ResponseEntity.ok(Map.of("valid", true, "message", "Verification successful!"));
    }
}

