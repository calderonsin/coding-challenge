package com.challenge.challenge.application.dtos;

import java.util.List;

public record NotificationResponse(List<String> messages, String recipient, String sender) {}
