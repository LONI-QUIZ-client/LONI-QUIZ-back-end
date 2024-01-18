package com.loniquiz.users.service;

import com.loniquiz.users.dto.request.UserLoginRequestDTO;
import com.loniquiz.users.dto.request.UserNewRequestDTO;
import com.loniquiz.users.dto.response.UserDetailResponseDTO;
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

    // 회원 단일 정보 조회
    public UserDetailResponseDTO detail(String id){
        User user = userRepository.findById(id).orElseThrow();
        return new UserDetailResponseDTO(user);
    }

    // 회원가입 처리
    public boolean newUser(UserNewRequestDTO dto) {
        if (dto != null){ // 회원가입할때 값이 null이 아닐 때만 저장
            userRepository.save(dto.isEntity(encoder));
            return true;
        }else { // 회원가입 할때 값을 좆 같이 주면 노 저장
            return false;
        }
    }

    // 로그인 처리
    public String login(UserLoginRequestDTO dto){
        User user = userRepository.findById(dto.getId()).orElseThrow();

        String userId = user.getId();

        if (!userId.equals(dto.getId())){
            return "아이디가 틀림";
        }

        String userPw = dto.getPw();
        String encodingPw = user.getPw();

        if (!encoder.matches(userPw, encodingPw)){
            return "비밀번호가 틀림";
        }

        return "성공";
    }

    public
}
