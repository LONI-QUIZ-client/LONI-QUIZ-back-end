package com.loniquiz.side.service;

import com.loniquiz.side.entity.Side;
import com.loniquiz.side.repository.SideRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Random;

@Service
@Slf4j
@RequiredArgsConstructor
public class SideService {

    private final SideRepository sideRepository;

    @Value("${sideMenu.path}")
    private String sideMenuPath;

    // ========== side menu image ==========

    // 이미지 랜덤으로 뽑기
    public int randomImageId(){

        int count = sideRepository.countSideMenuImagesByAll();

        Random random = new Random();
        int ran = random.nextInt(count)+1;

        log.info("ran: {}", ran);

        return ran;
    }

    // 랜덤으로 뽑힌 이미지 경로 조회
    public String getMenuImgPath(){
        int id = randomImageId();

        Side sideMenuImage = sideRepository.findByImgId(id).orElseThrow();
        String fileName = sideMenuImage.getImgPath();

        return sideMenuPath + "/" + fileName;
    }

}
