package com.feane.repository;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.thymeleaf.util.StringUtils;

import com.feane.constant.MenuSellStatus;
import com.feane.dto.MainMenuDto;
import com.feane.dto.MenuSearchDto;
import com.feane.dto.QMainMenuDto;
import com.feane.entity.Menu;
import com.feane.entity.QMenu;
import com.feane.entity.QMenuImg;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.Wildcard;
import com.querydsl.jpa.impl.JPAQueryFactory;

import jakarta.persistence.EntityManager;

public class MenuRepositoryCustomImpl implements MenuRepositoryCustom {
	private JPAQueryFactory queryFactory;

	public MenuRepositoryCustomImpl(EntityManager em) {
		this.queryFactory = new JPAQueryFactory(em);
	}

	@Override
	public Page<Menu> getAdminMenuPage(MenuSearchDto menuSearchDto, Pageable pageable) {
		List<Menu> content = queryFactory.selectFrom(QMenu.menu)
				.where(regDtsAfter(menuSearchDto.getSearchDateType()),
						searchSellStatusEq(menuSearchDto.getSearchSellStatus()),
						searchByLike(menuSearchDto.getSearchBy(), menuSearchDto.getSearchQuery()))
				.orderBy(QMenu.menu.id.desc()).offset(pageable.getOffset()).limit(pageable.getPageSize()).fetch();

		long total = queryFactory.select(Wildcard.count).from(QMenu.menu)
				.where(regDtsAfter(menuSearchDto.getSearchDateType()),
						searchSellStatusEq(menuSearchDto.getSearchSellStatus()),
						searchByLike(menuSearchDto.getSearchBy(), menuSearchDto.getSearchQuery()))
				.fetchOne();
		return new PageImpl<>(content, pageable, total);
	}

	/*
	 * @Override public public List<MainMenuDto> getMainMenuList(MenuSearchDto
	 * menuSearchDto, Pageable pageable) { QMenu menu = QMenu.menu; QMenuImg menuImg
	 * = QMenuImg.menuImg;
	 * 
	 * List<MainMenuDto> content = queryFactory .select(new QMainMenuDto(menu.id,
	 * menu.menuNm, menu.menuDetail, menuImg.imgUrl, menu.price))
	 * .from(menuImg).join(menuImg.menu).where(menuImg.repimgYn.eq("Y"))
	 * .where(menuNmLike(menuSearchDto.getSearchQuery())).orderBy(menu.id.desc()).
	 * offset(pageable.getOffset()) .limit(pageable.getPageSize()).fetch();
	 * 
	 * long total =
	 * queryFactory.select(Wildcard.count).from(menuImg).join(menuImg.menu)
	 * .where(menuImg.repimgYn.eq("Y")).where(menuNmLike(menuSearchDto.
	 * getSearchQuery())).fetchOne();
	 * 
	 * return new PageImpl<>(content,pageable,total); }
	 */
	private BooleanExpression menuNmLike(String searchQuery) {
		return StringUtils.isEmpty(searchQuery) ? null : QMenu.menu.menuNm.like("%" + searchQuery + "%");
	}

	private BooleanExpression regDtsAfter(String searchDateType) {
		LocalDateTime dateTime = LocalDateTime.now();

		if (StringUtils.equals("all", searchDateType) || searchDateType == null) {
			return null;
		} else if (StringUtils.equals("1d", searchDateType))
			dateTime = dateTime.minusDays(1);
		else if (StringUtils.equals("1w", searchDateType))
			dateTime = dateTime.minusWeeks(1);
		else if (StringUtils.equals("1m", searchDateType))
			dateTime = dateTime.minusMonths(1);
		else if (StringUtils.equals("6m", searchDateType))
			dateTime = dateTime.minusMonths(6);
		return QMenu.menu.regTime.after(dateTime);
	}

	private BooleanExpression searchSellStatusEq(MenuSellStatus searchSellStatus) {
		return searchSellStatus == null ? null : QMenu.menu.menuSellStatus.eq(searchSellStatus);
	}

	private BooleanExpression searchByLike(String searchBy, String searchQuery) {
		if (StringUtils.equals("menuNm", searchBy)) {
			// 등록자로 검색시
			return QMenu.menu.menuNm.like("%" + searchQuery + "%");// item_nm like%검색어%
		} else if (StringUtils.equals("createdBy", searchBy)) {
			return QMenu.menu.createdBy.like("%" + searchQuery + "%");
		}
		return null;
	}

	@Override
	public List<MainMenuDto> getMainMenuList() {
		QMenu menu = QMenu.menu;
		QMenuImg menuImg = QMenuImg.menuImg;

		List<MainMenuDto> content = queryFactory.select(
				new QMainMenuDto(menu.id, menu.menuNm, menu.menuDetail, menuImg.imgUrl, menu.price, menu.category))
				.from(menuImg).join(menuImg.menu, menu).orderBy(menu.menuNm.asc()).fetch();

		return content;
	}
}
