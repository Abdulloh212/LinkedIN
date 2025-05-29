package uz.pdp.linkedin.DTO;

import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.ManyToMany;
import lombok.Value;
import uz.pdp.linkedin.Entity.Enums.Online;
import uz.pdp.linkedin.Entity.Skill;
import uz.pdp.linkedin.Entity.User;

import java.util.Date;
import java.util.List;
@Value
public class EducationDto {
     Integer companyId;
     String address;
     String direction;
     Date startDate;
     Date endDate;
     Online online;
     List<Integer> skillsId;
}
