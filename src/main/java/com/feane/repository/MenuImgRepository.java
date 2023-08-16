package com.feane.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.feane.entity.MenuImg;

public interface MenuImgRepository extends JpaRepository<MenuImg, Long> {

List<MenuImg> findByMenuIdOrderByIdAsc(Long menuId);
	
	//select *from item_img where item_id = ? and repimg_yn =?
MenuImg findByMenuIdAndRepimgYn(Long menuId, String repimgYn);
}
