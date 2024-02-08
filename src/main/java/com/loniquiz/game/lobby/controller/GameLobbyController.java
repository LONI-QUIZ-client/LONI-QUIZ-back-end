package com.loniquiz.game.lobby.controller;

import com.loniquiz.game.lobby.dto.request.GameLobbyCreateDTO;
import com.loniquiz.game.lobby.dto.request.GameRoomRequestDTO;
import com.loniquiz.game.lobby.dto.response.GameDetailDTO;
import com.loniquiz.game.lobby.dto.response.GameLobbyListResponseDTO;
import com.loniquiz.game.lobby.dto.request.PageRequestDTO;
import com.loniquiz.game.lobby.service.GameLobbyService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/game/lobby")
@CrossOrigin(origins = {"http://localhost:3000"}, allowCredentials = "true")
public class GameLobbyController {
    private final GameLobbyService gameLobbyService;


    // 게임 방 전체 조회
    @GetMapping
    public ResponseEntity<?> gameListView(PageRequestDTO dto) {
        GameLobbyListResponseDTO gameLobbies = gameLobbyService.gameAllList(dto);

        return ResponseEntity.ok()
                .body(
                        gameLobbies
                );
    }


    // 방 생성
    @PostMapping
    public ResponseEntity<?> lobbyCreate(
            PageRequestDTO pageRequestDTO,
            @Validated
            @RequestBody GameLobbyCreateDTO dto,
            BindingResult result
    ) {
        if (result.hasErrors()) {
            return ResponseEntity.badRequest()
                    .body(
                            result.getFieldError()
                    );
        }
        try {
            log.info("저장 완료 : {}", dto);
            GameLobbyListResponseDTO gameLobby = gameLobbyService.createGameLobby(dto, pageRequestDTO);

            return ResponseEntity.ok()
                    .body(
                            gameLobby
                    );
        } catch (Exception e) {
            log.warn("방 생성중 서버 에러 : {}", e.getMessage());
            return ResponseEntity.internalServerError()
                    .body(
                            e.getMessage()
                    );
        }
    }

    // 방 삭제 처리
    @DeleteMapping("/gno/{gno}/id/{userId}")
    public ResponseEntity<?> deleteRobbie(
            @PathVariable String gno,
            @PathVariable String userId,
            PageRequestDTO pageRequest
    ) {

        try {
            GameLobbyListResponseDTO gameList = gameLobbyService.deleteLobby(gno, userId, pageRequest);

            return ResponseEntity.ok()
                    .body(
                            gameList
                    );
        } catch (Exception e) {
            log.error("내뇌 내놔 아이디랑 게임 아이디");
            return ResponseEntity.internalServerError()
                    .body(e.getMessage());
        }
    }
}


