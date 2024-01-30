package com.loniquiz.users.service;

import com.loniquiz.auth.TokenProvider;
import com.loniquiz.users.dto.request.UserLoginRequestDTO;
import com.loniquiz.users.dto.request.UserNewRequestDTO;
import com.loniquiz.users.dto.response.UserDetailResponseDTO;
import com.loniquiz.users.dto.response.UserResponseDTO;
import com.loniquiz.users.entity.User;
import com.loniquiz.users.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder encoder;
    private final TokenProvider tokenProvider;

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
}
