package uz.pdp.linkedin.Controllers;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import uz.pdp.linkedin.DTO.WorkDto;
import uz.pdp.linkedin.Entity.User;
import uz.pdp.linkedin.Entity.Work;
import uz.pdp.linkedin.Repo.SkillRepository;
import uz.pdp.linkedin.Repo.UserRepository;
import uz.pdp.linkedin.Repo.WorkRepository;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/work")
@RequiredArgsConstructor
public class WorkController {

    private final WorkRepository workRepository;
    private final UserRepository userRepository;
    private final SkillRepository skillRepository;

    @PostMapping
    public ResponseEntity<Work> createWork(@RequestBody WorkDto workDto) {
        if (workDto.getCompanyId() == null) {
            return ResponseEntity.badRequest().build();
        }

        Optional<User> companyOpt = userRepository.findById(workDto.getCompanyId());
        if (companyOpt.isEmpty()) {
            return ResponseEntity.badRequest().build();
        }

        Work work = new Work();
        work.setCompany(companyOpt.get());
        work.setPosition(workDto.getPosition());
        work.setOnline(workDto.getOnline());
        work.setAddress(workDto.getAddress());
        work.setStartDate(workDto.getStartDate());

        if (workDto.getEndDate() != null) {
            work.setEndDate(workDto.getEndDate());
        }

        if (workDto.getSkillsId() != null && !workDto.getSkillsId().isEmpty()) {
            work.setSkills(skillRepository.findAllById(workDto.getSkillsId()));
        }

        Work savedWork = workRepository.save(work);
        return ResponseEntity.ok(savedWork);
    }

    @GetMapping
    public ResponseEntity<List<Work>> getAll() {
        return ResponseEntity.ok(workRepository.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Work> getById(@PathVariable Integer id) {
        return workRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<Work> updateWork(@PathVariable Integer id, @RequestBody Work updatedWork) {
        Optional<Work> workOpt = workRepository.findById(id);
        if (workOpt.isEmpty()) return ResponseEntity.notFound().build();

        Work work = workOpt.get();
        work.setAddress(updatedWork.getAddress());
        work.setPosition(updatedWork.getPosition());
        work.setStartDate(updatedWork.getStartDate());
        work.setEndDate(updatedWork.getEndDate());
        work.setOnline(updatedWork.getOnline());
        work.setSkills(updatedWork.getSkills());
        work.setIsAccepted(updatedWork.getIsAccepted());

        if (updatedWork.getCompany() != null && updatedWork.getCompany().getId() != null) {
            Optional<User> companyOpt = userRepository.findById(updatedWork.getCompany().getId());
            if (companyOpt.isPresent()) {
                work.setCompany(companyOpt.get());
            } else {
                return ResponseEntity.badRequest().body(null);
            }
        }
        return ResponseEntity.ok(workRepository.save(work));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteWork(@PathVariable Integer id) {
        if (!workRepository.existsById(id)) return ResponseEntity.notFound().build();
        workRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }

}
