package com.loniquiz.users.controller;

import com.loniquiz.users.dto.response.UserDetailResponseDTO;
import com.loniquiz.users.entity.User;
import com.loniquiz.users.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/user")
public class UserController {
    private final UserService userService;


    // 회원 단일 조회를 위한 컨트롤러
    @GetMapping("/{id}")
    public ResponseEntity<?> detailUser(@PathVariable String id){
        try{
            UserDetailResponseDTO user = userService.detail(id);
            return ResponseEntity.ok()
                    .body(
                            user
                    );
        }catch (Exception e){
            return ResponseEntity.internalServerError()
                    .body(e.getMessage());
        }
    }
}
