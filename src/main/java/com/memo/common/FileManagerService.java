package com.memo.common;

import java.io.File;

import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

@Component
public class FileManagerService {
	// 실제 업로드가 된 이미지가 저장될 서버 경로 
	// C:\Users\qkrtk\Desktop\sangbae\6_spring_project\memo\memo_workspace\images/ << 필수
	public static final String FILE_UPLOAD_PATH = "C:\\Users\\qkrtk\\Desktop\\sangbae\\6_spring_project\\memo\\memo_workspace\\images/";
	
	public String uploadFile(MultipartFile file, String loginId) {
		// 폴더(디렉토리) 생성
		// 예:aaaa_17237482334/sun.png
		String directoryName = loginId + "_" + System.currentTimeMillis();
		// "C:\\Users\\qkrtk\\Desktop\\sangbae\\6_spring_project\\memo\\memo_workspace\\images/aaaa_17237482334/";
		String filePath = FILE_UPLOAD_PATH + directoryName + "/";
		
		File directory = new File(filePath);
		if (directory.mkdir() == false) {
			return null; // 폴더 생성시 실패하면 경로를 null로 리턴(에러 아님)
		}
		
		return null;
	}
}
