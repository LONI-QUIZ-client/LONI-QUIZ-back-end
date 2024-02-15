package com.loniquiz.follwer.controller;

import com.loniquiz.auth.TokenUserInfo;
import com.loniquiz.follwer.dto.request.FollwerRequestDTO;
import com.loniquiz.follwer.dto.response.FollwerListResponseDTO;
import com.loniquiz.follwer.service.FollwerService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/follower")
@CrossOrigin(origins = {"http://localhost:3000"}, allowCredentials = "true")
public class FollwerController {
    private final FollwerService follwerService;


    // 팔로워하는 기능
    @PostMapping
    public ResponseEntity<?> followerSave(
            @RequestBody
            FollwerRequestDTO dto
    ){
        List<FollwerListResponseDTO> dtoList = follwerService.saveFollower(dto);

        return ResponseEntity.ok()
                .body(
                        dtoList
                );
    }

    @GetMapping
    public ResponseEntity<?> findFollowerList(
            @AuthenticationPrincipal TokenUserInfo userInfo
    ){
        List<FollwerListResponseDTO> followerList =
                follwerService.getFollowerList(userInfo.getUserId());

        return ResponseEntity.ok()
                .body(
                        followerList
                );
    }

    @PostMapping("/check")
    public ResponseEntity<?> flag(
            @RequestBody
            FollwerRequestDTO dto
    ){


        boolean flag = follwerService.follower(dto);

        return ResponseEntity.ok()
                .body(
                        flag
                );
    }
}
