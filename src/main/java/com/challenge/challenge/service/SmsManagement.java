package com.challenge.challenge.service;

import java.util.ArrayList;

public class SmsManagement implements ManageNotification{
    private final int  SMS_MAX_LENGTH = 160;
    private static final String SUFFIX_TEMPLATE = " - Part %d of %d";
    @Override
    public String sendNotification(String message, Integer recipient, Integer sender) {
        return sendSmsMessage(message, recipient, sender);
    }


    private Boolean IsShorMessage(String message) {
        return message.length() <= SMS_MAX_LENGTH;
    }





    // You need to fix this method, currently it will crash with > 160 char messages.
    private String sendSmsMessage (String message, Integer recipient, Integer sender) {
        if(IsShorMessage(message)){
            deliverMessageViaCarrier(message, recipient, sender);
            return message;
        }
        ArrayList<String> messageList = splitMessage(message);
        for(String text : messageList) deliverMessageViaCarrier(text, recipient, sender);

        return  IsShorMessage(message) ? message: messageList.toString();
    }

    public ArrayList<String> splitMessage(String message) {
        StringBuilder messageBuilder = new StringBuilder(message);
        int left = 0;
        int countParts = 1;
        int expectedParts = calculateTotalParts(message);
        ArrayList<String> messageList = new  ArrayList<>();

        while(left < messageBuilder.length()){
            String sufix = String.format(SUFFIX_TEMPLATE, countParts, expectedParts);
            int availableSpace = SMS_MAX_LENGTH - sufix.length();
            int right = Math.min( left + availableSpace, message.length());

            if (right < message.length()) right = findOptimalSplitPoint(message, left, right);
            messageList.add( new StringBuilder(message.substring(left, right)).append(sufix).toString());

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


    // This method actually sends the message via an already existing SMS carrier
// You just have to display the message to your console
    private void deliverMessageViaCarrier (String text, Integer recipient, Integer sender) {
        System.out.println(text);
    }
}
