package com.challenge.challenge;

import com.challenge.challenge.domain.Notification;
import com.challenge.challenge.application.exceptions.SmsValidationException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;


import static org.junit.jupiter.api.Assertions.*;

public class NotificationTest {
    private final String SHORT_MESSAGE = "Short message";
    private final String CORRECT_PHONE_NUMBER = "1234567";

    @Test
    @DisplayName("Should throw validation exception for null message")
    public void shouldThrowSmsValidationExceptionForNullMessage(){
        // Given & When
        Exception exception = assertThrows(SmsValidationException.class, () -> {
            new Notification(null, CORRECT_PHONE_NUMBER, CORRECT_PHONE_NUMBER);
        });
        //Then
        assertEquals("Message cannot be null or empty", exception.getMessage());
    }
    @Test
    @DisplayName("Should throw validation exception for empty message")
    public void shouldThrowSmsValidationExceptionForEmptyMessage(){
        // Given & When
        Exception exception = assertThrows(SmsValidationException.class, () -> {
            new Notification("", CORRECT_PHONE_NUMBER, CORRECT_PHONE_NUMBER);
        });
        //Then
        assertEquals("Message cannot be null or empty", exception.getMessage());
    }
    @Test
    @DisplayName("Should throw SMS validation exception for whitespace-only message")
    public void shouldThrowSmsValidationExceptionForWhitespaceMessage(){
        // Given & When

        Exception exception = assertThrows(SmsValidationException.class, () -> {
            new Notification("        ", CORRECT_PHONE_NUMBER, CORRECT_PHONE_NUMBER);
        });
        //Then
        assertEquals("Message cannot be null or empty", exception.getMessage());
    }
    @ParameterizedTest
    @ValueSource(strings = {
            "1234567",
            "1234567890",
            "123456789012345",
            "+1 (555) 123-4567",
            "555-123-4567",
            "(555) 123-4567"
    })
    @DisplayName("Should accept valid phone numbers")
    void shouldAcceptValidPhoneNumbers(String phoneNumber) {
        // Given & When & Then
        assertDoesNotThrow(() ->
                new Notification(SHORT_MESSAGE, phoneNumber, CORRECT_PHONE_NUMBER)
        );
    }

    @ParameterizedTest
    @ValueSource(strings = {
            "123456",            // Too short (6 digits)
            "1234567890123456",  // Too long (16 digits)
            "123abc7890",        // Contains letters
            "12-34-ab",          // Mixed invalid
            "",                  // Empty
            "   "                // Whitespace only
    })
    @DisplayName("Should reject invalid phone numbers and throws SmsValidationException")
    public void shouldRejectInvalidPhoneNumbers(String phoneNumber){
        // Given & When & Then
        assertThrows(SmsValidationException.class, () -> {
            new Notification(SHORT_MESSAGE, phoneNumber, CORRECT_PHONE_NUMBER);
        });


    }
    @Test
    @DisplayName("Should throw validation exception for null recipient")
    public void shouldThrowExceptionForNullRecipient(){
        // Given & When
        Exception exception = assertThrows(SmsValidationException.class, () -> {
            new Notification(SHORT_MESSAGE, null, CORRECT_PHONE_NUMBER);
        });
        //Then
        assertEquals("Recipient cannot be null or empty", exception.getMessage());


    }
    @Test
    @DisplayName("Should throw validation exception for null sender")
    public void shouldThrowExceptionForNullSender(){
        // Given & When
        Exception exception = assertThrows(SmsValidationException.class, () -> {
            new Notification(SHORT_MESSAGE, CORRECT_PHONE_NUMBER, null);
        });
        //Then
        assertEquals("Sender cannot be null or empty", exception.getMessage());


    }
}
