package com.feane.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.feane.constant.MenuSellStatus;
import com.feane.entity.Menu;

public interface MenuRepository extends JpaRepository<Menu, Long>, MenuRepositoryCustom{
	List<Menu> findByMenuNm(String menuNm);

	List<Menu> findByMenuNmAndMenuSellStatus(String menuNm, MenuSellStatus menuSellStatus);

	@Query("select m from Menu m where m.menuDetail like %:menuDetail% order by m.price desc")
	List<Menu> findByMenuDetail(@Param("menuDetail") String menuDetail);

	// JPQL(native 쿼리) -> h2 데이터베이스를 기준으로 한 쿼리문 작성
	@Query(value = "select * from Menu where menu_detail like %:menuDetail% order by price desc", nativeQuery = true)
	List<Menu> findByMenuDetailByNative(@Param("menuDetail") String menuDetail);

	// JPQL(non native 쿼리) -> 컬럼명, 테이블명은 entity 클래스 기준으로 작성한다.
	@Query("select m from Menu m where m.price >= :price")
	List<Menu> findByPrice(@Param("price") int price);

	// JPQL(non native 쿼리) -> 컬럼명, 테이블명은 entity 클래스 기준으로 작성한다.
	@Query("select m from Menu m where m.menuNm = :menuNm and m.menuSellStatus = :sell")
	List<Menu> findByMenuNmAndMenuSellStatus2(@Param("menuNm") String menuNm, @Param("sell") MenuSellStatus sell);


	
	
	
}
