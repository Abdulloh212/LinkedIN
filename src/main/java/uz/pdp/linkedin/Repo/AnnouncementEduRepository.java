package uz.pdp.linkedin.Repo;

import org.springframework.data.jpa.repository.JpaRepository;
import uz.pdp.linkedin.Entity.AnnouncementEdu;

import java.util.List;

public interface AnnouncementEduRepository extends JpaRepository<AnnouncementEdu, Integer> {
    List<AnnouncementEdu> findByEducationCompanyId(Integer id);
}