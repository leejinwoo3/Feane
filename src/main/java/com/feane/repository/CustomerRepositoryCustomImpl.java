package com.feane.repository;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.PageImpl;
import org.thymeleaf.util.StringUtils;

import com.feane.dto.CustomerSearchDto;
import com.feane.dto.MainCustomerDto;
import com.feane.dto.QMainCustomerDto;
import com.feane.entity.QCustomer;
import com.feane.entity.QCustomerImg;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.Wildcard;
import com.querydsl.jpa.impl.JPAQueryFactory;

import jakarta.persistence.EntityManager;

public class CustomerRepositoryCustomImpl implements CustomerRepositoryCustom {
	private JPAQueryFactory queryFactory;

	public CustomerRepositoryCustomImpl(EntityManager em) {
		this.queryFactory = new JPAQueryFactory(em);
	}

	@Override
	public Page<MainCustomerDto> getMainCustomerPage(CustomerSearchDto customerSearchDto, Pageable pageable) {
		QCustomer customer = QCustomer.customer;
		QCustomerImg customerImg = QCustomerImg.customerImg;
		List<MainCustomerDto> content = queryFactory
				.select(new QMainCustomerDto(customer.id, customer.customerNm, customer.customerwriteig,
						customerImg.imgUrl))
				.from(customerImg).join(customerImg.customer).where(customerImg.repimgYn.eq("Y"))
				.where(customerNmLike(customerSearchDto.getSearchQuery())).orderBy(customer.id.desc())
				.offset(pageable.getOffset()).limit(pageable.getPageSize()).fetch();

		long total = queryFactory.select(Wildcard.count).from(QCustomer.customer)
				.where(regDtsAfter(customerSearchDto.getSearchDateType()),
						searchByLike(customerSearchDto.getSearchBy(), customerSearchDto.getSearchQuery()))
				.fetchOne();
		return new PageImpl<>(content, pageable, total);
	}

	private BooleanExpression customerNmLike(String searchQuery) {
		return StringUtils.isEmpty(searchQuery) ? null : QCustomer.customer.customerNm.like("%" + searchQuery + "%");
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
		return QCustomer.customer.regTime.after(dateTime);
	}

	private BooleanExpression searchByLike(String searchBy, String searchQuery) {
		if (StringUtils.equals("customerNm", searchBy)) {
			// 등록자로 검색시
			return QCustomer.customer.customerNm.like("%" + searchQuery + "%");// item_nm like%검색어%
		} else if (StringUtils.equals("createdBy", searchBy)) {
			return QCustomer.customer.createdBy.like("%" + searchQuery + "%");
		}
		return null;
	}

	
}
