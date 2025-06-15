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

    private List<String> sendSmsMessage (Notification notification) {
        String message = notification.getMessage();
        String recipient = notification.getRecipient();
        String sender = notification.getSender();

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
        StringBuilder sb = new StringBuilder();
        ArrayList<String> messageList = new  ArrayList<>();
        while(left < messageBuilder.length()){
            String suffix = String.format(SUFFIX_TEMPLATE, countParts, expectedParts);
            int availableSpace = SMS_MAX_LENGTH - suffix.length();
            int right = Math.min( left + availableSpace, message.length());

            if (right < message.length()) right = findOptimalSplitPoint(message, left, right);
            sb.append(message, left, right);
            sb.append(suffix);
            messageList.add(message.substring(left, right) + suffix);
            sb.setLength(0);
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

    private  int calculateTotalParts(String message) {
        int maxSuffix = " - Part 99 of 99".length();
        int minAvailableSpace = SMS_MAX_LENGTH - maxSuffix;
        int messageLength = message.length();
        return (int) Math.ceil((double) messageLength / minAvailableSpace);
    }
    private void deliverMessageViaCarrier (String text, String recipient, String sender) {
        System.out.println(text);
    }

}
