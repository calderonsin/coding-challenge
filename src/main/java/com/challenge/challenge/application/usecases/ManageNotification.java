package com.challenge.challenge.application.usecases;


import com.challenge.challenge.domain.Notification;

import java.util.List;

public interface ManageNotification {
    List<String> sendNotification(Notification notification);

}
