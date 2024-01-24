package com.loniquiz.game.members.service;


import com.loniquiz.game.lobby.dto.request.GameRoomRequestDTO;
import com.loniquiz.game.lobby.dto.response.GameDetailDTO;
import com.loniquiz.game.lobby.dto.response.GameLobbyDetailDTO;
import com.loniquiz.game.lobby.dto.response.GameLobbyResponseDTO;
import com.loniquiz.game.lobby.dto.response.GameRoomUserResponseDTO;
import com.loniquiz.game.lobby.entity.GameLobby;
import com.loniquiz.game.lobby.repository.GameLobbyRepository;
import com.loniquiz.game.members.dto.request.UpScoreRequestDTO;
import com.loniquiz.game.members.entity.GameMembers;
import com.loniquiz.game.members.repository.GameMembersRepository;
import com.loniquiz.users.entity.User;
import com.loniquiz.users.repository.UserRepository;
import com.loniquiz.utils.DateChangeUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional
public class MembersService {
    private final GameMembersRepository gameMembersRepository;
    private final GameLobbyRepository gameLobbyRepository;
    private final UserRepository userRepository;

    /**
     * 방 들어가기 로직인데 쫌 빡세다 ..
     * 한 회원이 들어가면 user_count가 올르고 다시 나가면 user_count가 줄어들어야 한다.
     */
    public GameDetailDTO isUserCheck(GameRoomRequestDTO dto){
        GameLobby gameLobby
                = gameLobbyRepository.findById(dto.getGno()).orElseThrow();
        User user = userRepository.findById(dto.getUserId()).orElseThrow();

        // 사용자가 현재 방에 들어갔는지 아닌지 확인을 위한 변수
        boolean flag =
                gameMembersRepository.existsByUserAndGameLobby(user, gameLobby);

        if (!flag){ // 존재하지 않다면

            // 게임 들어 올시 tb_game_room 디비에 저장
            GameMembers gameRoom = GameMembers.builder()
                    .gameLobby(gameLobby)
                    .user(user)
                    .build();

            List<GameMembers> byGameLobby = gameMembersRepository.findByGameLobby(gameLobby);

            //방에 들어오는 인원 제한을 위한 처리
            if (byGameLobby.size() >= gameLobby.getMaxCount()){
                throw new RuntimeException("방이 다 찾습니다");
            }



            gameMembersRepository.save(gameRoom);
            gameLobbyRepository.upUserCount(dto.getGno()); // 카운트 증가

            return detail(dto.getGno());
        }else{
            gameMembersRepository.deleteByUserAndGameLobby(user, gameLobby);

            gameLobbyRepository.downUserCount(dto.getGno()); // 카운트 하락띠

            return detail(dto.getGno());
        }
    }

    // 값 전달을 위한 처리
    public GameDetailDTO detail(String gno){
        GameLobby gameLobby = gameLobbyRepository.findById(gno).orElseThrow();


        List<GameMembers> byGameLobby = gameMembersRepository.findByGameLobby(gameLobby);


        List<GameRoomUserResponseDTO> collect = byGameLobby.stream()
                .map(gameRoom -> new GameRoomUserResponseDTO(gameRoom))
                .collect(Collectors.toList());


        GameLobbyDetailDTO dto = GameLobbyDetailDTO.builder()
                .lobbyId(gameLobby.getId())
                .title(gameLobby.getTitle())
                .maxRound(gameLobby.getLobbyMaxRound())
                .createDate(DateChangeUtil.postDateChang(gameLobby.getCreateDate()))
                .build();


        return GameDetailDTO.builder()
                .users(collect)
                .gameLobby(dto)
                .build();
    }


    // 게임 맞추면 점수 올라가는 처리
    public GameDetailDTO upCount(UpScoreRequestDTO dto){
        User user = userRepository.findById(dto.getId()).orElseThrow();

        GameMembers member = gameMembersRepository.findByUser(user);

        int score = member.getScore();

        member.setScore(score + 1);

        gameMembersRepository.save(member);

        return detail(member.getGameLobby().getId());

    }


}
