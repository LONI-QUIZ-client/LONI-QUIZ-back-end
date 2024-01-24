package com.loniquiz.game.lobby.service;

import com.loniquiz.comment.lobby.dto.request.GameLobbyCreateDTO;
import com.loniquiz.game.lobby.dto.request.GameRoomRequestDTO;
import com.loniquiz.game.lobby.dto.response.*;
import com.loniquiz.game.lobby.dto.response.GameLobbyListResponseDTO;
import com.loniquiz.game.lobby.dto.response.GameLobbyResponseDTO;

import com.loniquiz.game.lobby.entity.GameLobby;
import com.loniquiz.game.lobby.dto.request.PageRequestDTO;
import com.loniquiz.game.lobby.repository.GameLobbyRepository;
import com.loniquiz.comment.lobby.repository.LobbyChatRepository;
import com.loniquiz.game.members.entity.GameMembers;
import com.loniquiz.game.members.repository.GameMembersRepository;
import com.loniquiz.users.entity.User;
import com.loniquiz.users.repository.UserRepository;
import com.loniquiz.utils.DateChangeUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional
public class GameLobbyService {
    private final GameLobbyRepository gameLobbyRepository;
    private final UserRepository userRepository;
    private final GameMembersRepository gameRoomRepository;
    private final LobbyChatRepository lobbyChatRepository;


    // 페이징 처리한 게임 방 보여주기
    public GameLobbyListResponseDTO gameAllList(PageRequestDTO dto){

        PageRequest pageInfo = PageRequest.of(
                dto.getPage() - 1, dto.getAmount()
        );

        //페이징 처리
        Page<GameLobby> gamePageList = gameLobbyRepository.findAll(pageInfo);

        //리시트로 변환
        List<GameLobby> gameList = gamePageList.toList();


        //dto로 저장
        List<GameLobbyResponseDTO> lobbyResponseDTO = gameList.stream()
                .map(gameLobby -> new GameLobbyResponseDTO(gameLobby))
                .collect(Collectors.toList());

        return GameLobbyListResponseDTO.builder()
                .pageMaxCount(gamePageList.getTotalPages())
                .dto(lobbyResponseDTO)
                .build();
    }


    /**
     * 방 생성 처리입니다. 승한이형
     * @param dto - 클라이언트가 준 값
     * @param pageRequest - 이거는 방을 다시 조회해야하는데 조회할때
     *                    필요한 매개변수여서 억지로 끼워넣었습니다.
     *                    같이 해결해주세요 승한이형
     * @return - 저장하고 다시 조회된 방
     */
    public GameLobbyListResponseDTO createGameLobby(GameLobbyCreateDTO dto,
                                                    PageRequestDTO pageRequest){

        if (dto == null){
            log.warn("방 생성 중 값이 null이다 dto : {}", dto);
            throw new RuntimeException();
        }

        User user = userRepository.findById(dto.getUserId()).orElseThrow();

        gameLobbyRepository.save(dto.toEntity(user));

        return gameAllList(pageRequest);
    }


    // 방 삭제
    public GameLobbyListResponseDTO deleteLobby(String gno, String userId, PageRequestDTO pageRequest){
        try{
            gameLobbyRepository.deleteWithIdAndUser(gno, userId);
            return gameAllList(pageRequest);
        }catch (Exception e){
            log.error("아이디 또는 방 아이디가 맞지 않아 삭제 불가한다 : {}", gno);
            throw new RuntimeException();
        }
    }

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
                gameRoomRepository.existsByUserAndGameLobby(user, gameLobby);

        if (!flag){ // 존재하지 않다면

            // 게임 들어 올시 tb_game_room 디비에 저장
            GameMembers gameRoom = GameMembers.builder()
                    .gameLobby(gameLobby)
                    .user(user)
                    .build();

            gameRoomRepository.save(gameRoom);
            gameLobbyRepository.upUserCount(dto.getGno()); // 카운트 증가

            return detail(dto.getGno());
        }else{
            gameRoomRepository.deleteByUserAndGameLobby(user, gameLobby);

            gameLobbyRepository.downUserCount(dto.getGno()); // 카운트 하락띠

            return detail(dto.getGno());
        }
    }

    // 값 전달을 위한 처리
    public GameDetailDTO detail(String gno){
        GameLobby gameLobby = gameLobbyRepository.findById(gno).orElseThrow();


        List<GameMembers> byGameLobby = gameRoomRepository.findByGameLobby(gameLobby);


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

}
