package uz.pdp.linkedin.Entity;

import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import uz.pdp.linkedin.Entity.BaseEntity.BaseEntity;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Builder
public class AttachmentContent extends BaseEntity {
    private byte[] content;

    @ManyToOne
    private Attachment attachment;
}
