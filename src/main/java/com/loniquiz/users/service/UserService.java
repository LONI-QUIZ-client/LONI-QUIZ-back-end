package com.loniquiz.users.service;

import com.loniquiz.users.dto.response.UserDetailResponseDTO;
import com.loniquiz.users.entity.User;
import com.loniquiz.users.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional
public class UserService {
    private final UserRepository userRepository;

    // 회원 단일 정보 조회
    public UserDetailResponseDTO detail(String id){
        User user = userRepository.findById(id).orElseThrow();
        return new UserDetailResponseDTO(user);
    }
}
