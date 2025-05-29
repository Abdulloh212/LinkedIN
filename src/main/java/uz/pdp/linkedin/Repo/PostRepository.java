package uz.pdp.linkedin.Repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import uz.pdp.linkedin.Entity.Post;

import java.util.List;

public interface PostRepository extends JpaRepository<Post, Integer> {
    @Query("SELECT p FROM Post p JOIN p.likedUser u WHERE u.id = :userId")
    List<Post> findAllByLikedUserId(@Param("userId") Integer userId);
}