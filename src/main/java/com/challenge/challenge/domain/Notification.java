package com.challenge.challenge.domain;


import com.challenge.challenge.application.exceptions.SmsValidationException;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
public class Notification {
    private String message;
    private String recipient;
    private String sender;

    // Constructor with validation (Factory method pattern)
    public Notification(String message, String recipient, String sender) {
        validateAndSet(message, recipient, sender);
    }

    private void validateAndSet(String message, String recipient, String sender) {
        this.message = validateMessage(message);
        this.recipient = validatePhoneNumber(recipient, "Recipient");
        this.sender = validatePhoneNumber(sender, "Sender");
    }

    private String validateMessage(String message) {
        if (message == null || message.trim().isEmpty()) {
            throw new SmsValidationException("Message cannot be null or empty");
        }
        return message;
    }

    private String validatePhoneNumber(String phoneNumber, String fieldName) {
        if (phoneNumber == null || phoneNumber.trim().isEmpty()) {
            throw new SmsValidationException(fieldName + " cannot be null or empty");
        }

        if (isNotValidPhoneNumber(phoneNumber)) {
            throw new SmsValidationException("Invalid " + fieldName.toLowerCase() + " phone number format");
        }
        return phoneNumber;
    }

    private boolean isNotValidPhoneNumber(String phoneNumber) {
        String cleaned = phoneNumber.replaceAll("[\\s\\-\\(\\)\\+]", "");
        return !cleaned.matches("\\d{7,15}");
    }

    // Business logic methods
    public boolean isShortMessage() {
        return message.length() <= 160;
    }

    public int getMessageLength() {
        return message.length();
    }

    // Setters with validation
    public void setMessage(String message) {
        this.message = validateMessage(message);
    }

    public void setRecipient(String recipient) {
        this.recipient = validatePhoneNumber(recipient, "Recipient");
    }

    public void setSender(String sender) {
        this.sender = validatePhoneNumber(sender, "Sender");
    }
}
