package uz.pdp.linkedin.DTO;

import jakarta.persistence.ManyToOne;
import lombok.Value;
import uz.pdp.linkedin.Entity.Post;
import uz.pdp.linkedin.Entity.User;

import java.util.Date;

@Value
public class CommentDto {
     String text;
     Integer authorId;
     Integer postId;

}
