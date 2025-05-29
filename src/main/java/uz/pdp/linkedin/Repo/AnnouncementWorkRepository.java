package uz.pdp.linkedin.Repo;

import org.springframework.data.jpa.repository.JpaRepository;
import uz.pdp.linkedin.Entity.AnnouncementWork;

import java.util.List;

public interface AnnouncementWorkRepository extends JpaRepository<AnnouncementWork, Integer> {
    List<AnnouncementWork> findByWorkCompanyId(Integer id);
}