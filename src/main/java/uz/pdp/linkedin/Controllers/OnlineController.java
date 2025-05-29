package uz.pdp.linkedin.Controllers;

import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import uz.pdp.linkedin.Entity.Enums.Online;

import java.util.List;

@RestController
@RequestMapping("/api/online")
public class OnlineController {
    @GetMapping
    public HttpEntity<?> online() {
        return ResponseEntity.ok(List.of(Online.ONLINE, Online.OFFLINE));
    }
}
