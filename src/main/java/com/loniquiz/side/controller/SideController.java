package com.loniquiz.side.controller;

import com.loniquiz.side.service.SideService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.File;
import java.io.IOException;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/side")
//@CrossOrigin(origins = {"http://localhost:3000"}) // api 접근을 허용할 클라이언트 ip,3000 포트 허용
public class SideController {

    private final SideService sideService;

    // 파일의 확장자를 추출하여 미디어타입을 알려주는 메서드
    private MediaType extractFileExtension(String filePath) {

        // D:/abc/def/image/sdjlfjslfajdfjlsajkl_cat.png
        String ext = filePath.substring(filePath.lastIndexOf(".") + 1);

        switch (ext.toUpperCase()) {
            case "JPEG": case "JPG":
                return MediaType.IMAGE_JPEG;
            case "PNG":
                return MediaType.IMAGE_PNG;
            case "GIF":
                return MediaType.IMAGE_GIF;
            default:
                return null;
        }
    }


    // 파일 클라이언트에 전송하기
    @GetMapping("/side-menu")
    private ResponseEntity<?> loadSideMenuImage(){
        try {
            String profilePath = sideService.getMenuImgPath();

            File profileFile = new File(profilePath);

//            if (!profileFile.exists()) return ResponseEntity.notFound().build();

            // 서버에 저장된 파일을 직렬화하여 바이트배열로 만들어서 가져옴
            byte[] fileData = FileCopyUtils.copyToByteArray(profileFile);

            // 3. 응답헤더에 이 데이터의 타입이 무엇인지를 설정해야 함.
            HttpHeaders headers = new HttpHeaders();
            MediaType mediaType = extractFileExtension(profilePath);
            if (mediaType == null) {
                return ResponseEntity.internalServerError()
                        .body("발견된 파일은 이미지가 아닙니다.");
            }
            headers.setContentType(mediaType);

            log.info("여기로들어옴?");
            return ResponseEntity.ok()
                    .headers(headers)
                    .body(fileData);

        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

}
