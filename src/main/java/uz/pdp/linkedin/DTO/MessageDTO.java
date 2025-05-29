package uz.pdp.linkedin.DTO;

import lombok.Value;

@Value
public class MessageDTO {
     Integer fromUserId;
     Integer toUserId;
     String text;
}
