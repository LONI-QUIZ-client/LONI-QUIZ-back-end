package com.loniquiz.users.service;

import com.loniquiz.auth.TokenProvider;
import com.loniquiz.users.dto.request.UserLoginRequestDTO;
import com.loniquiz.users.dto.request.UserNewRequestDTO;
import com.loniquiz.users.dto.response.*;
import com.loniquiz.users.entity.User;
import com.loniquiz.users.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Map;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder encoder;
    private final TokenProvider tokenProvider;

    @Value("${kakao.kakaoClientID}")
    private String KAKAO_CLIENT_ID;

    @Value("${kakao.kakaoRedirectURI}")
    private String KAKAO_REDIRECT_URI;

    @Value("${kakao.kakaoClientSecret}")
    private String KAKAO_CLIENT_SECRET;

    @Value("${kakao.kakaoTokenURI}")
    private String KAKAO_TOKEN_URI;

    @Value("${kakao.kakaoUserInfoURI}")
    private String KAKAO_USER_INFO_URI;

    // 회원 단일 정보 조회
    public UserDetailResponseDTO detail(String id){
        User user = userRepository.findById(id).orElseThrow();
        return new UserDetailResponseDTO(user);
    }

    // 회원가입 처리
    public boolean newUser(UserNewRequestDTO dto, String savePath) {
        if (dto != null){ // 회원가입할때 값이 null이 아닐 때만 저장
            userRepository.save(dto.isEntity(encoder, savePath));
            return true;
        }else { // 회원가입 할때 값을 좆 같이 주면 노 저장
            return false;
        }
    }

    // 로그인 처리
    public UserResponseDTO login(UserLoginRequestDTO dto){
        User user = userRepository.findById(dto.getId()).orElseThrow(
                () -> new RuntimeException("아이디가 존재하지 않습니다")
        );


        String userPw = dto.getPw();
        String encodingPw = user.getPw();

        if (!encoder.matches(userPw, encodingPw)){
            throw new RuntimeException("비밀번호가 일치하지 않습니다");
        }
        String token = tokenProvider.createToken(user);
        return new UserResponseDTO(user, token);
    }


    // 회원 탈퇴 기능
    public boolean delete(String id){
        if (id != null){
            userRepository.deleteById(id);
            return true;
        }
        else {
            return false;
        }
    }


    public boolean isDuplicateId(String type, String keyword){

        if (type.equals("id")){
            boolean flag = userRepository.existsById(keyword);
            return flag;
        }

        if (type.equals("nickname")){
            boolean flag = userRepository.existsByNickname(keyword);
            return flag;
        }

        return false;
    }


    public String getProfileImage(String id){
        User user = userRepository.findById(id).orElseThrow();

        String profileImage = user.getProfileImage();

        return profileImage;
    }

    public List<User> orderByScore(){
        List<User> allByOrderByScore = userRepository.findAllByOrderByScore();
        return allByOrderByScore;
    }

    // 카카오 로그인 처리
    public void kakaoLogin(String accessToken) {

        // 토큰을 통해서 사용자 정보 가져오기
        Object kakaoUserInfo = getKakaoUserInfo(accessToken);
        System.out.println("kakaoUserInfo = " + kakaoUserInfo);


    }
    // 토큰으로 사용자 정보 요청
    private Object getKakaoUserInfo(String accessToken) {

        // 요청 헤더
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer " + accessToken);
        headers.add("Content-type", "application/x-www-form-urlencoded;charset=utf-8");

        // 요청 보내기
        RestTemplate template = new RestTemplate();
        ResponseEntity<?> responseEntity = template.exchange(
                KAKAO_USER_INFO_URI,
                HttpMethod.POST,
                new HttpEntity<>(headers),
                KakaoUserResponseDTO.class
        );

        Object body = responseEntity.getBody();
        return body;

    }

    public String getKakaoAccessToken(String code) {

        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-type", "application/x-www-form-urlencoded;charset=utf-8");

        // Http Response Body 객체 생성
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("grant_type", "authorization_code"); //카카오 공식문서 기준 authorization_code 로 고정
        params.add("client_id", KAKAO_CLIENT_ID); // 카카오 Dev 앱 REST API 키
        params.add("redirect_uri", KAKAO_REDIRECT_URI); // 카카오 Dev redirect uri
        params.add("code", code); // 프론트에서 인가 코드 요청시 받은 인가 코드값

        RestTemplate template = new RestTemplate();

        HttpEntity<Object> requestEntity = new HttpEntity<>(params, headers);

        ResponseEntity<Map> responseEntity = template.exchange(KAKAO_TOKEN_URI, HttpMethod.POST, requestEntity, Map.class);
        Map<String, Object> responseJSON = (Map<String, Object>) responseEntity.getBody();
        String accessToken = (String) responseJSON.get("access_token");
        System.out.println("accessToken = " + accessToken);
        return accessToken;
    }
}
