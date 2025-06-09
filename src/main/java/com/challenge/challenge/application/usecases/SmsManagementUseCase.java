package com.challenge.challenge.application.usecases;

import com.challenge.challenge.domain.Notification;
import com.challenge.challenge.application.exceptions.SmsValidationException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
public class SmsManagementUseCase implements ManageNotification {
    private final int  SMS_MAX_LENGTH = 160;
    private static final String SUFFIX_TEMPLATE = " - Part %d of %d";
    @Override
    public List<String> sendNotification(Notification notification) {
        return sendSmsMessage(notification);
    }


        // You need to fix this method, currently it will crash with > 160 char messages.
    private List<String> sendSmsMessage (Notification notification) {
        String message = notification.getMessage();
        String recipient = notification.getRecipient();
        String sender = notification.getSender();


        //validateInputs(notification);
        if(notification.isShortMessage()){
            deliverMessageViaCarrier(message, recipient, sender);
            return List.of(message);
        }
        ArrayList<String> messageList = splitMessage(message);
        for(String text : messageList) deliverMessageViaCarrier(text, recipient, sender);

        return messageList;
    }

    private ArrayList<String> splitMessage(String message) {
        StringBuilder messageBuilder = new StringBuilder(message);
        int left = 0;
        int countParts = 1;
        int expectedParts = calculateTotalParts(message);
        ArrayList<String> messageList = new  ArrayList<>();

        while(left < messageBuilder.length()){
            String suffix = String.format(SUFFIX_TEMPLATE, countParts, expectedParts);
            int availableSpace = SMS_MAX_LENGTH - suffix.length();
            int right = Math.min( left + availableSpace, message.length());

            if (right < message.length()) right = findOptimalSplitPoint(message, left, right);
            messageList.add( new StringBuilder(message.substring(left, right)).append(suffix).toString());

            left = right;
            countParts += 1;

        }

        return  messageList;
    }
    private int findOptimalSplitPoint(String message, int start, int maxEnd) {
        for (int i = maxEnd - 1; i > start; i--) {
            if (Character.isWhitespace(message.charAt(i))) {
                return i + 1;
            }
        }

        return maxEnd;
    }

    private int calculateTotalParts(String message) {
        int messageLength = message.length();
        int estimatedParts = 1;
        // Iteratively calculate until stable
        while (true) {
            String testSuffix = String.format(SUFFIX_TEMPLATE, estimatedParts, estimatedParts);
            int availableSpacePerPart = SMS_MAX_LENGTH - testSuffix.length();
            int calculatedParts = (int) Math.ceil((double) messageLength / availableSpacePerPart);

            if (calculatedParts <= estimatedParts) {
                return estimatedParts;
            }
            estimatedParts = calculatedParts;

            // Safety check to prevent infinite loop
            if (estimatedParts > 1000) {
                return 1000;
            }
        }
    }

    private Boolean IsShortMessage(String message) {
        return message.length() <= SMS_MAX_LENGTH;
    }


    // This method actually sends the message via an already existing SMS carrier
// You just have to display the message to your console
    private void deliverMessageViaCarrier (String text, String recipient, String sender) {
        System.out.println(text);
    }
    private void validateInputs(Notification notification) {
        String message = notification.getMessage();
        String recipient = notification.getRecipient();
        String sender = notification.getSender();
        if (Objects.isNull(message) || message.trim().isEmpty()) {
            throw new SmsValidationException("Message cannot be null or empty");
        }

        if (Objects.isNull(recipient) || recipient.trim().isEmpty()) {
            throw new SmsValidationException("Recipient cannot be null or empty");
        }

        if (Objects.isNull(sender) || sender.trim().isEmpty()) {
            throw new SmsValidationException("Sender cannot be null or empty");
        }

        // Basic phone number validation
        if (isNotValidPhoneNumber(recipient)) {
            throw new SmsValidationException("Invalid recipient phone number format");
        }

        if (isNotValidPhoneNumber(sender)) {
            throw new SmsValidationException("Invalid sender phone number format");
        }
    }

    private boolean isNotValidPhoneNumber(String phoneNumber) {
        String cleaned = phoneNumber.replaceAll("[\\s\\-\\(\\)\\+]", "");
        return !cleaned.matches("\\d{7,15}");
    }
}
