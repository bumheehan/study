package xyz.bumbing.springreact.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

// @Configuration
// @EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {
//    private MemberService memberService;

    // 비밀번호 암호화 방식 빈 설정
    @Bean
    public PasswordEncoder passwordEncoder() {
	return new BCryptPasswordEncoder();
    }

    /*
     * WebSecurity는 리소스 무시 , 디버그 모드 설정, 사용자 지정 방화벽 정의 등 설정
     */
    @Override
    public void configure(final WebSecurity web) throws Exception {
		// static 디렉터리의 하위 파일 목록은 인증 무시 ( = 항상통과 )
		web.ignoring().antMatchers("/resource/**");
	}

	/*
	 * HttpSecurity 는 HTTP 요청에 대한 웹 기반 보안을 설정
	 */
	@Override
	protected void configure(final HttpSecurity http) throws Exception {
		http.authorizeRequests()// 페이지 권한 설정
				.antMatchers("/admin/**").hasRole("ADMIN") // /admin으로 시작하는 페이지는 Role이 ADMIN 사용자만 사용가능
				.antMatchers("/user/myinfo").hasRole("MEMBER") // /user/myinfo로 시작하는 페이지는 Role이 MEMBER인 사용자만 사용가능
				.antMatchers("/**").permitAll() // 모든경로에 대해서 권한없이 접근가능
				// .anyRequest().authenticated()// 모든 요청에 대해, 인증된 사용자만 접근하도록 설정할 수도 있습니다. ( 예제에는
				// 적용 안함 )
				// .antMatchers(HttpMethod.GET, "/books/**").hasRole("USER") //메소드마다 줄수있음
				// .antMatchers(HttpMethod.POST, "/books").hasRole("ADMIN")
				// .antMatchers(HttpMethod.PUT, "/books/**").hasRole("ADMIN")
				// .antMatchers(HttpMethod.PATCH, "/books/**").hasRole("ADMIN")
				// .antMatchers(HttpMethod.DELETE, "/books/**").hasRole("ADMIN")
				.and() // 로그인 설정
				.formLogin() // form 기반으로 인증을 하도록 합니다. 로그인 정보는 기본적으로 HttpSession을 이용합니다.
				//.loginPage("/user/login")/// login 경로로 접근하면, Spring Security에서 제공하는 로그인 form을 사용할 수 있습니다. 커스턴 로그인 페이지
				/// 하려면 다음과 같이사용
				.defaultSuccessUrl("/user/login/result")// 로그인성공시 이동경로
				.permitAll() // ? 확인
				// .usernameParameter("파라미터명")// 로그인 form에서 아이디는 name=username인 input을 기본으로
				// 인식하는데, usernameParameter() 메서드를 통해 파라미터명을 변경할 수 있습니다. ( 예제에는 적용 안함 )
				.and() // 로그아웃 설정
				.logout()// 로그아웃을 지원하는 메서드이며, WebSecurityConfigurerAdapter를 사용할 때 자동으로 적용됩니다. 기본적으로
				// "/logout"에 접근하면 HTTP 세션을 제거합니다.
				.logoutRequestMatcher(new AntPathRequestMatcher("/user/logout"))// 로그아웃의 기본 URL(/logout) 이 아닌 다른 URL로
				// 재정의합니다.
				.logoutSuccessUrl("/user/logout/result").invalidateHttpSession(true)// Http 세션 초기화
				// .deleteCookies("KEY명") //로그아웃 시, 특정 쿠기를 제거하고 싶을 때 사용하는 메서드입니다. ( 예제에는 적용안함 )
				.and()
				// 403 예외처리 핸들링
				.exceptionHandling().accessDeniedPage("/user/denied")// 예외가 발생했을 때 exceptionHandling() 메서드로 핸들링할 수 있습니다.
		// 예제에서는 접근권한이 없을 때, 로그인 페이지로 이동하도록 명시해줬습니다
		;
		// .and()
		// .addFilterAfter(filter, afterFilter) // 이 필터 전 후로 다른 필터추가
		// .addFilterBefore(filter, beforeFilter)

		/*
		 * rest api용
		 */
		// http
		// .httpBasic().disable() // rest api 이므로 기본설정 사용안함. 기본설정은 비인증시 로그인폼 화면으로 리다이렉트
		// 된다.
		// .csrf().disable() // rest api이므로 csrf 보안이 필요없으므로 disable처리.
		// .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
		// // jwt token으로 인증하므로 세션은 필요없으므로 생성안함.
		// .and()
		// .authorizeRequests() // 다음 리퀘스트에 대한 사용권한 체크
		// .antMatchers("/*/signin", "/*/signup").permitAll() // 가입 및 인증 주소는 누구나 접근가능
		// .antMatchers(HttpMethod.GET, "helloworld/**").permitAll() // hellowworld로
		// 시작하는 GET요청 리소스는 누구나 접근가능
		// .anyRequest().hasRole("USER") // 그외 나머지 요청은 모두 인증된 회원만 접근 가능
		// .and()
		// .addFilterBefore(new JwtAuthenticationFilter(jwtTokenProvider),
		// UsernamePasswordAuthenticationFilter.class); // jwt token 필터를 id/password 인증
		// 필터 전에 넣는다

	}

	/*
	 * AuthenticationManagerBuilder은 AuthenticationProviders를 쉽게 추가 할 수있게하여 인증 메커니즘을
	 * 설정하는 데 사용됩니다.
	 */
	@Override
	public void configure(final AuthenticationManagerBuilder auth) throws Exception {
	/*
	 * 로그인 처리 즉, 인증을 위해서는 UserDetailService를 통해서 필요한 정보들을 가져오는데, 예제에서는 서비스
	 * 클래스(memberService)에서 이를 처리합니다.
	 */
//	auth.userDetailsService(memberService).passwordEncoder(passwordEncoder());

//        .auth
//        .inMemoryAuthentication()
//        .withUser("user")
//        .password("password")
//        .roles("USER")
//        .and()
//        .withUser("admin")
//        .password("password")
//        .roles("ADMIN","USER");
    }

}