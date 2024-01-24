package com.loniquiz.game.members.controller;


import com.loniquiz.game.lobby.dto.request.GameRoomRequestDTO;
import com.loniquiz.game.lobby.dto.response.GameDetailDTO;
import com.loniquiz.game.members.service.MembersService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/game/room")
@CrossOrigin(origins = {"http://localhost:3000"}, allowCredentials = "true")
public class MembersController {
    private final MembersService membersService;

    // 값 전달을 위한 처리
    @PostMapping()
    public ResponseEntity<?> gameRoomDetail(
            @RequestBody GameRoomRequestDTO dto
    ){

        try {
            GameDetailDTO userCheck = membersService.isUserCheck(dto);


            log.info("detail : {}", userCheck);


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

    // 스코어 상승 처리

}
