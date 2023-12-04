package com.feane.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.feane.dto.MainMenuDto;
import com.feane.dto.MenuFormDto;
import com.feane.dto.MenuImgDto;
import com.feane.dto.MenuSearchDto;
import com.feane.entity.Category;
import com.feane.entity.Menu;
import com.feane.entity.MenuImg;
import com.feane.repository.CategoryRepository;
import com.feane.repository.MenuImgRepository;
import com.feane.repository.MenuRepository;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class MenuService {
	private final MenuRepository menuRepository;
	private final MenuImgService menuImgService;
	private final MenuImgRepository menuImgRepository;
	private final CategoryRepository categoryRepository;

// 메뉴 테이블 메뉴등록
	public Long saveMenu(MenuFormDto menuFormDto, List<MultipartFile> menuImgFileList) throws Exception {
		//1.카테고리 등록
		Category category = categoryRepository.findById(menuFormDto.getCategoryId())
				.orElseThrow(EntityNotFoundException::new);
		Menu menu = menuFormDto.createMenu(category);//dto->entity
		menuRepository.save(menu); // insert(저장)
		
		for (int i = 0; i < menuImgFileList.size(); i++) {
			// 부모테이블에 해당하는 entity를 먼저 넣어줘야 한다.
			MenuImg menuImg = new MenuImg();
			menuImg.setMenu(menu);
			// 첫번째 이미지 일때 대표상품 이미지 지정
			if (i == 0) {
				menuImg.setRepimgYn("Y");
			} else {
				menuImg.setRepimgYn("N");
			}
			System.out.println("a");

			// menuImgService.savaMenuImg(menuImg, menuImgFileList.get(i));
			System.out.println("b");
		}

		return menu.getId(); // 등록된 상품 id를 리턴

	}

	// 메뉴 가져오기
	@Transactional(readOnly = true)
	public MenuFormDto getMenuDtl(Long menuId) {
		List<MenuImg> menuImgList = menuImgRepository.findByMenuIdOrderByIdAsc(menuId);
		List<MenuImgDto> menuImgDtoList = new ArrayList<>();
		for (MenuImg menuImg : menuImgList) {
			MenuImgDto menuImgDto = MenuImgDto.of(menuImg);
			menuImgDtoList.add(menuImgDto);
		}
		Menu menu = menuRepository.findById(menuId).orElseThrow(EntityNotFoundException::new);

		MenuFormDto menuFormDto = MenuFormDto.of(menu);

		menuFormDto.setMenuImgDtoList(menuImgDtoList);

		return menuFormDto;

	}

	public Long updateMenu(MenuFormDto menuFormDto, List<MultipartFile> menuImgFileList) throws Exception {
		Menu menu = menuRepository.findById(menuFormDto.getId()).orElseThrow(EntityNotFoundException::new);
		menu.updateMenu(menuFormDto);
		List<Long> menuImgIds = menuFormDto.getMenuImgIds();
		for (int i = 0; i < menuImgFileList.size(); i++) {
			menuImgService.updateMenuImg(menuImgIds.get(i), menuImgFileList.get(i));
		}
		return menu.getId();
	}

//메뉴 관리
	@Transactional(readOnly = true)
	public Page<Menu> getAdminMenuPage(MenuSearchDto menuSearchDto, Pageable pageable) {
		Page<Menu> menuPage = menuRepository.getAdminMenuPage(menuSearchDto, pageable);

		return menuPage;
	}

	// 카테고리 리스트
	public List<Category> getCategoryList() {
		List<Category> categoryList = categoryRepository.findAll();

		return categoryList;
	}

	// 서버 최초 실행 시 카테고리 등록
	public Category saveCategory(Category category) {
		Category saveCategory = categoryRepository.save(category);

		return saveCategory;
	}

	@Transactional(readOnly = true)
	public Page<MainMenuDto> getMainMenuPage(MenuSearchDto menuSearchDto, Pageable pageable) {
		Page<MainMenuDto> mainMenuPage = menuRepository.getMainMenuPage(menuSearchDto, pageable);
		return mainMenuPage;
	}

	public void deleteMenu(Long menuId) {
		Menu menu = menuRepository.findById(menuId).orElseThrow(EntityNotFoundException::new);

		menuRepository.delete(menu);
	}

	

}
