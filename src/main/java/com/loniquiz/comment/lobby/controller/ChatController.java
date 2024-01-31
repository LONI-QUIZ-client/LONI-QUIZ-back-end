package com.loniquiz.comment.lobby.controller;

import com.loniquiz.chatEntity.ChatResponse;
import com.loniquiz.comment.lobby.dto.response.GameChatResponseDTO;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

import java.time.LocalDateTime;
import java.util.Date;

@Controller

public class ChatController {

    @MessageMapping("/chat")
    @SendTo("/topic/messages")
    public ChatResponse sendMessage(@Payload ChatResponse res) {
        res.setTimestamp(new Date());
        return res;
    }

    @MessageMapping("/game/chat")
    @SendTo("/topic/game/messages")
    public GameChatResponseDTO gameSendMessage(@Payload GameChatResponseDTO res){
        res.setLocalDateTime(LocalDateTime.now());
        System.out.println("res = " + res);
        return res;
    }
}
