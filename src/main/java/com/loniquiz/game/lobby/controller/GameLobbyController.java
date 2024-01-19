package com.loniquiz.game.lobby.controller;

import com.loniquiz.game.lobby.dto.response.GamLobbyListResponseDTO;
import com.loniquiz.game.lobby.entity.GameLobby;
import com.loniquiz.game.lobby.dto.request.PageRequestDTO;
import com.loniquiz.game.lobby.service.GameLobbyService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/game/lobby")
public class GameLobbyController {
    private final GameLobbyService gameLobbyService;

    @GetMapping
    public ResponseEntity<?> gameListView(PageRequestDTO dto){
        GamLobbyListResponseDTO gameLobbies = gameLobbyService.gameAllList(dto);

        return ResponseEntity.ok()
                .body(
                        gameLobbies
                );
    }
}
