package com.loniquiz.comment.lobby.controller;

import com.loniquiz.chatEntity.ChatResponse;
import com.loniquiz.comment.lobby.dto.request.ImageRequestDTO;
import com.loniquiz.comment.lobby.dto.request.TimerRequestDTO;
import com.loniquiz.comment.lobby.dto.response.GameChatResponseDTO;
import com.loniquiz.comment.lobby.dto.response.MemberResponseDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;

import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

@CrossOrigin(origins = "http://localhost:3000")
@Controller
public class ChatController {

    List<MemberResponseDTO> member = new ArrayList<>();

    @Autowired
    private SimpMessagingTemplate messagingTemplate;
    List<MemberResponseDTO> superMember = new ArrayList<>();


    @MessageMapping("/chat")
    @SendTo("/topic/messages")
    public ChatResponse sendMessage(@Payload ChatResponse res) {
        res.setTimestamp(new Date());
        return res;
    }

    @MessageMapping("/game/chat")
    @SendTo("/topic/game/messages")
    public GameChatResponseDTO gameSendMessage(@Payload GameChatResponseDTO res) {
        res.setLocalDateTime(LocalDateTime.now());
        return res;
    }

    @MessageMapping("/game/members")
    @SendTo("/topic/game/members")
    public String gameSendMembers(@Payload MemberResponseDTO dto) {


        // 같은 gno를 가진 사용자 수를 세기 위한 변수
        int countSameGnoUsers = 0;

        // 현재 member 리스트를 확인하여 같은 gno를 가진 사용자 수를 계산
        for (MemberResponseDTO memberResponseDTO : member) {
            if (memberResponseDTO.getGno().equals(dto.getGno())) {
                countSameGnoUsers++;
            }
            // 이미 아이디와 방 번호가 같은게 있으면 null 반환
            if (memberResponseDTO.getUserId().equals(dto.getUserId()) && memberResponseDTO.getGno().equals(dto.getGno())) {
                return "false";
            }
        }

        // 같은 gno를 가진 사용자가 maxUser를 초과하는 경우 null 반환
        if (countSameGnoUsers >= dto.getMaxUser()) {
            return "false";
        }

        boolean existsInSuperMember = superMember.stream().anyMatch(memberDTO -> memberDTO.getGno().equals(dto.getGno()));
        if (!existsInSuperMember) {
            superMember.add(dto);
            System.out.println("superMember = " + superMember);
        }

        // 모든 조건을 통과한 경우에만 member 리스트에 추가
        member.add(dto);
        return "false";


    }


    @MessageMapping("/game/memberList")
    @SendTo("/topic/game/memberList")
    public List<MemberResponseDTO> gameSendMessage(@Payload String gno) throws InterruptedException {
        Thread.sleep(200);
        return member;
    }

    @MessageMapping("/game/timer")
    @SendTo("/topic/game/timer")
    public void sendTimer() {
        ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
        AtomicInteger countdown = new AtomicInteger(10); // 초기 카운트다운 값

        scheduler.scheduleAtFixedRate(() -> {
            int currentCountdown = countdown.getAndDecrement();
            if (currentCountdown > 0) {
                System.out.println("남은 시간: " + currentCountdown + "초");
                messagingTemplate.convertAndSend("/topic/game/timer", currentCountdown);
            } else {
                System.out.println("카운트다운 종료!");
                scheduler.shutdown(); // 카운트다운이 끝나면 스케줄러 종료
            }
        }, 0, 1, TimeUnit.SECONDS); // 1초 간격
    }

    @MessageMapping("/game/image")
    @SendTo("/topic/game/image")
    public String imageSelect(@Payload ImageRequestDTO image){
        System.out.println("image = " + image.getImage());
        return image.getImage();
    }

}
