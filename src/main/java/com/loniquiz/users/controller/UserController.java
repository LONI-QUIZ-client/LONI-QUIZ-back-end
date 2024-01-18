package com.loniquiz.users.controller;

import com.loniquiz.users.dto.request.UserNewRequestDTO;
import com.loniquiz.users.dto.response.UserDetailResponseDTO;
import com.loniquiz.users.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/user")
public class UserController {
    private final UserService userService;


    // 회원 단일 조회를 위한 컨트롤러
    @GetMapping("/{id}")
    public ResponseEntity<?> detailUser(@PathVariable String id){

        log.info("user id : {}", id);
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


    // 회원가입을 위한 컨트롤러
    @PostMapping
    public ResponseEntity<?> newUserCreate(
            @Validated
            @RequestBody UserNewRequestDTO dto
            , BindingResult result
    ){
        // 입력 값 검증에 걸리면 안 됌 ㅠㅠㅠㅠ
        if (result.hasErrors()){
            return ResponseEntity.badRequest()
                    .body(
                            result.getFieldError()
                    );
        }

        boolean login = userService.newUser(dto);

        log.info("회원가입을 위한 post매핑 접속 dto : {}", dto);

        if (!login){ // 회원가입 실패시
            return ResponseEntity.badRequest()
                    .body(
                            "값 똑바로 줘야돼 이거 뜨면 정범준한테 말해"
                    );
        }else{ // 회원가입 성공시
            return ResponseEntity.ok()
                    .body(
                            "나이스 회원가입 성공띠"
                    );
        }

    }
}
