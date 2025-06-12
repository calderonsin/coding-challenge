package com.challenge.challenge.presentation;

import com.challenge.challenge.application.dtos.NotificationRequest;
import com.challenge.challenge.application.dtos.NotificationResponse;
import com.challenge.challenge.application.mappers.NotificationMapper;
import com.challenge.challenge.application.usecases.ManageNotification;
import com.challenge.challenge.domain.Notification;
import com.challenge.challenge.application.exceptions.SmsValidationException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/notification")
public class NotificationController {
    private final ManageNotification manageNotification;
    private final NotificationMapper mapper;

    public NotificationController(ManageNotification manageNotification, NotificationMapper mapper){
        this.manageNotification = manageNotification;
        this.mapper = mapper;
    }

    @PostMapping("/send")
    public ResponseEntity<?> sendSms(@RequestBody NotificationRequest request) {

        try {
            Notification notification = mapper.toEntity(request);
            List<String> result = manageNotification.sendNotification(notification);

            NotificationResponse response = NotificationResponse.builder()
                    .messages(result)
                    .recipient(request.recipient())
                    .sender(request.sender()).build();

            return ResponseEntity.ok(response);

        }
        catch (SmsValidationException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
        catch (Exception e) {
            return ResponseEntity.internalServerError().body(e.getMessage());
        }

}

}
