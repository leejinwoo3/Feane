# Feane
## 프로젝트 소개
스프링 부트 + JSP를 이용한 음식주문 사이트

## 개발 기간 (Table of Contents)
* 23.07.17 - 23.08.16일
 
### 멤버구성
개인프로젝트<br>
- 이진우 - 주문, 장바구니, 로그인, 회원가입, 주문관리(CRUD), 관리자페이지, 
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
<pre><code>
 public interface CartMenuRepository extends JpaRepository<CartMenu, Long> {
	CartMenu findByMenuAndCart(Menu menu, Cart cart);

	@Query("select new com.feane.dto.CartDetailDto(ci.id, i.menuNm, i.price, ci.count, im.imgUrl, ci.menu.id menuId) "
			+ "from CartMenu ci, MenuImg im " + "join ci.menu i " + "where ci.cart.id = :cartId "
			+ "and im.menu.id = ci.menu.id " + "and im.repimgYn = 'Y' " + "order by ci.regTime desc")
	List<CartDetailDto> findCartDetailDtoList(@Param("cartId") Long cartId);

} 
</code></pre>
- `@Query`: JPQL 쿼리를 사용합니다. 해당 메소드는 CartDetailDto특정 DTO를 사용하여 장바구니 정보를 상세하게 조회합니다.
- `CartMenu findByMenuAndCart(Menu menu, Cart cart)`: 카트에 담겨있는 메뉴와 카트를 기반으로 CartMenu분리를 조회하는 방법입니다. 생성된 메서드 이름은 Spring Data JPA에서 생성되어 해당 조건에 맞는 데이터를 조회하는 쿼리를 자동으로 생성합니다.

<pre><code>
@Configuration // Bean 객체를 싱글톤으로 객체를 관리해준다.
@EnableWebSecurity 
public class SecurityConfig {
	@Bean
	public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
		// 로그인에 대한 설정
		http.authorizeHttpRequests(authorize->authorize//페이지 접근에 관한
				//모든 사용자가 로그인(인증) 없이 접근할수 있도록 설정
				.requestMatchers("/css/**","/js/**","/img/**","/images/**","/fonts/**").permitAll()
				.requestMatchers("/","/members/**","/menu/**", "/franchisee/**","/member/**","/order/**","/cart/","/about/").permitAll()
				.requestMatchers("/favicon.ico","/error").permitAll()
				//'admin'으로 시작하는 경로는 관리자만 접근가능하도록 설정
				.requestMatchers("/admin/**").hasRole("ADMIN")
				.anyRequest().authenticated()//그외 페이지는 모두 로그인 (인증을 받아야한다.)
				)
		.formLogin(formLogin -> formLogin//2.로그인에 관련된 설정
				.loginPage("/members/login")//로그인페이지 URL설정
				.defaultSuccessUrl("/")//로그인 성공시 이동할 페이지
				.usernameParameter("email")//로그인시 id로 사용할 파라메터 이름
				.failureUrl("/members/login/error")//로그인 실패시 이동할 url
				//.permitAll()
				)
		.logout(logout->logout//3.로그아웃에 관련된 설정
				.logoutRequestMatcher(new AntPathRequestMatcher("/members/logout"))
				.logoutSuccessUrl("/")//로그아웃 성공시 이동할 URL
				//.permitAll()
				)	//4.인증 되지 않은 사용자가 리소스에 접근했을때 설정(ex.로그인 안했는데 cart페이지에 접근..)
		.exceptionHandling(handling-> handling
				.authenticationEntryPoint(new CustomAuthenticationEntryPoint())
				)
		.rememberMe(Customizer.withDefaults());
	
		return http.build();
		
	}

	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
}
</code></pre>



![Anurag's GitHub stats](https://github-readme-stats.vercel.app/api?username=leejinwoo3&show_icons=true&theme=radical)
[![Top Langs](https://github-readme-stats.vercel.app/api/top-langs/?username=leejinwoo3&layout=compact)](https://github.com/delay-100/github-readme-stats)

