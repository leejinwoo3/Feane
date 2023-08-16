package com.feane.service;

import java.io.File;
import java.io.FileOutputStream;
import java.util.UUID;

import org.springframework.stereotype.Service;

import lombok.extern.java.Log;

@Service
@Log
public class FileService {
	public String uploadFile(String uploadPath, String originalFileName, byte[] fileDate) throws Exception {
		UUID uuid = UUID.randomUUID();
		
		String extension = originalFileName.substring(originalFileName.lastIndexOf("."));
		String savedFileName = uuid.toString() + extension; //파일이름 생성 -> ERSFH4FDG0454.jpg
		
		//C:/shop/item/ERSFH4FDG0454.jpg 식으로 생성
		String filleUploadFullUrl = uploadPath + "/" + savedFileName;
		
		//파일 업로드
		FileOutputStream fos = new FileOutputStream(filleUploadFullUrl);
		fos.write(fileDate);
		fos.close();
		return savedFileName;
	}
	public void deleteFile(String filePath) throws Exception{
		File deleteFile = new File(filePath);
		
		if(deleteFile.exists()) {
			deleteFile.delete();
			log.info("파일을 삭제하였습니다.");
		}else {
			log.info("파일이 존재하지 않습니다.");
		}
	}
}
