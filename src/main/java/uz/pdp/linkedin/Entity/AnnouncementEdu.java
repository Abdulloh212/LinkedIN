package uz.pdp.linkedin.Entity;

import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import uz.pdp.linkedin.Entity.BaseEntity.BaseEntity;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
public class AnnouncementEdu extends BaseEntity {
    @ManyToOne
    private Education education;
    @ManyToOne
    private User educationCompany;

}
