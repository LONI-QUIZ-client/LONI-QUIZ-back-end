package com.loniquiz.users.controller;

import com.loniquiz.users.dto.request.UserLoginRequestDTO;
import com.loniquiz.users.dto.request.UserNewRequestDTO;
import com.loniquiz.users.dto.response.UserDetailResponseDTO;
import com.loniquiz.users.dto.response.UserResponseDTO;
import com.loniquiz.users.service.UserService;
import com.loniquiz.utils.upload.FileUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/user")
@CrossOrigin(origins = {"http://localhost:3000"}, allowCredentials = "true")
public class UserController {
    private final UserService userService;

    @Value("${root.path}")
    private String rootPath;


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


        File file = new File(rootPath);

        if (!file.exists()) file.mkdirs();

        String savePath = FileUtil.uploadFile(dto.getProfile(), rootPath);

        boolean newUser = userService.newUser(dto, savePath);

        log.info("회원가입을 위한 post매핑 접속 dto : {}", dto);

        if (!newUser){ // 회원가입 실패시
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


    // 로그인 처리 컨트롤러
    @PostMapping("/login")
    public ResponseEntity<?> loginUser(
             @RequestBody UserLoginRequestDTO dto //dkdkdk
    ){
        try{
            UserResponseDTO loginStatus = userService.login(dto);

            return ResponseEntity.ok()
                    .body(
                            loginStatus
                    );
        }catch (Exception e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // 회원 탈퇴 컨트롤러
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteUser(
            @PathVariable String id
    ){
        boolean flag = userService.delete(id);

        if (flag){ // 삭제 정상 처리
            return ResponseEntity.ok()
                    .body(
                            "삭제 완료띠"
                    );
        }

        else{ // 삭제 실패
            return ResponseEntity.badRequest()
                    .body(
                            "아이디 값을 한번 확인해 보자"
                    );
        }
    }

    // 아이디 중복 체크 중복 체크
    @GetMapping("/check")
    public ResponseEntity<?> checkUser(String type, String keyword){
        boolean flag = userService.isDuplicateId(type, keyword);

        return ResponseEntity.ok()
                .body(
                        flag
                );
    }
}
