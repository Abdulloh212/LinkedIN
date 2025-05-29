package uz.pdp.linkedin.Controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import uz.pdp.linkedin.DTO.PostDto;
import uz.pdp.linkedin.Entity.*;
import uz.pdp.linkedin.Repo.*;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

@RestController
@RequestMapping("/api/post")
@RequiredArgsConstructor
public class PostController {

    private final PostRepository postRepository;
    private final UserRepository userRepository;
    private final EducationRepository educationRepository;
    private final AttachmentRepository attachmentRepository;
    private final AttachmentContentRepository attachmentContentRepository;
    private final WorkRepository workRepository;

    @PostMapping
    public ResponseEntity<Post> createPost(@RequestBody PostDto postDto) {
        if (!postDto.getCreatorId().equals(null)){
            Post post =new Post();
            post.setCreator(userRepository.findById(postDto.getCreatorId()).get());
            post.setTitle(postDto.getTitle());
            post.setDescription(postDto.getDescription());
            List<Attachment> allById = attachmentRepository.findAllById(postDto.getPhotos());
            post.setAttachments(allById);
            Post savedPost = postRepository.save(post);
          return ResponseEntity.ok(savedPost);
        }else {
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping
    public ResponseEntity<List<Post>> getAllPosts() {
        List<Post> posts = postRepository.findAll();
        return ResponseEntity.ok(posts);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Post> getPostById(@PathVariable Integer id) {
        var postOpt = postRepository.findById(id);
        if (postOpt.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(postOpt.get());
    }

    @PutMapping("/{id}")
    public ResponseEntity<Post> updatePost(@PathVariable Integer id, @RequestBody PostDto updatedPost) {
        var postOpt = postRepository.findById(id);
        if (postOpt.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        Post post = postOpt.get();

        post.setTitle(updatedPost.getTitle());
        post.setDescription(updatedPost.getDescription());
        post.setAttachments(attachmentRepository.findAllById(updatedPost.getPhotos()));

        if (updatedPost.getCreatorId() != null) {
            var creatorOpt = userRepository.findById(updatedPost.getCreatorId());
            if (creatorOpt.isEmpty()) {
                return ResponseEntity.badRequest().body(null);
            }
            post.setCreator(creatorOpt.get());
        }

        Post savedPost = postRepository.save(post);
        return ResponseEntity.ok(savedPost);
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<?> deletePost(@PathVariable Integer id) {
        if (!postRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        postRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/from-education/{educationId}")
    public ResponseEntity<?> createPostFromEducation(@PathVariable Integer educationId) throws IOException {
        Education education = educationRepository.findById(educationId)
                .orElseThrow(() -> new RuntimeException("Education not found"));

        if (!Boolean.TRUE.equals(education.getIsAccepted())) {
            return ResponseEntity.badRequest().body("User is not accepted to this education");
        }

        Attachment photo = new Attachment();
        photo.setFileName("img.png");
        Attachment save = attachmentRepository.save(photo);
        AttachmentContent attachmentContent=new AttachmentContent();
        attachmentContent.setAttachment(save);
        attachmentContent.setContent(readFileFromResources("img.png") );
        attachmentContentRepository.save(attachmentContent);

        Post post = new Post();
        post.setTitle("🎓 I just got into " + education.getCompany().getFullName() + "!");
        post.setDescription("Never stop learning! I'm excited to begin a new journey in " +
                education.getDirection() + ". Wish me luck!");
        post.setCreator(education.getCompany());
        post.setAttachments(List.of(save));

        return ResponseEntity.ok(postRepository.save(post));
    }

    @PostMapping("/from-work/{workId}")
    public ResponseEntity<?> createPostFromWork(@PathVariable Integer workId) throws IOException {
        Work work = workRepository.findById(workId)
                .orElseThrow(() -> new RuntimeException("Work not found"));

        if (!Boolean.TRUE.equals(work.getIsAccepted())) {
            return ResponseEntity.badRequest().body("User is not accepted to this work");
        }

        Attachment photo = new Attachment();
        photo.setFileName("img.png");
        Attachment savedAttachment = attachmentRepository.save(photo);

        AttachmentContent attachmentContent = new AttachmentContent();
        attachmentContent.setAttachment(savedAttachment);
        attachmentContent.setContent(readFileFromResources("img.png"));
        attachmentContentRepository.save(attachmentContent);

        Post post = new Post();
        post.setTitle("💼 I just started working at " + work.getCompany().getFullName() + "!");
        post.setDescription("Excited to begin my new role as " + work.getPosition() +
                " at " + work.getCompany().getFullName() + ". Looking forward to new challenges!");
        post.setCreator(work.getCompany());
        post.setAttachments(List.of(savedAttachment));

        return ResponseEntity.ok(postRepository.save(post));
    }

    public byte[] readFileFromResources(String filename) throws IOException {
        try (InputStream is = getClass().getResourceAsStream("/files/" + filename)) {
            if (is == null) throw new IOException("File not found");
            return is.readAllBytes();
        }
    }


}
