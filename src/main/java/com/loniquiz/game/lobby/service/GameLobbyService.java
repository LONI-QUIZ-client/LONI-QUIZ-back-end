package com.loniquiz.game.lobby.service;

import com.loniquiz.game.lobby.dto.request.GameLobbyCreateDTO;
import com.loniquiz.game.lobby.dto.request.GameRoomRequestDTO;
import com.loniquiz.game.lobby.dto.response.*;
import com.loniquiz.game.lobby.dto.response.GameLobbyListResponseDTO;
import com.loniquiz.game.lobby.dto.response.GameLobbyResponseDTO;

import com.loniquiz.game.lobby.entity.GameLobby;
import com.loniquiz.game.lobby.dto.request.PageRequestDTO;
import com.loniquiz.game.lobby.repository.GameLobbyRepository;

import com.loniquiz.game.members.entity.GameMembers;
import com.loniquiz.game.members.repository.GameMembersRepository;
import com.loniquiz.users.entity.User;
import com.loniquiz.users.repository.UserRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional
public class GameLobbyService {
    private final GameLobbyRepository gameLobbyRepository;
    private final UserRepository userRepository;
    private final GameMembersRepository gameMembersRepository;



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

        if (dto == null){ // 값 전달이 잘 안 될을때
            log.warn("방 생성 중 값이 null이다 dto : {}", dto);
            throw new RuntimeException();
        }

        User user = userRepository.findById(dto.getUserId()).orElseThrow();

        boolean flag = gameLobbyRepository.existsByUser(user);// 유저는 방 한번만 만들 수 있게 설정

        if (flag){ // 방을 만든 사람이 방을 또 만들라고 하면 여기에 걸림
            throw new RuntimeException("방을 이미 한번 만드셨어요");
        }


        // 비번 방 값 처리
        String uuid = secretUUid(dto.isSecret());

        GameLobby save = gameLobbyRepository.save(dto.toEntity(user, uuid));

        // 방 만든 사람은 자동 멤버로 참여
        autoSaveMember(user, save);


        return gameAllList(pageRequest);
    }


    // 방 만든 사람은 자동 멤버로 참여 처리
    public void autoSaveMember(User user, GameLobby gameLobby){
        GameMembers gameMember = GameMembers.builder()
                .gameLobby(gameLobby)
                .user(user)
                .build();

        gameMembersRepository.save(gameMember);
    }
    
    // 비밀 방일 경우 랜덤 키값 반환
    public String secretUUid(boolean secret){
        String uuid = null;
        if (secret){
            uuid = String.valueOf(UUID.randomUUID());
        }
        
        return uuid;
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
    public void deleteLobby(String gno){
        try{
            gameLobbyRepository.deleteById(gno);
        }catch (Exception e){
            log.error("아이디 또는 방 아이디가 맞지 않아 삭제 불가한다 : {}", gno);
            throw new RuntimeException();
        }
    }
}
