package com.challenge.challenge.application.dtos;

import lombok.Builder;

import java.util.List;

@Builder
public record NotificationResponse(List<String> messages, String recipient, String sender) {}
