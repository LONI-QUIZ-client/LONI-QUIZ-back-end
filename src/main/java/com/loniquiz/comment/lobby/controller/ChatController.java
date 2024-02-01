package com.loniquiz.comment.lobby.controller;

import com.loniquiz.chatEntity.ChatResponse;
import com.loniquiz.comment.lobby.dto.response.GameChatResponseDTO;
import com.loniquiz.comment.lobby.dto.response.MemberResponseDTO;
import com.loniquiz.comment.lobby.dto.response.RoomIsFullDTO;
import com.loniquiz.game.members.service.MembersService;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class ChatController {

    private final MembersService membersService;
    List<MemberResponseDTO> member = new ArrayList<>();




    @MessageMapping("/chat")
    @SendTo("/topic/messages")
    public ChatResponse sendMessage(@Payload ChatResponse res) {
        res.setTimestamp(new Date());
        return res;
    }

    @MessageMapping("/game/chat")
    @SendTo("/topic/game/messages")
    public GameChatResponseDTO gameSendMessage(@Payload GameChatResponseDTO res){
        res.setLocalDateTime(LocalDateTime.now());
        return res;
    }

    @MessageMapping("/game/members")
    @SendTo("/topic/game/members")
    public RoomIsFullDTO gameSendMembers(@Payload MemberResponseDTO dto){
        RoomIsFullDTO roomIsFullDTO = new RoomIsFullDTO();

        // 같은 gno를 가진 사용자 수를 세기 위한 변수
        int countSameGnoUsers = 0;

        // 현재 member 리스트를 확인하여 같은 gno를 가진 사용자 수를 계산
        for (MemberResponseDTO memberResponseDTO : member) {
            if(memberResponseDTO.getGno().equals(dto.getGno())) {
                countSameGnoUsers++;
            }
            // 이미 아이디와 방 번호가 같은게 있으면 null 반환
            if(memberResponseDTO.getUserId().equals(dto.getUserId()) && memberResponseDTO.getGno().equals(dto.getGno())){
                roomIsFullDTO.setIsFull("false");
                return roomIsFullDTO;
            }
        }

        // 같은 gno를 가진 사용자가 maxUser를 초과하는 경우 null 반환
        if(countSameGnoUsers >= dto.getMaxUser()) {
            roomIsFullDTO.setIsFull("true");
            return roomIsFullDTO;
        }

        // 모든 조건을 통과한 경우에만 member 리스트에 추가
        member.add(dto);
        roomIsFullDTO.setIsFull("false");
        return roomIsFullDTO;

    }


    @MessageMapping("/game/memberList")
    @SendTo("/topic/game/memberList")
    public List<MemberResponseDTO> gameSendMessage(@Payload String gno) throws InterruptedException {
        Thread.sleep(200);
        return member;

    }
}
