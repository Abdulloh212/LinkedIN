package uz.pdp.linkedin.Entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import uz.pdp.linkedin.Entity.BaseEntity.BaseEntity;
import uz.pdp.linkedin.Entity.Enums.Online;

import java.util.Date;
import java.util.List;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
public class Education extends BaseEntity {
        @ManyToOne
        private User company;
        private String address;
        private String direction;
        private Date startDate;
        private Date endDate;
        @Enumerated(EnumType.STRING)
        private Online online;
        @ManyToMany
        private List<Skill>skills;
        private Boolean isAccepted;
}
