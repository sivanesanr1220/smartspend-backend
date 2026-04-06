package com.smartspend.dto;
import lombok.Data;
import java.time.LocalDateTime;
@Data
public class NotificationDTO {
    private Long id;
    private String message;
    private String type;
    private boolean read;
    private LocalDateTime createdAt;
}
