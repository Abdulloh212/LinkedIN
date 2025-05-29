package uz.pdp.linkedin.Repo;

import org.springframework.data.jpa.repository.JpaRepository;
import uz.pdp.linkedin.Entity.AttachmentContent;

public interface AttachmentContentRepository extends JpaRepository<AttachmentContent, Integer> {
    AttachmentContent findByAttachmentId(Integer attachmentId);
}
