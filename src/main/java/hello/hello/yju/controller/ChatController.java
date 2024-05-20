package hello.hello.yju.controller;

import hello.hello.yju.entity.ChatMessage;
import hello.hello.yju.entity.ChatRoom;
import hello.hello.yju.entity.UserEntity;
import hello.hello.yju.repository.UserRepository;
import hello.hello.yju.service.ChatMessageService;
import hello.hello.yju.service.ChatRoomService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;


@Controller
public class ChatController {

    private final ChatRoomService chatRoomService;
    private final ChatMessageService chatMessageService;
    private final SimpMessagingTemplate messagingTemplate;

    @Autowired
    public ChatController(ChatRoomService chatRoomService, ChatMessageService chatMessageService, SimpMessagingTemplate messagingTemplate) {
        this.chatRoomService = chatRoomService;
        this.chatMessageService = chatMessageService;
        this.messagingTemplate = messagingTemplate;
    }

    @GetMapping("/item/room/{room_Id}")
    public String chatRoom(@PathVariable Long roomId, Model model) {
        ChatRoom room = chatRoomService.findById(roomId);
        List<ChatMessage> messages = chatMessageService.findAllMessages(roomId);
        model.addAttribute("room", room);
        model.addAttribute("messages", messages);
        return "room";
    }

    @GetMapping("/item/room/create")
    public String createRoom(@RequestParam String seller, @RequestParam String buyer, @RequestParam Long itemId) {
        chatRoomService.createRoom(seller, buyer, itemId);
        return "redirect:/";
    }

    @PostMapping("/item/room/{roomId}/leave")
    public String leaveRoom(@PathVariable Long roomId, Principal principal) {
        String username = principal.getName();
        ChatRoom room = chatRoomService.findById(roomId);

        if (room.getSeller().getName().equals(username) || room.getBuyer().getName().equals(username)) {
            chatRoomService.deleteRoom(roomId);
        }

        return "redirect:/";
    }

    @MessageMapping("/chat.sendMessage")
    public void sendMessage(ChatMessage message) {
        chatMessageService.saveMessage(message);
        messagingTemplate.convertAndSend("/topic/" + message.getChatRoom().getId(), message);
    }
}