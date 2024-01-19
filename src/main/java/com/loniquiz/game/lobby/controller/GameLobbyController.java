package com.loniquiz.game.lobby.controller;

import com.loniquiz.game.lobby.dto.response.GameLobbyListResponseDTO;
import com.loniquiz.game.lobby.dto.request.PageRequestDTO;
import com.loniquiz.game.lobby.service.GameLobbyService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/game/lobby")
@CrossOrigin(origins = {"http://localhost:3000"}, allowCredentials = "true")
public class GameLobbyController {
    private final GameLobbyService gameLobbyService;


    // 게임 방 전체 조회
    @GetMapping
    public ResponseEntity<?> gameListView(PageRequestDTO dto){
        GameLobbyListResponseDTO gameLobbies = gameLobbyService.gameAllList(dto);

        return ResponseEntity.ok()
                .body(
                        gameLobbies
                );
    }


}
