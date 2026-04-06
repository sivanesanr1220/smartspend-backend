package com.smartspend.service;
import com.smartspend.dto.NotificationDTO;
import com.smartspend.entity.Notification;
import com.smartspend.entity.User;
import com.smartspend.repository.NotificationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.stream.Collectors;

@Service @RequiredArgsConstructor
public class NotificationService {
    private final NotificationRepository notifRepo;
    private final SimpMessagingTemplate messaging;

    public void create(User user, String message, String type) {
        Notification n = Notification.builder().user(user).message(message).type(type).build();
        notifRepo.save(n);
        try { messaging.convertAndSendToUser(user.getEmail(), "/queue/notifications", toDTO(n)); }
        catch (Exception ignored) {}
    }

    public List<NotificationDTO> getAll(User user) {
        return notifRepo.findByUserOrderByCreatedAtDesc(user).stream().map(this::toDTO).collect(Collectors.toList());
    }

    public long countUnread(User user) { return notifRepo.countByUserAndReadFalse(user); }

    public void markAllRead(User user) {
        List<Notification> unread = notifRepo.findByUserAndReadFalse(user);
        unread.forEach(n -> n.setRead(true));
        notifRepo.saveAll(unread);
    }

    private NotificationDTO toDTO(Notification n) {
        NotificationDTO dto = new NotificationDTO();
        dto.setId(n.getId()); dto.setMessage(n.getMessage()); dto.setType(n.getType());
        dto.setRead(n.isRead()); dto.setCreatedAt(n.getCreatedAt()); return dto;
    }
}
