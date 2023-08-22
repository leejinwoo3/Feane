# Feane
## 프로젝트 소개
스프링 부트 + JSP를 이용한 음식주문 사이트

## 개발 기간 (Table of Contents)
* 23.07.17 - 23.08.16일
 
### 멤버구성
*개인프로젝트 *<br>
이진우 - 주문, 장바구니, 로그인, 회원가입, 주문관리(CRUD), 관리자페이지, 
         고객의 소리(CRUD),가맹점문의,메뉴 관리,메뉴수정(CRUD)

### 개발 스텍
- 언어 : Java 17
- 프레임워크 : Spring Boot, Spring MVC, Spring JPA
- 데이터베이스 : MySQL
- 보안 : spring security
- 빌드 : Maven
### ERD
<img width="30%" src="./images/image.png"/>

### USECASE
<img width="30%" src="./images/image2.png"/>
### JPA
public interface CartMenuRepository extends JpaRepository<CartMenu, Long> {
	CartMenu findByMenuAndCart(Menu menu, Cart cart);

	@Query("select new com.feane.dto.CartDetailDto(ci.id, i.menuNm, i.price, ci.count, im.imgUrl, ci.menu.id menuId) "
			+ "from CartMenu ci, MenuImg im " + "join ci.menu i " + "where ci.cart.id = :cartId "
			+ "and im.menu.id = ci.menu.id " + "and im.repimgYn = 'Y' " + "order by ci.regTime desc")
	List<CartDetailDto> findCartDetailDtoList(@Param("cartId") Long cartId);

}
- `@Query`: JPQL 쿼리를 사용합니다. 해당 메소드는 CartDetailDto특정 DTO를 사용하여 장바구니 정보를 상세하게 조회합니다.
-`CartMenu findByMenuAndCart(Menu menu, Cart cart)`: 카트에 담겨있는 메뉴와 카트를 기반으로 CartMenu분리를 조회하는 방법입니다. 생성된 메서드 이름은 Spring Data JPA에서 생성되어 해당 조건에 맞는 데이터를 조회하는 쿼리를 자동으로 생성합니다.
