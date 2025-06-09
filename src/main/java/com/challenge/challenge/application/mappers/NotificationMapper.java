package com.challenge.challenge.application.mappers;

import com.challenge.challenge.application.dtos.NotificationRequest;
import com.challenge.challenge.domain.Notification;
import org.springframework.stereotype.Component;

@Component
public class NotificationMapper {
    public Notification toEntity(NotificationRequest request) {
        return new Notification(
                request.message(),
                request.recipient(),
                request.sender()
        );
    }

    public NotificationRequest toDto(Notification notification) {
        return NotificationRequest.builder()
                .message(notification.getMessage())
                .recipient(notification.getRecipient())
                .sender(notification.getSender())
                .build();
    }
}
