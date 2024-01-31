package com.ljs.shop.service;

import org.springframework.stereotype.Service;

import java.io.FileOutputStream;
import java.util.UUID;

@Service
public class FileService {

    /**
     * 파일을 업로드하고 저장된 파일명을 반환한다.
     *
     * @param uploadPath     파일을 업로드할 경로
     * @param originFileName 업로드된 파일의 원본 이름
     * @param fileData       업로드된 파일의 바이트 데이터
     * @return 저장된 파일명
     * @throws Exception 파일 업로드 및 저장 중 예외 발생 시
     */
    public String uploadFile(String uploadPath, String originFileName, byte[] fileData) throws Exception {
        // UUID를 이용하여 고유한 파일명 생성
        UUID uuid = UUID.randomUUID();

        // 파일 확장자 추출
        String extension = originFileName.substring(originFileName.lastIndexOf("."));

        // 저장될 파일명 생성
        String savedFileName = uuid + extension;

        // 파일 업로드 전체 경로 생성
        String fileUploadFullUrl = uploadPath + "/" + savedFileName;

        // 파일을 로컬에 저장
        try (FileOutputStream fos = new FileOutputStream(fileUploadFullUrl)) {
            fos.write(fileData);
        }

        // 저장된 파일명 반환
        return savedFileName;
    }
}