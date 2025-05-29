package uz.pdp.linkedin.Controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import uz.pdp.linkedin.DTO.MessageDTO;
import uz.pdp.linkedin.DTO.UserDTO;
import uz.pdp.linkedin.Entity.Message;
import uz.pdp.linkedin.Servises.MessageService;

import java.util.List;

@RestController
@RequestMapping("/api/message")
@RequiredArgsConstructor
public class MessageController {

    private final MessageService messageService;

    @PostMapping
    public ResponseEntity<?> create(@RequestBody MessageDTO dto) {
        return ResponseEntity.ok(messageService.createMessage(dto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> update(@PathVariable Integer id, @RequestBody MessageDTO dto) {
        return ResponseEntity.ok(messageService.updateMessage(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Integer id) {
        messageService.deleteMessage(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/between/{from}/{to}")
    public ResponseEntity<?> getFromTo(@PathVariable Integer from, @PathVariable Integer to) {
        List<Message> messages = messageService.getMessagesBetween(from, to);
        return ResponseEntity.ok(messages);
    }

    @GetMapping("/users/{currentUserId}")
    public ResponseEntity<?> getUsersWithMessages(@PathVariable Integer currentUserId) {
        List<UserDTO> users = messageService.getUsersWithMessages(currentUserId);
        return ResponseEntity.ok(users);
    }
}
