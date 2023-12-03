package com.feane.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
@Configuration
public class WebMvcConfig implements WebMvcConfigurer {
	/*
	 * @Value("${uploadPath}") String uploadPath;
	 */
	 String uploadPath = "file:///C:/feane/";  // 업로드할 경로

	// 웹브라우저에서 URL이 /images로 시작하는 경우 uploadPath에 설정한 폴더를 기준으로 파일을 읽어온다.
	@Override
	public void addResourceHandlers(ResourceHandlerRegistry registry) {
		// TODO Auto-generated method stub
		registry.addResourceHandler("/images/**").addResourceLocations(uploadPath);
	}
}
