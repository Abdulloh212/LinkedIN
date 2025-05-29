package uz.pdp.linkedin.Repo;

import org.springframework.data.jpa.repository.JpaRepository;
import uz.pdp.linkedin.Entity.Work;

import java.util.List;

public interface WorkRepository extends JpaRepository<Work, Integer> {
    List<Work> findByCompanyId(Integer companyId);
}