package com.challenge.challenge;

import com.challenge.challenge.application.usecases.SmsManagementUseCase;
import com.challenge.challenge.domain.Notification;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class SmsManagementUseCaseTest {
    private  SmsManagementUseCase smsManagementUseCase;
    private final String LONG_MESSAGE = "This is a message which has more than 160 character for testing the amazing splited function and making a good impresion in Leverest. We should look for another way to create a veryyyyyyyyyyy long string";
    private final String EXACTLY_160_CHARS = "A".repeat(160);
    private final String SHORT_MESSAGE = "Short message";
    private final String CORRECT_PHONE_NUMBER = "1234567";
    private static final String VERY_LONG_MESSAGE = "This is a very long message that will definitely need to be split into multiple parts. ".repeat(100);

    @BeforeEach
    void setUp() {
        smsManagementUseCase = new SmsManagementUseCase();
    }

    @Test
    @DisplayName("Should not split message when less than 160 characters")
    public void shouldNotSplitShortMessage(){
        //Given
    Notification notificationShort = new Notification(SHORT_MESSAGE, CORRECT_PHONE_NUMBER, CORRECT_PHONE_NUMBER);
    //When
    List<String> result = smsManagementUseCase.sendNotification(notificationShort);
    //Then
    assertEquals(1, result.size());
    assertEquals(SHORT_MESSAGE, result.get(0));

    }
    @Test
    @DisplayName("Should not split message when exactly 160 characters")
    public void shouldNotSplitTextWhenMessageIs160Long(){
        //Given
        Notification notificationMid = new Notification(EXACTLY_160_CHARS, CORRECT_PHONE_NUMBER, CORRECT_PHONE_NUMBER);
        //When
        List<String> result = smsManagementUseCase.sendNotification(notificationMid);
        //Then
        assertEquals(1, result.size());
        assertEquals(EXACTLY_160_CHARS, result.get(0));

}
@Test
@DisplayName("Should split message when greater than 160 characters")
public void shouldSplitTextWhenMessageIsLong(){
        //Given
    Notification notificationLong = new Notification(LONG_MESSAGE, CORRECT_PHONE_NUMBER, CORRECT_PHONE_NUMBER);
            //When
            List<String> result = smsManagementUseCase.sendNotification(notificationLong);
            //Then
            assertTrue(result.size() > 1);
            for (int i = 0; i < result.size(); i++) {
                String expectedSuffix = String.format(" - Part %d of %d", i + 1, result.size());
                assertTrue(result.get(i).endsWith(expectedSuffix));
                assertTrue(result.get(i).length() <= 160);
            }
    }
    @Test
    @DisplayName("Should split very long message correctly")
    void shouldSplitVeryLongMessage() {
        // Given
        Notification notification = new Notification(VERY_LONG_MESSAGE, CORRECT_PHONE_NUMBER, CORRECT_PHONE_NUMBER);

        // When
        List<String> result = smsManagementUseCase.sendNotification(notification);

        // Then
        assertTrue(result.size() > 2);
        result.forEach(part -> assertTrue(part.length() <= 160));
    }


}
