package hello.hello.yju.controller.chat;

import hello.hello.yju.dto.chat.ChatMessageDto;
import hello.hello.yju.dto.chat.ChatMessageRequest;
import hello.hello.yju.dto.chat.ChatRoomDto;
import hello.hello.yju.dto.chat.ChatRoomRequest;
import hello.hello.yju.dto.user.CustomOAuth2User;
import hello.hello.yju.service.chat.ChatMessageService;
import hello.hello.yju.service.chat.ChatService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class ChatController {

    private final ChatService chatService;
    private final ChatMessageService chatMessageService;

    @PostMapping("/chat/create/{itemId}")
    public String createChatRoom(@PathVariable Long itemId, Authentication authentication, Model model) {
        CustomOAuth2User currentUser = (CustomOAuth2User) authentication.getPrincipal();
        String userId = currentUser.getGoogleId();

        ChatRoomRequest request = ChatRoomRequest.builder()
                .itemId(itemId)
                .buyerId(userId)
                .build();

        ChatRoomDto chatRoomDto = chatService.createChatRoom(request);

        model.addAttribute("chatRoomId", chatRoomDto.getId());
        model.addAttribute("senderId", userId);
        model.addAttribute("senderName", currentUser.getName());
        model.addAttribute("itemName", chatRoomDto.getItemName());

        return "chat";
    } 


    @GetMapping("/chat/{chatRoomId}")
    public String chatRoom(@PathVariable Long chatRoomId, Authentication authentication, Model model) {
        chatService.getChatRoom(chatRoomId);
        CustomOAuth2User user = (CustomOAuth2User) authentication.getPrincipal();
        String userId = user.getGoogleId();
        String userName = user.getName();

        model.addAttribute("chatRoomId", chatRoomId);
        model.addAttribute("senderId", userId);
        model.addAttribute("senderName", userName);

        return "chat";
    }

    @MessageMapping("/chat/send/{chatRoomId}")
    @SendTo("/sub/chat/{chatRoomId}")
    public ChatMessageDto sendMessage(@DestinationVariable Long chatRoomId, @Payload ChatMessageRequest request) {
        request.setChatRoomId(chatRoomId);
        return chatMessageService.saveMessage(request);

    }


    @GetMapping("/chat/messages/{chatRoomId}")
    @ResponseBody
    public List<ChatMessageDto> getChatMessages(@PathVariable Long chatRoomId) {
        return chatMessageService.getChatMessages(chatRoomId);
    }

    @PostMapping("/chat/messages/send")
    public ResponseEntity<ChatMessageDto> sendMessage(@RequestBody ChatMessageRequest request) {
        ChatMessageDto savedMessageDto = chatMessageService.saveMessage(request);

        return ResponseEntity.ok(savedMessageDto);
    }


    @GetMapping("/myInfo/rooms")
    public String userChatRooms(Authentication authentication, Model model) {
        CustomOAuth2User user = (CustomOAuth2User) authentication.getPrincipal();
        List<ChatRoomDto> chatRooms = chatService.getUserChatRooms(user.getGoogleId());

        model.addAttribute("chatRooms", chatRooms);
        return "myInfo";
    }

}
