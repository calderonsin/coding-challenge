package com.challenge.challenge;

import com.challenge.challenge.service.SmsManagement;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class SmsManagementTest {
    private final SmsManagement smsManagement  = new SmsManagement();
    private final String LONG_MESSAGE = "This is a message which has more than 160 character for testing the amazing splited function and making a good impresion in Leverest. We should look for another way to create a veryyyyyyyyyyy long string";


@Test
    public void NoSplitTextWhenLesserThan160(){
        String result = smsManagement.sendNotification("Short message", 1, 1);
        assertEquals("Short message", result);

    }
    @Test
    public void SplitTextWhenGreaterThan160(){
        ArrayList<String> result = smsManagement.splitMessage(LONG_MESSAGE);
        for(String messagepart: result){
            System.out.println(messagepart);
        }

        assertTrue(result.size() > 1);

    }
}