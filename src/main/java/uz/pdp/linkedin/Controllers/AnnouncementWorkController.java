package uz.pdp.linkedin.Controllers;


import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import uz.pdp.linkedin.DTO.AnnouncementWorkDto;
import uz.pdp.linkedin.Entity.*;
import uz.pdp.linkedin.Repo.AnnouncementWorkRepository;
import uz.pdp.linkedin.Repo.UserRepository;
import uz.pdp.linkedin.Repo.WorkRepository;

import java.util.Optional;

@RestController
@RequestMapping("/api/announcement/work")
@RequiredArgsConstructor
public class AnnouncementWorkController {

    private final AnnouncementWorkRepository announcementWorkRepository;
    private final WorkRepository workRepository;
    private final UserRepository userRepository;

    @PostMapping
    public ResponseEntity<AnnouncementWork> create(@RequestBody AnnouncementWorkDto announcementWork) {
        if (announcementWork.getCompanyId() == null || announcementWork.getWorkId() == null) {
            return ResponseEntity.badRequest().build();
        }

        Optional<User> companyOpt = userRepository.findById(announcementWork.getCompanyId());
        Optional<Work> workOpt = workRepository.findById(announcementWork.getWorkId());

        if (companyOpt.isEmpty() || workOpt.isEmpty()) {
            return ResponseEntity.badRequest().build();
        }

        AnnouncementWork announcementWork1 = new AnnouncementWork();
        announcementWork1.setWork(workOpt.get());
        announcementWork1.setWorkCompany(companyOpt.get());

        AnnouncementWork saved = announcementWorkRepository.save(announcementWork1);
        return ResponseEntity.ok(saved);
    }
    @DeleteMapping("/{id}")
    public HttpEntity<?> delete(@PathVariable Integer id) {
        if (!announcementWorkRepository.existsById(id)) return ResponseEntity.notFound().build();
        announcementWorkRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }
    @GetMapping("/company/{id}")
    public HttpEntity<?> getWork(@PathVariable Integer id) {
        return ResponseEntity.ok(announcementWorkRepository.findByWorkCompanyId(id));
    }
    @PostMapping("/accept")
    public HttpEntity<?>verify(@RequestBody Integer id){
        Optional<AnnouncementWork> byId = announcementWorkRepository.findById(id);
        if (byId.isPresent()) {
            AnnouncementWork announcementWork = byId.get();
            Work work = workRepository.findById(byId.get().getWork().getId()).orElse(null);
            if (work == null) return ResponseEntity.badRequest().build();
            work.setIsAccepted(true);
            announcementWorkRepository.delete(announcementWork);
            return ResponseEntity.ok(workRepository.save(work));
        }else {
            return ResponseEntity.badRequest().build();
        }
    }
}