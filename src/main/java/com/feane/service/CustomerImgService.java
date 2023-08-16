package com.feane.service;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.thymeleaf.util.StringUtils;

import com.feane.entity.CustomerImg;
import com.feane.repository.CustomerImgRepository;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class CustomerImgService {
	private String customerImgLocation = "C:/feane/customer";
	private final FileService fileService;

	private final CustomerImgRepository customerImgRepository;

	public void savaCustomerImg(CustomerImg customerImg, MultipartFile customerImgFile) throws Exception {
		String oriImgName = customerImgFile.getOriginalFilename();
		String imgName = "";
		String imgUrl = "";
		if (!StringUtils.isEmpty(oriImgName)) {
			// oriImgName이 빈문자열이 아니라면 이미지 파일 업로드
			imgName = fileService.uploadFile(customerImgLocation, oriImgName, customerImgFile.getBytes());
			imgUrl = "/images/customer/" + imgName;
		}
		// 2.item_img 테이블에 저장 -> (이미지1.jsp,fafadf.jpg,"")
		customerImg.updateCustomerImg(oriImgName, imgName, imgUrl);
		customerImgRepository.save(customerImg); // db에 insert

	}
	public void updateCustomerImg(Long customerImgId, MultipartFile customerImgFile) throws Exception {
		if (!customerImgFile.isEmpty()) {
			CustomerImg savedcustomerImg = customerImgRepository.findById(customerImgId).orElseThrow(EntityNotFoundException::new);

			if (!StringUtils.isEmpty(savedcustomerImg.getImgName())) {
				fileService.deleteFile(customerImgLocation + "/" + savedcustomerImg.getImgName());
			}
			String oriImgName = customerImgFile.getOriginalFilename();
			String imgName = fileService.uploadFile(customerImgLocation, oriImgName, customerImgFile.getBytes());
			String imgUrl = "/images/customer/" + imgName;
			// update쿼리문 실행
			// 한번 insert를 진행하면 엔티티가 영속성 컨텍스트에 저장 되므로 그이후에는
			// 변경감지 기능이 동작하기 때문에 개발자는 update쿼리문을
			// 쓰지 않고 엔티티만 변경해주면된다.
			savedcustomerImg.updateCustomerImg(oriImgName, imgName, imgUrl);
		}
	}
}
