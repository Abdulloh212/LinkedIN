package uz.pdp.linkedin.Repo;

import org.springframework.data.jpa.repository.JpaRepository;
import uz.pdp.linkedin.Entity.Education;

import java.util.List;

public interface EducationRepository extends JpaRepository<Education, Integer> {
    List<Education> findByCompanyId(Integer companyId);
}