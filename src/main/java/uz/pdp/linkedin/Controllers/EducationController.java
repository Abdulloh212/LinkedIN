package uz.pdp.linkedin.Controllers;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import uz.pdp.linkedin.DTO.EducationDto;
import uz.pdp.linkedin.Entity.Education;
import uz.pdp.linkedin.Entity.User;
import uz.pdp.linkedin.Repo.EducationRepository;
import uz.pdp.linkedin.Repo.SkillRepository;
import uz.pdp.linkedin.Repo.UserRepository;
import uz.pdp.linkedin.Servises.JwtService;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/education")
@RequiredArgsConstructor
public class EducationController {

    private final EducationRepository educationRepository;
    private final UserRepository userRepository;
    private final SkillRepository skillRepository;
    private final JwtService jwtService;

    @PostMapping
    public ResponseEntity<Education> createEducation(@RequestBody EducationDto educationDto, HttpServletRequest request) {
        if (educationDto.getCompanyId() == null) {
            return ResponseEntity.badRequest().build();
        }

        Optional<User> companyOpt = userRepository.findById(educationDto.getCompanyId());
        if (companyOpt.isEmpty()) {
            return ResponseEntity.badRequest().build();
        }

        String authHeader = request.getHeader("Authorization");
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return ResponseEntity.status(401).build();
        }

        String token = authHeader.replace("Bearer ", "");
        User userFromToken = jwtService.getUserFromToken(token);

        Optional<User> userOpt = userRepository.findById(userFromToken.getId());
        if (userOpt.isEmpty()) {
            return ResponseEntity.status(404).build();
        }

        User currentUser = userOpt.get();

        Education education = new Education();
        education.setCompany(companyOpt.get());
        education.setAddress(educationDto.getAddress());
        education.setOnline(educationDto.getOnline());
        education.setSkills(skillRepository.findAllById(educationDto.getSkillsId()));
        education.setStartDate(educationDto.getStartDate());
        education.setEndDate(educationDto.getEndDate());
        education.setDirection(educationDto.getDirection());

        Education savedEducation = educationRepository.save(education);

        if (currentUser.getEducations() == null) {
            currentUser.setEducations(new ArrayList<>());
        }
        currentUser.getEducations().add(savedEducation);
        userRepository.save(currentUser);

        return ResponseEntity.ok(savedEducation);
    }

    @GetMapping
    public ResponseEntity<List<Education>> getAllEducation() {
        return ResponseEntity.ok(educationRepository.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Education> getEducationById(@PathVariable Integer id) {
        Optional<Education> educationOpt = educationRepository.findById(id);
        if (educationOpt.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(educationOpt.get());
    }

    @PutMapping("/{id}")
    public ResponseEntity<Education> updateEducation(@PathVariable Integer id, @RequestBody Education updatedEducation, HttpServletRequest request) {
        Optional<Education> educationOpt = educationRepository.findById(id);
        if (educationOpt.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        String authHeader = request.getHeader("Authorization");
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return ResponseEntity.status(401).build();
        }

        String token = authHeader.replace("Bearer ", "");
        User userFromToken = jwtService.getUserFromToken(token);

        boolean isOwner = userFromToken.getEducations().stream()
                .anyMatch(e -> e.getId().equals(id));

        if (!isOwner) {
            return ResponseEntity.status(403).build();
        }

        Education education = educationOpt.get();
        education.setAddress(updatedEducation.getAddress());
        education.setDirection(updatedEducation.getDirection());
        education.setStartDate(updatedEducation.getStartDate());
        education.setEndDate(updatedEducation.getEndDate());
        education.setOnline(updatedEducation.getOnline());
        education.setSkills(updatedEducation.getSkills());
        education.setIsAccepted(updatedEducation.getIsAccepted());

        if (updatedEducation.getCompany() != null && updatedEducation.getCompany().getId() != null) {
            Optional<User> companyOpt = userRepository.findById(updatedEducation.getCompany().getId());
            if (companyOpt.isEmpty()) {
                return ResponseEntity.badRequest().build();
            }
            education.setCompany(companyOpt.get());
        }
        Education saved = educationRepository.save(education);
        List<Education> updatedList = userFromToken.getEducations().stream()
                .map(e -> e.getId().equals(id) ? saved : e)
                .collect(Collectors.toList());

        userFromToken.setEducations(updatedList);
        userRepository.save(userFromToken);
        return ResponseEntity.ok(saved);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteEducation(@PathVariable Integer id) {
        if (!educationRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        educationRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }

}
