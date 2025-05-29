package uz.pdp.linkedin.DTO;

import lombok.Value;
import uz.pdp.linkedin.Entity.Enums.Online;

import java.util.Date;
import java.util.List;

@Value
public class WorkDto {
    Integer companyId;
    String address;
    String position;
    Date startDate;
    Date endDate;
    Online online;
    List<Integer> skillsId;
}
