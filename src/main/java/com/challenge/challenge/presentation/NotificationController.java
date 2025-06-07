package com.challenge.challenge.presentation;

import com.challenge.challenge.application.dtos.NotificationRequest;
import com.challenge.challenge.application.dtos.NotificationResponse;
import com.challenge.challenge.application.usecases.ManageNotification;
import com.challenge.challenge.domain.Notification;
import com.challenge.challenge.exceptions.SmsValidationException;
import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api_notification/v1")
public class NotificationController {
    private final ManageNotification manageNotification;
    private final ModelMapper modelMapper;

    public NotificationController(ManageNotification manageNotification, ModelMapper modelMapper){
        this.manageNotification = manageNotification;
        this.modelMapper = modelMapper;
    }

    @PostMapping("/send")
    public ResponseEntity<?> sendSms(@RequestBody NotificationRequest request) {
        Notification notification = modelMapper.map(request, Notification.class);
        try {
            List<String> result = manageNotification.sendNotification(notification);
            NotificationResponse response = new NotificationResponse(result, request.recipient(), request.sender());

            return ResponseEntity.ok(response);

        } catch (SmsValidationException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
    }
}
}
