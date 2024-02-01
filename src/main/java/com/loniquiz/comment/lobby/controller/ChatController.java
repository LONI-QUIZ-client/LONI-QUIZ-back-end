package com.loniquiz.comment.lobby.controller;

import com.loniquiz.chatEntity.ChatResponse;
import com.loniquiz.comment.lobby.dto.response.GameChatResponseDTO;
import com.loniquiz.comment.lobby.dto.response.MemberResponseDTO;
import com.loniquiz.game.members.dto.response.GameRoomResponseDTO;
import com.loniquiz.game.members.service.MembersService;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;

import java.time.LocalDateTime;
import java.util.*;

@CrossOrigin(origins = "http://localhost:3000")
@Controller
public class ChatController {

    List<MemberResponseDTO> member = new ArrayList<>();

    private final SimpMessagingTemplate messagingTemplate;

    public ChatController(SimpMessagingTemplate messagingTemplate) {
        this.messagingTemplate = messagingTemplate;
    }


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
    public String gameSendMembers(@Payload MemberResponseDTO dto){

        // 같은 gno를 가진 사용자 수를 세기 위한 변수
        int countSameGnoUsers = 0;

        // 현재 member 리스트를 확인하여 같은 gno를 가진 사용자 수를 계산
        for (MemberResponseDTO memberResponseDTO : member) {
            if(memberResponseDTO.getGno().equals(dto.getGno())) {
                countSameGnoUsers++;
            }
            // 이미 아이디와 방 번호가 같은게 있으면 null 반환
            if(memberResponseDTO.getUserId().equals(dto.getUserId()) && memberResponseDTO.getGno().equals(dto.getGno())){
                return "false";
            }
        }

        // 같은 gno를 가진 사용자가 maxUser를 초과하는 경우 null 반환
        if(countSameGnoUsers >= dto.getMaxUser()) {
            return "false";
        }

        // 모든 조건을 통과한 경우에만 member 리스트에 추가
        member.add(dto);
        return "true";

    }


    @MessageMapping("/game/memberList")
    @SendTo("/topic/game/memberList")
    public List<MemberResponseDTO> gameSendMessage(@Payload String gno) throws InterruptedException {
        Thread.sleep(200);
        return member;
    }

//    @MessageMapping("/game/timer")
//    @SendTo("/topic/game/timer")
//    public int sendTimer() {
//        int i = startCountdown();
//
//        return i;
//    }

//    public int startCountdown() {
//        Timer timer = new Timer(true);
//
//        timer.scheduleAtFixedRate(new TimerTask() {
//            @Override
//            public void run() {
//                if (countdown > 0) {
//                    System.out.println("남은 시간: " + countdown + "초");
//                    countdown--;
//                } else {
//                    System.out.println("카운트다운 종료!");
//                    timer.cancel(); // 타이머 종료
//                }
//            }
//        }, 0, 1000); // 0초부터 시작해서 1초 간격으로 실행
//        return countdown;
//    }


    @Scheduled(fixedRate = 1000) // 1초마다 메시지 전송
    public void sendTimerUpdate() {
        // 여기에서 실제 타이머 값 계산 및 전송
        int timerValue = calculateTimerValue();
        messagingTemplate.convertAndSend("/topic/game/timer", String.valueOf(timerValue));
    }

    private int calculateTimerValue() {
        // 실제 타이머 값 계산 로직 추가
        return 0;
    }



}
