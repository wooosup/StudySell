package hello.hello.yju.controller;

import hello.hello.yju.dto.ChatMessageDto;
import hello.hello.yju.dto.ChatRoomDto;
import hello.hello.yju.dto.CustomOAuth2User;
import hello.hello.yju.dto.ItemFormDto;
import hello.hello.yju.entity.ChatMessage;
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

    @GetMapping("/chat/{itemId}")
    public String chatRoom(@PathVariable Long itemId, Authentication authentication, Model model) {
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

    @GetMapping("/chat/messages/{chatRoomId}")
    @ResponseBody
    public List<ChatMessageDto> getChatMessages(@PathVariable Long chatRoomId) {
        return chatService.getChatMessages(chatRoomId).stream()
                .map(chatMessage -> {
                    ChatMessageDto dto = new ChatMessageDto();
                    dto.setId(chatMessage.getId());
                    dto.setChatRoomId(chatRoomId);
                    dto.setSenderId(chatMessage.getSender().getGoogleId());
                    dto.setSenderName(chatMessage.getSenderName().getName());
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

    @MessageMapping("/chat/send/{chatRoomId}")
    @SendTo("/sub/chat/{chatRoomId}")
    public ChatMessageDto sendMessage(@DestinationVariable Long chatRoomId, @Payload ChatMessageDto chatMessageDto) {
        return chatMessageService.saveMessage(chatRoomId, chatMessageDto.getMessage(), chatMessageDto.getSenderId(), chatMessageDto.getSenderName());
    }
}
