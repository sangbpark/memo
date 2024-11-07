package com.memo.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.memo.common.FileManagerService;

@Configuration // 설정 관련 spring bean
public class WebMvcConfig implements WebMvcConfigurer {
	
	// 예언된 이미지 path와 서버에 업로드 된 실제 이미지와 매핑
	@Override
	public void addResourceHandlers(ResourceHandlerRegistry registry) {
		registry
		.addResourceHandler("/images/**") // path http://localhost/images/aaaa_1730889229301/Button.png
		.addResourceLocations("file:///" + FileManagerService.FILE_UPLOAD_PATH); // 윈도우 /// 맥 리눅스 //
	}
	
}
