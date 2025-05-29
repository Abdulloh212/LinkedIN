package uz.pdp.linkedin.DTO;

import lombok.Value;

import java.util.List;

@Value
public class PostDto {
    String title;
    String description;
    List<Integer> photos;
    Integer creatorId;
}
