package com.feane.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.feane.dto.MainMenuDto;
import com.feane.dto.MenuSearchDto;
import com.feane.entity.Menu;

public interface MenuRepositoryCustom {
	Page<Menu> getAdminMenuPage(MenuSearchDto menuSearchDto,Pageable pageable);
	
	Page<MainMenuDto>getMainMenuPage(MenuSearchDto menuSearchDto,Pageable pageable);
	
}
