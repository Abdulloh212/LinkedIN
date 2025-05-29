package uz.pdp.linkedin.Entity;

import jakarta.persistence.Entity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import uz.pdp.linkedin.Entity.BaseEntity.BaseEntity;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Skill extends BaseEntity {
    private String skill;
}
