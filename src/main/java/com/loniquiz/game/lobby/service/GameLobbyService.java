package com.loniquiz.game.lobby.service;

import com.loniquiz.game.lobby.dto.request.GameLobbyCreateDTO;
import com.loniquiz.game.lobby.dto.request.GameRoomRequestDTO;
import com.loniquiz.game.lobby.dto.response.*;
import com.loniquiz.game.lobby.dto.response.GameLobbyListResponseDTO;
import com.loniquiz.game.lobby.dto.response.GameLobbyResponseDTO;

import com.loniquiz.game.lobby.entity.GameLobby;
import com.loniquiz.game.lobby.dto.request.PageRequestDTO;
import com.loniquiz.game.lobby.repository.GameLobbyRepository;

import com.loniquiz.game.room.entity.GameRoom;
import com.loniquiz.game.room.repository.GameRoomRepository;
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


}
