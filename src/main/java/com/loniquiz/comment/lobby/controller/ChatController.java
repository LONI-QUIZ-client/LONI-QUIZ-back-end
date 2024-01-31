package com.loniquiz.comment.lobby.controller;

import com.loniquiz.chatEntity.ChatResponse;
import com.loniquiz.comment.lobby.dto.response.GameChatResponseDTO;
import com.loniquiz.game.members.dto.response.MemberResponseDTO;
import com.loniquiz.game.members.service.MembersService;
import lombok.RequiredArgsConstructor;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
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
        System.out.println("res = " + res);
        return res;
    }

    @MessageMapping("/game/members")
    @SendTo("/topic/game/members")
    public List<MemberResponseDTO> gameSendMembers(@Payload MemberResponseDTO dto){
        for (MemberResponseDTO memberResponseDTO : member) {
            if(memberResponseDTO.getUserId().equals(dto.getUserId()) &&
                    memberResponseDTO.getGno().equals(dto.getGno())){
                return null;
            }
        }

        member.add(dto);

        return member;


    }


    @MessageMapping("/game/memberList")
    @SendTo("/topic/game/memberList")
    public List<MemberResponseDTO> gameSendMessage(@Payload String gno){
        List<MemberResponseDTO> memberList = new ArrayList<>();

        try {
            JSONObject jsonObject = (JSONObject) new JSONParser().parse(gno);

            String gnoValue = (String) jsonObject.get("gno");

            for (MemberResponseDTO memberResponseDTO : member) {
                if (memberResponseDTO.getGno().equals(gnoValue)){
                    memberList.add(memberResponseDTO);
                }
            }
            System.out.println("memberList = " + memberList);
            return memberList;
        } catch (org.json.simple.parser.ParseException e) {
            throw new RuntimeException(e);
        }

    }
}
