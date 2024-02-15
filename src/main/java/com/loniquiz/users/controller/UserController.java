package com.loniquiz.users.controller;

import com.loniquiz.auth.TokenProvider;
import com.loniquiz.auth.TokenUserInfo;
import com.loniquiz.game.lobby.dto.request.UserSearchRequestDTO;
import com.loniquiz.users.dto.request.UserLoginRequestDTO;
import com.loniquiz.users.dto.request.UserNewRequestDTO;
import com.loniquiz.users.dto.response.KakaoLoginResponseDTO;
import com.loniquiz.users.dto.response.UserDetailResponseDTO;
import com.loniquiz.users.dto.response.UserResponseDTO;
import com.loniquiz.users.dto.response.UserSearchResponseDTO;
import com.loniquiz.users.dto.response.UserSortResponseDTO;
import com.loniquiz.users.entity.User;
import com.loniquiz.users.service.UserService;
import com.loniquiz.utils.upload.FileUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.coyote.Response;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.util.FileCopyUtils;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.IOException;
import java.util.List;

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
    public ResponseEntity<?> detailUser(@PathVariable String id) {

        log.info("user id : {}", id);
        try {
            UserDetailResponseDTO user = userService.detail(id);
            return ResponseEntity.ok()
                    .body(
                            user
                    );
        } catch (Exception e) {
            return ResponseEntity.internalServerError()
                    .body(e.getMessage());
        }
    }


    // 회원가입을 위한 컨트롤러
    @PostMapping
    public ResponseEntity<?> newUserCreate(
            @Validated
            @RequestPart("user") UserNewRequestDTO dto,
            @RequestPart(value = "profileImage", required = false) MultipartFile profileImage
            , BindingResult result
    ) {
        // 입력 값 검증에 걸리면 안 됌 ㅠㅠㅠㅠ
        if (result.hasErrors()) {
            return ResponseEntity.badRequest()
                    .body(
                            result.getFieldError()
                    );
        }

        String uploadProfileImagePath = null;

        File file = new File(rootPath);
        if (!file.exists()) file.mkdirs();

        if(profileImage!=null){
            uploadProfileImagePath = FileUtil.uploadFile(profileImage, rootPath);
        }

        boolean newUser = userService.newUser(dto, uploadProfileImagePath);


        /*File file = new File(rootPath);
        if (!file.exists()) file.mkdirs();
        String savePath = FileUtil.uploadFile(profileImage, rootPath);
        boolean newUser = userService.newUser(dto, savePath);*/

        log.info("회원가입을 위한 post매핑 접속 dto : {}", dto);

        if (!newUser) { // 회원가입 실패시
            return ResponseEntity.badRequest()
                    .body(
                            "값 똑바로 줘야돼 이거 뜨면 정범준한테 말해"
                    );
        } else { // 회원가입 성공시
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
    ) {
        try {
            UserResponseDTO loginStatus = userService.login(dto);
            return ResponseEntity.ok().body(loginStatus);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // 로그아웃 처리
    @PostMapping("/logout/{id}")
    public ResponseEntity<?> logoutUser(
            @PathVariable String id
    ){
        UserDetailResponseDTO logout = userService.logout(id);

        return ResponseEntity.ok().body(logout);
    }


    // 회원 탈퇴 컨트롤러
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteUser(
            @PathVariable String id
            , @RequestBody UserLoginRequestDTO dto
    ) {

        boolean checked = userService.checkMyself(dto);

        if(!checked){
            return ResponseEntity.badRequest()
                    .body(
                      "비밀번호가 알맞지 않음"
                    );
        }

        boolean flag = userService.delete(id);

        if (flag) { // 삭제 정상 처리
            return ResponseEntity.ok()
                    .body(
                            "삭제 완료띠"
                    );
        } else { // 삭제 실패
            return ResponseEntity.badRequest()
                    .body(
                            "아이디 값을 한번 확인해 보자"
                    );
        }
    }

    // 아이디 중복 체크 중복 체크
    @GetMapping("/check")
    public ResponseEntity<?> checkUser(String type, String keyword) {
        boolean flag = userService.isDuplicateId(type, keyword);

        return ResponseEntity.ok()
                .body(
                        flag
                );
    }

    /*
    다른 회원의 이미즈를 뿌려주기 위한 코드
     */

    @GetMapping("/profile-image/{id}")
    public ResponseEntity<?> userProfileImage(
            @PathVariable String id
    ){
        log.info("profile-image/id GET");


        try {
            String profilePath = userService.getProfileImage(id);
            if(profilePath==null){
                return ResponseEntity.ok().body(null);
            }

            File profileFile = new File(rootPath + profilePath);
            if(!profileFile.exists()) return ResponseEntity.notFound().build();

            byte[] fileDate = FileCopyUtils.copyToByteArray(profileFile);

            HttpHeaders headers = new HttpHeaders();

            MediaType mediaType = extractFileExtension(profilePath);

            if (mediaType == null){
                return ResponseEntity.internalServerError()
                        .body("발견된 파일은 이미지가 아닙니다.");
            }

            headers.setContentType(mediaType);

            return ResponseEntity.ok()
                    .headers(headers)
                    .body(fileDate);

        } catch (IOException e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }


    @GetMapping("/profile-image")
    public ResponseEntity<?> loadProfileImage(
            @AuthenticationPrincipal TokenUserInfo userInfo
            ){
        try {
            String profilePath = userService.getProfileImage(userInfo.getUserId());

            File profileFile = new File(rootPath + profilePath);
            if(!profileFile.exists()) return ResponseEntity.notFound().build();

            byte[] fileDate = FileCopyUtils.copyToByteArray(profileFile);

            HttpHeaders headers = new HttpHeaders();

            MediaType mediaType = extractFileExtension(profilePath);

            if (mediaType == null){
                return ResponseEntity.internalServerError()
                        .body("발견된 파일은 이미지가 아닙니다.");
            }

            headers.setContentType(mediaType);

            return ResponseEntity.ok()
                    .headers(headers)
                    .body(fileDate);

        } catch (IOException e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }


    private MediaType extractFileExtension(String filePath){
        String ext = filePath.substring(filePath.lastIndexOf(".") + 1);

        switch (ext.toUpperCase()){
            case "JPEG": case "JPG":
                return MediaType.IMAGE_JPEG;
            case "PNG":
                return MediaType.IMAGE_PNG;
            case "GIF":
                return MediaType.IMAGE_GIF;
            default:
                return null;
        }
    }

    @GetMapping("/order/score")
    public ResponseEntity<?> orderScore(){
        List<UserSortResponseDTO> users = userService.orderByScore();
        return ResponseEntity.ok()
                .body(
                        users
                );
    }

    @GetMapping("/oauth")
    public  ResponseEntity<?> kakaoLogin(HttpServletRequest request) {
        String code = request.getParameter("code");
        System.out.println("code = " + code);
        String kakaoAccessToken = userService.getKakaoAccessToken(code);
        UserResponseDTO userResponseDTO = userService.kakaoLogin(kakaoAccessToken);
        return ResponseEntity.ok().body(userResponseDTO);
    }

    // 회원 정보 검색 및 뿌리기
    @PostMapping("/nickname")
    public ResponseEntity<?> searchUser(
            @RequestBody UserSearchRequestDTO dto
    ){
        List<UserSearchResponseDTO> user = userService.findUser(dto.getNickname());
        return ResponseEntity
                .ok()
                .body(
                        user
                );
    }

}
