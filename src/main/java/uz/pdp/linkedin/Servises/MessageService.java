package uz.pdp.linkedin.Servises;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import uz.pdp.linkedin.DTO.MessageDTO;
import uz.pdp.linkedin.DTO.UserDTO;
import uz.pdp.linkedin.Entity.Message;
import uz.pdp.linkedin.Entity.User;
import uz.pdp.linkedin.Repo.MessageRepository;
import uz.pdp.linkedin.Repo.UserRepository;

import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MessageService {

    private final MessageRepository messageRepository;
    private final UserRepository usersRepository;


    public Message createMessage(MessageDTO dto) {
        User fromUser = usersRepository.findById(dto.getFromUserId())
                .orElseThrow(() -> new RuntimeException("Yuboruvchi foydalanuvchi topilmadi"));

        User toUser = usersRepository.findById(dto.getToUserId())
                .orElseThrow(() -> new RuntimeException("Qabul qiluvchi foydalanuvchi topilmadi"));

        Message message = new Message();
        message.setFrom(fromUser);
        message.setTo(toUser);
        message.setText(dto.getText());
        message.setDate(new Date());

        return messageRepository.save(message);
    }

    public Message updateMessage(Integer id, MessageDTO dto) {
        Message message = messageRepository.getById(id);

        if (dto.getText() != null) {
            message.setText(dto.getText());
        }

        if (dto.getFromUserId() != null) {
            User fromUser = usersRepository.findById(dto.getFromUserId())
                    .orElseThrow(() -> new RuntimeException("Yuboruvchi foydalanuvchi topilmadi"));
            message.setFrom(fromUser);
        }

        if (dto.getToUserId() != null) {
            User toUser = usersRepository.findById(dto.getToUserId())
                    .orElseThrow(() -> new RuntimeException("Qabul qiluvchi foydalanuvchi topilmadi"));
            message.setTo(toUser);
        }

        return messageRepository.save(message);
    }

    public void deleteMessage(Integer id) {
        if (!messageRepository.existsById(id)) {
            throw new RuntimeException("O‘chirish uchun xabar topilmadi: ID = " + id);
        }
        messageRepository.deleteById(id);
    }

    public List<Message> getMessagesBetween(Integer fromUserId, Integer toUserId) {
        return messageRepository.findByFromIdAndToIdOrFromIdAndToId(
                fromUserId, toUserId, toUserId, fromUserId
        );
    }

    public List<UserDTO> getUsersWithMessages(Integer currentUserId) {
        List<Message> messages = messageRepository.findByFromIdOrToId(currentUserId, currentUserId);
        Set<Integer> userIds = new HashSet<>();

        for (Message message : messages) {
            if (message.getFrom().getId().equals(currentUserId)) {
                userIds.add(message.getTo().getId());
            } else {
                userIds.add(message.getFrom().getId());
            }
        }

        List<User> users = usersRepository.findAllById(userIds);
        return users.stream()
                .map(user -> new UserDTO(
                        user.getId(),
                        user.getFullName(),
                        user.getPhone(),
                        user.getEmail(),
                        user.getPhoto() != null ? user.getPhoto().getId() : null,
                        user.getRoles()
                ))
                .collect(Collectors.toList());
    }
}
