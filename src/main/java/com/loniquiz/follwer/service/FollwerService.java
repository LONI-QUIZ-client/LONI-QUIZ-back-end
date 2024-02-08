package com.loniquiz.follwer.service;

import com.loniquiz.follwer.dto.request.FollwerRequestDTO;
import com.loniquiz.follwer.dto.response.FollwerListResponseDTO;
import com.loniquiz.follwer.entity.Follower;
import com.loniquiz.follwer.repository.FollwerRepository;
import com.loniquiz.users.entity.User;
import com.loniquiz.users.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Slf4j
@Transactional
@RequiredArgsConstructor
public class FollwerService {
    private final FollwerRepository follwerRepository;
    private final UserRepository userRepository;

    // 팔로우 한 사람 저장하기
    public List<FollwerListResponseDTO> saveFollower(FollwerRequestDTO dto){
        User user = findOneUser(dto.getUserId());

        follwerRepository.save(dto.toEnity(user));

        return getFollowerList(dto.getUserId());
    }


    // 팔로워 전체 조회
    public List<FollwerListResponseDTO> getFollowerList(String userId){
        List<Follower> followerList = follwerRepository.findByFollwerId(userId);

        List<FollwerListResponseDTO> dtoList = followerList.stream()
                .map(follower -> new FollwerListResponseDTO(follower))
                .collect(Collectors.toList());


        return dtoList;
    }


    // 단일 유저 정보 찾기를 위한 메서드
    private User findOneUser(String userId){
        User user = userRepository.findById(userId).orElseThrow();
        return user;
    }
}
