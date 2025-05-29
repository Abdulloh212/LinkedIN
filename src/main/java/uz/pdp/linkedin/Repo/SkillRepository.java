package uz.pdp.linkedin.Repo;

import org.springframework.data.jpa.repository.JpaRepository;
import uz.pdp.linkedin.Entity.Skill;

import java.util.List;

public interface SkillRepository extends JpaRepository<Skill, Integer> {
    List<Skill> findBySkillContainingIgnoreCase(String keyword);
}