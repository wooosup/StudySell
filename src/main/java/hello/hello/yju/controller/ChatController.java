package hello.hello.yju.controller;

import hello.hello.yju.dto.ChatMessageDto;
import hello.hello.yju.dto.ChatRoomDto;
import hello.hello.yju.dto.CustomOAuth2User;
import hello.hello.yju.entity.ChatRoom;
import hello.hello.yju.entity.ItemEntity;
import hello.hello.yju.repository.ItemRepository;
import hello.hello.yju.service.ChatMessageService;
import hello.hello.yju.service.ChatService;
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
import java.util.stream.Collectors;

@Controller
@RequiredArgsConstructor
public class ChatController {

    private final ChatService chatService;
    private final ItemRepository itemRepository;
    private final ChatMessageService chatMessageService;

    @PostMapping("/chat/create/{itemId}")
    public String createChatRoom(@PathVariable Long itemId, Authentication authentication, Model model) {
        CustomOAuth2User buyerGoogleId = (CustomOAuth2User) authentication.getPrincipal();
        String buyerId = buyerGoogleId.getGoogleId();
        String buyerName = buyerGoogleId.getName();

        ItemEntity item = itemRepository.findById(itemId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid item ID"));
        String sellerId = item.getUser().getGoogleId();

        ChatRoomDto chatRoomDto = chatService.createChatRoom(sellerId, buyerId, itemId);
        model.addAttribute("chatRoomId", chatRoomDto.getId());
        model.addAttribute("senderId", buyerId);
        model.addAttribute("senderName", buyerName);

        return "chat"; // The chat room HTML page
    } 


    @GetMapping("/chat/{chatRoomId}")
    public String chatRoom(@PathVariable Long chatRoomId, Authentication authentication, Model model) {
        ChatRoom chatRoom = chatService.getChatRoom(chatRoomId);
        CustomOAuth2User user = (CustomOAuth2User) authentication.getPrincipal();
        String userId = user.getGoogleId();
        String userName = user.getName();

        model.addAttribute("chatRoomId", chatRoomId);
        model.addAttribute("senderId", userId);
        model.addAttribute("senderName", userName);

        return "chat"; // The chat room HTML page
    }

    @MessageMapping("/chat/send/{chatRoomId}")
    @SendTo("/sub/chat/{chatRoomId}")
    public ChatMessageDto sendMessage(@DestinationVariable Long chatRoomId, @Payload ChatMessageDto chatMessageDto) {
        return chatMessageService.saveMessage(chatRoomId, chatMessageDto.getMessage(), chatMessageDto.getSenderId(), chatMessageDto.getSenderName());
    }


    @GetMapping("/chat/messages/{chatRoomId}")
    @ResponseBody
    public List<ChatMessageDto> getChatMessages(@PathVariable Long chatRoomId) {
        return chatService.getChatMessages(chatRoomId).stream()
                .map(chatMessage -> {
                    ChatMessageDto dto = new ChatMessageDto();
                    dto.setId(chatMessage.getId());
                    dto.setChatRoomId(chatRoomId);
                    dto.setSenderId(chatMessage.getSender().getGoogleId());
                    dto.setSenderName(chatMessage.getSenderName());
                    dto.setMessage(chatMessage.getMessage());
                    dto.setTimestamp(chatMessage.getTimestamp());
                    return dto;
                })
                .collect(Collectors.toList());
    }

    @PostMapping("/chat/messages/send")
    public ResponseEntity<ChatMessageDto> sendMessage(@RequestBody ChatMessageDto chatMessageDto) {
        ChatMessageDto savedMessageDto = chatMessageService.saveMessage(chatMessageDto.getChatRoomId(), chatMessageDto.getMessage(), chatMessageDto.getSenderId(), chatMessageDto.getSenderName());

        return ResponseEntity.ok(savedMessageDto);
    }


    @GetMapping("/myInfo/rooms")
    public String userChatRooms(Authentication authentication, Model model) {
        CustomOAuth2User user = (CustomOAuth2User) authentication.getPrincipal();
        String userId = user.getGoogleId();
        List<ChatRoom> chatRooms = chatService.getUserChatRooms(userId);

        model.addAttribute("chatRooms", chatRooms);
        return "myInfo"; // 채팅방 목록 페이지
    }

}
