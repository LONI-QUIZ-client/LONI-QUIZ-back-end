package com.loniquiz.comment.lobby.controller;

import com.loniquiz.comment.lobby.dto.request.CreateImageRequestDTO;
import com.loniquiz.comment.lobby.dto.request.LobbyChatCreateRequestDTO;
import com.loniquiz.comment.lobby.dto.response.CreatedImageResponseDTO;
import com.loniquiz.comment.lobby.dto.response.LobbyChatListResponseDTO;
import com.loniquiz.comment.lobby.entity.LobbyChat;
import com.loniquiz.comment.lobby.service.LobbyChatService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/game/lobbyChat")
@CrossOrigin(origins = {"http://localhost:3000"}, allowCredentials = "true")
public class LobbyChatController {

    private final LobbyChatService lobbyChatService;

    // 채팅 등록 요청
    @PostMapping
    public ResponseEntity<?> createChat(
            @Validated @RequestBody LobbyChatCreateRequestDTO dto,
            BindingResult result) {

        if (result.hasErrors()) {
            return ResponseEntity.badRequest().body(result.getFieldError());
        }

        try {
            LobbyChatListResponseDTO dtoList = lobbyChatService.create(dto);
            return ResponseEntity.ok().body(dtoList);
        } catch (Exception e) {
            log.error(e.getMessage());
            return ResponseEntity
                    .internalServerError()
                    .body(e.getMessage());
        }
    }

    // 채팅 목록 50개씩 조회
    @GetMapping
    public ResponseEntity<?> lobbyChatView() {
        log.info("/game/lobbyChat");
        LobbyChatListResponseDTO chats = lobbyChatService.getChats();
        System.out.println("chats = " + chats);
        return ResponseEntity.ok().body(chats);
    }

    @PostMapping("/imageCreate")
    public ResponseEntity<?> imageCreate(@Validated
                                             @RequestBody CreateImageRequestDTO dto
    ) {
        CreatedImageResponseDTO image = lobbyChatService.createImage(dto);
        return ResponseEntity.ok().body(image);
    }

}
