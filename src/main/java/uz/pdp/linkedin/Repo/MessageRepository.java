package uz.pdp.linkedin.Repo;

import org.springframework.data.jpa.repository.JpaRepository;
import uz.pdp.linkedin.Entity.Message;

import java.util.List;

public interface MessageRepository extends JpaRepository<Message, Integer> {
    List<Message> findByFromIdAndToIdOrFromIdAndToId(Integer fromUserId, Integer toUserId, Integer toUserId1, Integer fromUserId1);

    List<Message> findByFromIdOrToId(Integer currentUserId, Integer currentUserId1);
}