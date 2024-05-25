package hello.hello.yju.service;

import hello.hello.yju.dto.ChatMessageDto;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AlarmService {

    private final SimpMessageSendingOperations messagingTemplate;

    public void alarmByMessage(String userId, ChatMessageDto messageDto) {
        System.out.println("Sending notification to user: " + userId); // Log user ID
        System.out.println("Message DTO: " + messageDto); // Log message DTO

        try {
            messagingTemplate.convertAndSendToUser(userId, "/sub/notifications", messageDto);
            System.out.println("Notification sent successfully to user: " + userId);
        } catch (Exception e) {
            System.err.println("Error sending notification to user: " + userId);
            e.printStackTrace();
        }
    }
}
