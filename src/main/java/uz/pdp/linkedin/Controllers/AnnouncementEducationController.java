package uz.pdp.linkedin.Controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import uz.pdp.linkedin.DTO.AnnouncementWorkDto;
import uz.pdp.linkedin.Entity.AnnouncementEdu;
import uz.pdp.linkedin.Entity.Education;
import uz.pdp.linkedin.Entity.User;
import uz.pdp.linkedin.Repo.AnnouncementEduRepository;
import uz.pdp.linkedin.Repo.EducationRepository;
import uz.pdp.linkedin.Repo.UserRepository;

@RestController
@RequestMapping("/api/announcement/education")
@RequiredArgsConstructor
public class AnnouncementEducationController {

    private final AnnouncementEduRepository announcementEduRepository;
    private final EducationRepository educationRepository;
    private final UserRepository userRepository;

    @PostMapping
    public ResponseEntity<AnnouncementEdu> create(@RequestBody AnnouncementWorkDto announcementEdu) {
        if (announcementEdu.getCompanyId() == null || announcementEdu.getWorkId() == null ) {
            return ResponseEntity.badRequest().build();
        }
        Education edu = educationRepository.findById(announcementEdu.getCompanyId()).orElse(null);
        User company = userRepository.findById(announcementEdu.getCompanyId()).orElse(null);

        if (edu == null || company == null) return ResponseEntity.badRequest().build();
        AnnouncementEdu announcementEdu1=new AnnouncementEdu();
        announcementEdu1.setEducation(edu);
        announcementEdu1.setEducationCompany(company);

        return ResponseEntity.ok(announcementEduRepository.save(announcementEdu1));
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Integer id) {
        if (!announcementEduRepository.existsById(id)) return ResponseEntity.notFound().build();
        announcementEduRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }
    @GetMapping("/company/{id}")
    public HttpEntity<?> getWork(@PathVariable Integer id) {
        return ResponseEntity.ok(announcementEduRepository.findByEducationCompanyId(id));
    }

}
