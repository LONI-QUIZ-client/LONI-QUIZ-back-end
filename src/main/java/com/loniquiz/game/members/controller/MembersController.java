package com.loniquiz.game.members.controller;


import com.loniquiz.game.lobby.dto.request.GameRoomRequestDTO;
import com.loniquiz.game.lobby.dto.response.GameDetailDTO;
import com.loniquiz.game.members.dto.request.UpScoreRequestDTO;
import com.loniquiz.game.members.service.MembersService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.websocket.server.PathParam;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/game/room")
@CrossOrigin(origins = {"http://localhost:3000"}, allowCredentials = "true")
public class MembersController {
    private final MembersService membersService;

    // 값 전달을 위한 처리
    @PostMapping
    public ResponseEntity<?> gameRoomDetail(
            @RequestBody GameRoomRequestDTO dto
    ){
        try {
            GameDetailDTO userCheck = membersService.isUserCheck(dto);

            return ResponseEntity.ok()
                    .body(
                            userCheck
                    );

        }catch (RuntimeException e){
            return ResponseEntity.badRequest()
                    .body(
                            e.getMessage()
                    );
        }
    }

    @GetMapping("/{gno}")
    public ResponseEntity<?> lobbyList(
            @PathVariable String gno
    ){
        GameDetailDTO detail = membersService.detail(gno);

        if (gno == null){
            return ResponseEntity.badRequest().body(
                    "방 정보가 없다"
            );
        }

        else {
            return ResponseEntity.ok()
                    .body(
                            detail
                    );
        }
    }

    // 스코어 상승 처리
    @PatchMapping("/upScore")
    public ResponseEntity<?> upScore(
            @RequestBody UpScoreRequestDTO dto
            ){

        GameDetailDTO gameDetailDTO = membersService.upCount(dto);
        return ResponseEntity.ok()
                .body(
                        gameDetailDTO
                );
    }
}
