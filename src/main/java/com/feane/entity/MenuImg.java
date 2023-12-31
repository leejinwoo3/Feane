package com.feane.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "menu_img")
@Getter
@Setter
public class MenuImg extends BaseEntity {
	@Id
	@Column(name = "menu_img_id")
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	private String imgName; // 바뀐 이미지 파일명(중복장지)
	private String oriImgName; // 원본 이미지 파일명
	private String imgUrl; // 이미지 조회 경로

	private String repimgYn;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "menu_id")
	private Menu menu;

	public void updateMenuImg(String oriImgName, String imgName, String imgUrl) {
		this.oriImgName = oriImgName;
		this.imgName = imgName;
		this.imgUrl = imgUrl;
	}
}
