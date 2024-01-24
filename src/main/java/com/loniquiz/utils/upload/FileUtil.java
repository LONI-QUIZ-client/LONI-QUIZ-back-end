package com.loniquiz.utils.upload;

import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.text.DecimalFormat;
import java.time.LocalDateTime;
import java.util.UUID;

public class FileUtil {

    public static String uploadFile(MultipartFile file, String rootPath){
        // 원본 파일명을 중복이 없는 랜덤 이름으로 변경
        String newFileName = UUID.randomUUID() + "_" + file.getOriginalFilename();

        // 이 파일을 날짜별로 관리하기 위해 날짜별 폴더를 생성
        String newUploadPath = makeDateFormatDirectory(rootPath);

        // 파일의 풀 경로를 생성
        String fullPath = newUploadPath + "/" + newFileName;

        // 파일 업로드 수행
        try {
            file.transferTo(new File(newUploadPath, newFileName));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return fullPath.substring(rootPath.length());
    }

    private static String makeDateFormatDirectory(String rootPath) {

        // 오늘 날짜정보 추출
        LocalDateTime now = LocalDateTime.now();
        int year = now.getYear();
        int month = now.getMonthValue();
        int day = now.getDayOfMonth();

        String[] dateInfo = {year + "", len2(month), len2(day)};

        String directoryPath = rootPath;
        for (String s : dateInfo) {
            directoryPath += "/" + s;
            File f = new File(directoryPath);
            if (!f.exists()) f.mkdir();
        }

        return directoryPath;
    }
    // 한글자 월과 한글자 일자를 두글자로 변환해주는 메서드
    // ex)2024-1-3 => 2024-01-03
    private static String len2(int n) {
        return new DecimalFormat("00").format(n);
    }
}
