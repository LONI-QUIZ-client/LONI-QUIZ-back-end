package com.loniquiz.users.service;

import com.loniquiz.auth.TokenProvider;
import com.loniquiz.aws.S3Service;
import com.loniquiz.users.dto.request.UserLoginRequestDTO;
import com.loniquiz.users.dto.request.UserNewRequestDTO;

import com.loniquiz.users.dto.response.*;

import com.loniquiz.users.dto.response.UserDetailResponseDTO;
import com.loniquiz.users.dto.response.UserResponseDTO;
import com.loniquiz.users.dto.response.UserSearchResponseDTO;
import com.loniquiz.users.dto.response.UserSortResponseDTO;

import com.loniquiz.users.entity.User;
import com.loniquiz.users.repository.UserRepository;
import com.sun.xml.bind.v2.runtime.output.Encoded;
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
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import java.util.UUID;
import java.util.stream.Collectors;

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

    private final S3Service s3Service;

    @Value("${root.path}")
    private String rootPath; // 파일 저장 루트경로

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

    @Transactional
    public void updateScore(String userId, int point) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다: " + userId));

        // 기존 점수에 새로운 포인트를 더합니다.
        int newScore = user.getScore() + point;
        user.setScore(newScore);

        userRepository.save(user);
    }

    // 프로필 변경
    public boolean changeProfile(
            String profilePath
            , String id
    ){
        if(profilePath!=null){
            Optional<User> byId = userRepository.findById(id);
            if (byId.isPresent()) { // 해당 ID에 해당하는 사용자가 존재하는지 확인
                User user = byId.get();
                user.setProfileImage(profilePath); // 프로필 이미지 경로 설정
                userRepository.save(user);
                return true;
            } else {
                log.info("해당 ID에 해당하는 사용자가 존재하지 않음");
                return false; // 해당 ID에 해당하는 사용자가 존재하지 않음
            }
        } else {
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
        user.setLoginState(true);

        return new UserResponseDTO(user, token);
    }

    // 로그아웃 처리
    public UserDetailResponseDTO logout(String id){
        User user = userRepository.findById(id).orElseThrow(
                () -> new RuntimeException("아이디가 존재하지 않습니다")
        );

        user.setLoginState(false);

        return new UserDetailResponseDTO();
    }

    // 회원인증
    public boolean checkMyself(UserLoginRequestDTO dto){
        Optional<User> userInfo = userRepository.findById(dto.getId());

        if(userInfo.isPresent()){
            User user = userInfo.get();
            String storedPw = user.getPw();

            return encoder.matches(dto.getPw(), storedPw);
        }

        return false;
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

    public List<UserSortResponseDTO> orderByScore(){
        List<User> SortUser = userRepository.findAllByOrderByScore();

        List<UserSortResponseDTO> dtoList = SortUser.stream()
                .map(user -> new UserSortResponseDTO(user))
                .collect(Collectors.toList());
        return dtoList;
    }

    // 사용자 검색
    public List<UserSearchResponseDTO> findUser(String nickname){
        List<User> nickName = userRepository.findByNicknameContaining(nickname);

        List<UserSearchResponseDTO> userList = nickName.stream()
                .map(user -> new UserSearchResponseDTO(user))
                .collect(Collectors.toList());

        return userList;
    }

    // 카카오 로그인 처리
    public UserResponseDTO kakaoLogin(String accessToken) {

        // 토큰을 통해서 사용자 정보 가져오기
        KakaoUserResponseDTO kakaoUserInfo = getKakaoUserInfo(accessToken);
        System.out.println("kakaoUserInfo = " + kakaoUserInfo);


        if (!userRepository.existsByNickname(kakaoUserInfo.getProperties().getNickname())){
            User user = userRepository.save(new KakaoLoginSaveResponseDTO().toEntity(kakaoUserInfo, encoder));
            UserResponseDTO login = login(
                    UserLoginRequestDTO.builder()
                            .id(kakaoUserInfo.getId())
                            .pw("0000")
                            .build()
            );
            return login;
        }else {
            UserResponseDTO login = login(
                    UserLoginRequestDTO.builder()
                            .id(kakaoUserInfo.getId())
                            .pw("0000")
                            .build()
            );
            return login;
        }
    }
    // 토큰으로 사용자 정보 요청
    private KakaoUserResponseDTO getKakaoUserInfo(String accessToken) {

        // 요청 헤더
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer " + accessToken);
        headers.add("Content-type", "application/x-www-form-urlencoded;charset=utf-8");

        // 요청 보내기
        RestTemplate template = new RestTemplate();
        ResponseEntity<KakaoUserResponseDTO> responseEntity = template.exchange(
                KAKAO_USER_INFO_URI,
                HttpMethod.POST,
                new HttpEntity<>(headers),
                KakaoUserResponseDTO.class
        );

        KakaoUserResponseDTO body = responseEntity.getBody();

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


    public String uploadProfileImage(MultipartFile originalFile) throws IOException {

        //루트 디렉코리가 존재하는지 확인 후 존재하지 않으면 생성한다.
//        File rootDir = new File(rootPath);

//        if (!rootDir.exists()) rootDir.mkdir();

        //파일명을 유니크하게 변경
        String uniqueFileName = UUID.randomUUID() + "_" + originalFile.getOriginalFilename();

        //파일을 서버에 저장
//        File uploadFile = new File(rootPath + "/" + uniqueFileName);
//        originalFile.transferTo(uploadFile);

        // 파일을 s3버킷에 저장
        String uploadedURL = s3Service.uploadToS3Bucket(originalFile.getBytes(), uniqueFileName);

        return uploadedURL;
    }

    // 로그인한 회원의 프로필 사진 저장 경로를 조회
    public String getProfilePath(String id){
        // db에서 파일명을 조회
        User user = userRepository.findById(id).orElseThrow();

        String fileName = user.getProfileImage();

        return fileName;
    }
}
