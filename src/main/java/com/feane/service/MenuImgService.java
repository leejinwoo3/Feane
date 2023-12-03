package com.feane.service;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.thymeleaf.util.StringUtils;

import com.feane.entity.MenuImg;
import com.feane.repository.MenuImgRepository;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class MenuImgService {
	
	/*
	 * @Value("${menuImgLocation}") private String menuImgLocation;
	 */
	 private String menuImgLocation = "C:/feane/menu"; 
	private final FileService fileService;

	private final MenuImgRepository menuImgRepository;

	public void savaMenuImg(MenuImg menuImg, MultipartFile menuImgFile) throws Exception {
		String oriImgName = menuImgFile.getOriginalFilename();
		String imgName = "";
		String imgUrl = "";

		if (!StringUtils.isEmpty(oriImgName)) {
			// oriImgName이 빈문자열이 아니라면 이미지 파일 업로드
			imgName = fileService.uploadFile(menuImgLocation, oriImgName, menuImgFile.getBytes());
			imgUrl = "/images/menu/" + imgName;
		}
		// 2.item_img 테이블에 저장 -> (이미지1.jsp,fafadf.jpg,"")
		menuImg.updateMenuImg(oriImgName, imgName, imgUrl);
		menuImgRepository.save(menuImg); // db에 insert

	}

	public void updateMenuImg(Long menuImgId, MultipartFile menuImgFile) throws Exception {
		if (!menuImgFile.isEmpty()) {
			MenuImg savedMenuImg = menuImgRepository.findById(menuImgId).orElseThrow(EntityNotFoundException::new);

			if (!StringUtils.isEmpty(savedMenuImg.getImgName())) {
				fileService.deleteFile(menuImgLocation + "/" + savedMenuImg.getImgName());
			}
			String oriImgName = menuImgFile.getOriginalFilename();
			String imgName = fileService.uploadFile(menuImgLocation, oriImgName, menuImgFile.getBytes());
			String imgUrl = "/images/menu/" + imgName;
			// update쿼리문 실행
			// 한번 insert를 진행하면 엔티티가 영속성 컨텍스트에 저장 되므로 그이후에는
			// 변경감지 기능이 동작하기 때문에 개발자는 update쿼리문을
			// 쓰지 않고 엔티티만 변경해주면된다.
			savedMenuImg.updateMenuImg(oriImgName, imgName, imgUrl);
		}
	}
}
