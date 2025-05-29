package uz.pdp.linkedin.Controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import uz.pdp.linkedin.DTO.LikeDto;
import uz.pdp.linkedin.Entity.Post;
import uz.pdp.linkedin.Entity.User;
import uz.pdp.linkedin.Repo.PostRepository;
import uz.pdp.linkedin.Repo.UserRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api")
public class LikeController {
    private final PostRepository postRepository;
    private final UserRepository userRepository;

    public LikeController(PostRepository postRepository, UserRepository userRepository) {
        this.postRepository = postRepository;
        this.userRepository = userRepository;
    }
    @GetMapping("/liked-by/{userId}")
    public ResponseEntity<List<Post>> getPostsLikedByUser(@PathVariable Integer userId) {
        List<Post> posts = postRepository.findAllByLikedUserId(userId);
        return ResponseEntity.ok(posts);
    }
    @PostMapping("/like")
    public ResponseEntity<?> postLike(@RequestBody LikeDto likeDto) {
        Optional<User> userOpt = userRepository.findById(likeDto.getUserId());
        Optional<Post> postOpt = postRepository.findById(likeDto.getPostId());

        if (userOpt.isEmpty() || postOpt.isEmpty()) {
            return ResponseEntity.badRequest().body("User or Post not found");
        }

        Post post = postOpt.get();
        User user = userOpt.get();

        if (post.getLikedUser() == null) {
            post.setLikedUser(new ArrayList<>());
        }

        if (!post.getLikedUser().contains(user)) {
            post.getLikedUser().add(user);
        }

        Post saved = postRepository.save(post);
        return ResponseEntity.ok(saved);
    }
    @DeleteMapping("/unlike")
    public ResponseEntity<?> unlikePost(@RequestBody LikeDto dto) {
        Optional<Post> postOpt = postRepository.findById(dto.getPostId());
        Optional<User> userOpt = userRepository.findById(dto.getUserId());

        if (postOpt.isEmpty() || userOpt.isEmpty()) {
            return ResponseEntity.badRequest().body("Post or User not found");
        }

        Post post = postOpt.get();
        User user = userOpt.get();

        if (post.getLikedUser() != null && post.getLikedUser().contains(user)) {
            post.getLikedUser().remove(user);
            Post updated = postRepository.save(post);
            return ResponseEntity.ok(updated);
        }

        return ResponseEntity.ok("User had not liked this post.");
    }

}
