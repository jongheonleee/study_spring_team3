package com.example.shop2.service.item;

import java.io.File;
import java.io.FileOutputStream;
import java.util.UUID;
import lombok.extern.java.Log;
import org.springframework.stereotype.Service;

// 파일 처리
@Service
@Log
public class FileService {

    // 1. 파일 등록(업로드 경로, 파일 이름, 파일 데이터)
    public String uploadFile(String uploadPath, String originalName, byte[] fileData) throws Exception{
        // 새로운 uuid 생성
        UUID uuid = UUID.randomUUID();

        // 확장자 추출
        String extension = originalName.substring(originalName.lastIndexOf('.'));

        // 새로운 파일 이름 생성
        String savedFileName = uuid + extension;

        // 파일 업로드 경로 생성
        String fileUploadUrlFullPath = uploadPath + "/" + savedFileName;

        // 파일 출력 스트림 생성, 파일 업로드 경로 지정
        FileOutputStream fos = new FileOutputStream(fileUploadUrlFullPath);

        // 파일 출력 전송
        fos.write(fileData);

        // 자원 반환
        fos.close();

        // 등록된 파일 이름 반환
        return savedFileName;
    }


    // 2. 파일 삭제
    public void deleteFile(String filePath) throws Exception {
        // 삭제할 파일 객체 생성, 삭제할 파일 경로 지정
        File file = new File(filePath);

        // 해당 파일이 존재하면 삭제, 그렇지 않으면 삭제 안함
        if (file.exists()) {
            file.delete();
            log.info("파일 삭제 성공");
        } else {
            log.info("파일이 존재하지 않습니다.");
        }

    }
}
