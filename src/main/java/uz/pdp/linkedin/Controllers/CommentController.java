package uz.pdp.linkedin.Controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import uz.pdp.linkedin.DTO.CommentDto;
import uz.pdp.linkedin.Entity.Comment;
import uz.pdp.linkedin.Entity.Post;
import uz.pdp.linkedin.Entity.User;
import uz.pdp.linkedin.Repo.CommentRepository;
import uz.pdp.linkedin.Repo.PostRepository;
import uz.pdp.linkedin.Repo.UserRepository;

import java.util.List;

@RestController
@RequestMapping("/api/comment")
@RequiredArgsConstructor
public class CommentController {

    private final CommentRepository commentRepository;
    private final UserRepository userRepository;
    private final PostRepository postRepository;

    @PostMapping
    public ResponseEntity<Comment> createComment(@RequestBody CommentDto comment1) {
        if (comment1.getAuthorId() == null ) {
            return ResponseEntity.badRequest().body(null);
        }
        User author = userRepository.findById(comment1.getAuthorId())
                .orElseThrow(() -> new RuntimeException("Author topilmadi"));

        if (comment1.getPostId() == null) {
            return ResponseEntity.badRequest().body(null);
        }
        Post post = postRepository.findById(comment1.getPostId())
                .orElseThrow(() -> new RuntimeException("Post topilmadi"));
        Comment comment = new Comment();
        comment.setAuthor(author);
        comment.setPost(post);
        comment.setText(comment1.getText());
        Comment savedComment = commentRepository.save(comment);
        return ResponseEntity.ok(savedComment);
    }

    @GetMapping()
    public ResponseEntity<List<Comment>> getAllComments(@PathVariable Integer id) {
        return ResponseEntity.ok(commentRepository.getAllByPostId(id));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Comment> getCommentById(@PathVariable Integer id) {
        return commentRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<Comment> updateComment(@PathVariable Integer id, @RequestBody CommentDto updatedComment) {
        return commentRepository.findById(id).map(comment -> {
            comment.setText(updatedComment.getText());

            if (updatedComment.getAuthorId() != null) {
                User author = userRepository.findById(updatedComment.getAuthorId())
                        .orElseThrow(() -> new RuntimeException("Author topilmadi"));
                comment.setAuthor(author);
            }

            if (updatedComment.getPostId() != null) {
                Post post = postRepository.findById(updatedComment.getAuthorId())
                        .orElseThrow(() -> new RuntimeException("Post topilmadi"));
                comment.setPost(post);
            }

            Comment saved = commentRepository.save(comment);
            return ResponseEntity.ok(saved);
        }).orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteComment(@PathVariable Integer id) {
        if (!commentRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        commentRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}

