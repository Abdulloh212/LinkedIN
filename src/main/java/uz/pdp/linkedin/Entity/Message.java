package uz.pdp.linkedin.Entity;

import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import uz.pdp.linkedin.Entity.BaseEntity.BaseEntity;

import java.util.Date;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
public class Message extends BaseEntity {
    private String text;
    @ManyToOne
    private User from;
    @ManyToOne
    private User to;
    @CreationTimestamp
    private Date date;
}
