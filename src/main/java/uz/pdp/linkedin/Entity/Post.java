package uz.pdp.linkedin.Entity;

import jakarta.persistence.Entity;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import uz.pdp.linkedin.Entity.BaseEntity.BaseEntity;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
public class Post extends BaseEntity {
    private String title;
    private String description;
    @ManyToMany
    private List<Attachment> attachments;
    @ManyToOne
    private User creator;
    @CreationTimestamp
    private Date date;
    @ManyToMany
    private List<User> likedUser;
}
