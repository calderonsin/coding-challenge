package com.challenge.challenge.application.dtos;

import lombok.Builder;

@Builder
public record NotificationRequest(String message, String recipient, String sender) {}
