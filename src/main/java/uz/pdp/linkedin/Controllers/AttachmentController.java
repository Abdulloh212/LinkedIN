package uz.pdp.linkedin.Controllers;

import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import uz.pdp.linkedin.Entity.Attachment;
import uz.pdp.linkedin.Entity.AttachmentContent;
import uz.pdp.linkedin.Repo.AttachmentContentRepository;
import uz.pdp.linkedin.Repo.AttachmentRepository;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/file")
public class AttachmentController {

    private final AttachmentRepository attachmentRepository;
    private final AttachmentContentRepository attachmentContentRepository;

    public AttachmentController(AttachmentRepository attachmentRepository, AttachmentContentRepository attachmentContentRepository) {
        this.attachmentRepository = attachmentRepository;
        this.attachmentContentRepository = attachmentContentRepository;
    }

    @PostMapping
    public Integer upload(@RequestParam MultipartFile file) throws IOException {
        return saveFile(file);
    }

    @PostMapping("/many")
    public List<Integer> uploadFiles(@RequestParam("files") List<MultipartFile> files) throws IOException {
        List<Integer> attachmentIds = new ArrayList<>();
        for (MultipartFile file : files) {
            if (!file.isEmpty()) {
                attachmentIds.add(saveFile(file));
            }
        }
        return attachmentIds;
    }

    private Integer saveFile(MultipartFile file) throws IOException {
        Attachment attachment = Attachment.builder()
                .fileName(file.getOriginalFilename())
                .build();
        attachmentRepository.save(attachment);

        AttachmentContent attachmentContent = AttachmentContent.builder()
                .attachment(attachment)
                .content(file.getBytes())
                .build();
        attachmentContentRepository.save(attachmentContent);

        return attachment.getId();
    }

    @GetMapping("/{attachmentId}")
    public void getFile(@PathVariable Integer attachmentId, HttpServletResponse response) throws IOException {
        Optional<Attachment> optionalAttachment = attachmentRepository.findById(attachmentId);
        if (optionalAttachment.isEmpty()) {
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
            response.getWriter().write("Fayl topilmadi!");
            return;
        }

        Attachment attachment = optionalAttachment.get();
        AttachmentContent content = attachmentContentRepository.findByAttachmentId(attachmentId);
        if (content == null) {
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
            response.getWriter().write("Fayl kontenti topilmadi!");
            return;
        }

        response.setContentType("application/octet-stream");
        response.setHeader("Content-Disposition", "attachment; filename=\"" + attachment.getFileName() + "\"");
        response.getOutputStream().write(content.getContent());
        response.getOutputStream().flush();
    }
}
