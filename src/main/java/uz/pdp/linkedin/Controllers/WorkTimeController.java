package uz.pdp.linkedin.Controllers;

import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import uz.pdp.linkedin.Entity.Enums.WorkTime;

import java.util.List;

@RestController
@RequestMapping("/api/workTime")
public class WorkTimeController {
    @GetMapping
    public HttpEntity<?> online() {
        return ResponseEntity.ok(List.of(WorkTime.FULLTIME,WorkTime.PARTTIME));
    }
}
