package uz.pdp.linkedin.Repo;

import org.springframework.data.jpa.repository.JpaRepository;
import uz.pdp.linkedin.Entity.Attachment;

public interface AttachmentRepository extends JpaRepository<Attachment, Integer> {
}