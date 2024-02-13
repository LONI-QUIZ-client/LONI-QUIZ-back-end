package com.loniquiz.comment.lobby.controller;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.loniquiz.chatEntity.ChatResponse;
import com.loniquiz.comment.lobby.dto.request.ImageRequestDTO;
import com.loniquiz.comment.lobby.dto.request.TimerRequestDTO;
import com.loniquiz.comment.lobby.dto.response.ForCheckResponseDTO;
import com.loniquiz.comment.lobby.dto.response.GameChatResponseDTO;
import com.loniquiz.comment.lobby.dto.response.MemberResponseDTO;
import com.loniquiz.comment.lobby.dto.response.UserPointUpResponseDTO;
import com.loniquiz.comment.lobby.entity.GameMemberList;
import com.loniquiz.comment.lobby.entity.Member;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
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

    List<GameMemberList> gameMembers = new ArrayList<>();

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

    @MessageMapping("/game/userPointUp")
    @SendTo("/topic/game/userPointUp")
    public void userPointUp(@Payload UserPointUpResponseDTO res) {
        System.out.println("여기까지 오기는 하냐아ㅏㅏㅏㅏㅏㅏㅏㅏ");
        // gameMembers 리스트에서 gno가 일치하는 GameMemberList 찾기
        Optional<GameMemberList> optionalGameMemberList = gameMembers.stream()
                .filter(gameMemberList -> gameMemberList.getGno().equals(res.getGno()))
                .findFirst();

        // gno가 일치하는 GameMemberList가 존재하는 경우
        if (optionalGameMemberList.isPresent()) {
            GameMemberList gameMemberList = optionalGameMemberList.get();

            // gameMemberList에서 userId가 일치하는 Member 찾기
            Optional<Member> optionalMember = gameMemberList.getMembers().stream()
                    .filter(member -> member.getUserId().equals(res.getUserId()))
                    .findFirst();

            // userId가 일치하는 Member가 존재하는 경우
            if (optionalMember.isPresent()) {
                Member member = optionalMember.get();

                // Member의 포인트를 1 증가시키기
                member.setPoint(member.getPoint() + 1);
            }
        }
        System.out.println("gameMembers = " + gameMembers);
    }

    @MessageMapping("/game/answerKey")
    @SendTo("/topic/game/answerKey")
    public ForCheckResponseDTO forCheck(@Payload ForCheckResponseDTO res) {
        if (res != null) {
            System.out.println("res = " + res);
        }
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
    @MessageMapping("/game/exitRoom")
    @SendTo("/topic/game/exitRoom")
    public List<List<?>> exitRoom(@Payload UserPointUpResponseDTO res) {
        String gno = res.getGno();
        String userId = res.getUserId();

        removeMemberFromSuperMemberList(superMember, gno, userId);
        removeMemberFromGameMemberList(gameMembers, gno, userId);
        removeMemberFromMemberList(member, gno, userId);

        List<List<?>> result = new ArrayList<>();
        result.add(superMember);
        result.add(gameMembers);
        result.add(member);
        return result;
    }

    @MessageMapping("/game/getSuperUser")
    @SendTo("/topic/game/getSuperUser")
    public List<MemberResponseDTO> getSuperUser(@Payload String gno) throws InterruptedException {
        Thread.sleep(200);
        System.out.println("superMember = " + superMember);
        return superMember;
    }



    @MessageMapping("/game/timer/{roomId}")
    @SendTo("/topic/game/timer/{roomId}")
    public TimerRequestDTO sendTimer(@Payload TimerRequestDTO dto, @DestinationVariable String roomId) {
        ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
        AtomicInteger countdown = new AtomicInteger(10); // 초기 카운트다운 값

        scheduler.scheduleAtFixedRate(() -> {
            int currentCountdown = countdown.getAndDecrement();
            if (currentCountdown >= 0) {
                System.out.println("Room ID: " + roomId + ", Remaining Time: " + currentCountdown + " seconds");
                dto.setTime(currentCountdown);
                messagingTemplate.convertAndSend("/topic/game/timer/" + roomId, dto);
            } else {
                System.out.println("Room ID: " + roomId + ", Countdown Finished!");
                scheduler.shutdown(); // 카운트다운이 끝나면 스케줄러 종료
            }
        }, 0, 1, TimeUnit.SECONDS); // 1초 간격

        TimerRequestDTO response = new TimerRequestDTO();
        response.setGno(roomId);
        return response;
    }

    @MessageMapping("/game/start")
    @SendTo("/topic/game/start")
    public List<GameMemberList> gameStart(@Payload String gno) {
        System.out.println(gno);
        try {
            // ObjectMapper 객체 생성
            ObjectMapper objectMapper = new ObjectMapper();

            // JSON 문자열 파싱
            JsonNode jsonNode = objectMapper.readTree(gno);

            // "gno" 필드의 값 추출
            String gnoValue = jsonNode.get("gno").asText();
            System.out.println("gnoValue = " + gnoValue);

            // 동일한 gno가 있는지 확인
            for (GameMemberList existingGameMemberList : gameMembers) {
                if (existingGameMemberList.getGno().equals(gnoValue)) {
                    // 동일한 gno가 이미 존재하므로 null 반환
                    return null;
                }
            }

            GameMemberList gameMemberList = new GameMemberList();
            gameMemberList.setGno(gnoValue);

            List<Member> memberList = new ArrayList<>();
            System.out.println("member = " + member);
            boolean isFirstMember = true;
            // 해당 gno와 동일한 gno를 가진 멤버들의 Member 객체를 생성하여 gameMemberList에 추가
            for (MemberResponseDTO memberDTO : member) {
                if (memberDTO.getGno().equals(gnoValue)) {
                    Member mem = new Member();
                    mem.setUserId(memberDTO.getUserId());
                    mem.setName(memberDTO.getUsername());

                    // 첫 번째 멤버인 경우 state를 true로 설정하고, 그 외에는 false로 설정
                    mem.setTurn(isFirstMember);
                    isFirstMember = false; // 첫 번째 멤버가 아니므로 false로 설정

                    memberList.add(mem);
                }
            }
            gameMemberList.setMembers(memberList);
            // gameMembers 리스트에 추가
            gameMembers.add(gameMemberList);
            System.out.println("gameMembers = " + gameMembers);
            return gameMembers;

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @MessageMapping("/game/next")
    @SendTo("/topic/game/next")
    public void nextTurn(@Payload String gno) {
        try {
            // ObjectMapper 객체 생성
            ObjectMapper objectMapper = new ObjectMapper();

            // JSON 문자열 파싱
            JsonNode jsonNode = objectMapper.readTree(gno);

            // "gno" 필드의 값 추출
            String gnoValue = jsonNode.get("gno").asText();
            // 주어진 gno에 해당하는 GameMemberList 찾기

            for (GameMemberList gameMemberList : gameMembers) {
                if (gameMemberList.getGno().equals(gnoValue)) {
                    GameMemberList targetGameMemberList = new GameMemberList();
                    targetGameMemberList = gameMemberList;
                    System.out.println("targetGameMemberList = " + targetGameMemberList);
                    if (targetGameMemberList != null) {
                        List<Member> members = targetGameMemberList.getMembers();
                        for (int i = 0; i < members.size(); i++) {
                            Member member = members.get(i);
                            if (member.isTurn()) { // 현재 멤버의 상태가 true인 경우
                                member.setTurn(false); // 상태를 false로 변경
                                int nextIndex = (i + 1) % members.size(); // 다음 멤버의 인덱스 계산
                                Member nextMember = members.get(nextIndex);
                                nextMember.setTurn(true); // 다음 멤버의 상태를 true로 변경
                                break;
                            }
                        }
                    }
                    System.out.println("gameMembers = " + gameMembers);

                    // 변경된 상태를 모든 클라이언트에게 전송
                    messagingTemplate.convertAndSend("/topic/game/next", gameMembers);
                    break;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @MessageMapping("/game/image")
    @SendTo("/topic/game/image")
    public ImageRequestDTO imageSelect(@Payload ImageRequestDTO image) {
        System.out.println("image = " + image.getImage());
        return image;
    }

    private void removeMemberFromSuperMemberList(List<MemberResponseDTO> superMember, String gno, String userId) {
        Iterator<MemberResponseDTO> iterator = superMember.iterator();
        while (iterator.hasNext()) {
            MemberResponseDTO member = iterator.next();
            if (member.getGno().equals(gno) && member.getUserId().equals(userId)) {
                iterator.remove();
            }
        }
    }

    private void removeMemberFromGameMemberList(List<GameMemberList> gameMembers, String gno, String userId) {
        for (GameMemberList gameMemberList : gameMembers) {
            if (gameMemberList.getGno().equals(gno)) {
                Iterator<Member> iterator = gameMemberList.getMembers().iterator();
                while (iterator.hasNext()) {
                    Member member = iterator.next();
                    if (member.getUserId().equals(userId)) {
                        iterator.remove();
                    }
                }
                break;
            }
        }
    }

    private void removeMemberFromMemberList(List<MemberResponseDTO> memberList, String gno, String userId) {
        Iterator<MemberResponseDTO> iterator = memberList.iterator();
        while (iterator.hasNext()) {
            MemberResponseDTO member = iterator.next();
            if (member.getGno().equals(gno) && member.getUserId().equals(userId)) {
                iterator.remove();
            }
        }
    }
}
