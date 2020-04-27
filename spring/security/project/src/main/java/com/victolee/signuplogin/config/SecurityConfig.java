package com.victolee.signuplogin.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.context.request.async.WebAsyncManagerIntegrationFilter;
import org.springframework.security.web.savedrequest.NullRequestCache;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import com.victolee.signuplogin.service.MemberService;

@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private MemberService memberService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private AuthenticationEntryPoint authenticationEntryPoint;

    @Override
    public void configure(WebSecurity web) throws Exception {
	// static 디렉터리의 하위 파일 목록은 인증 무시 ( = 항상통과 )
	web.ignoring().antMatchers("/css/**", "/js/**", "/img/**", "/lib/**");
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
	http.authorizeRequests()
		// 페이지 권한 설정
		.antMatchers("/admin/**").hasRole("ADMIN").antMatchers("/user/info").hasRole("MEMBER")
		.antMatchers("/", "/user/login", "/user/signup").permitAll().antMatchers("/h2_console/*").permitAll()
		.anyRequest().authenticated()
		// 로그인
		.and().formLogin().loginPage("/user/login").failureHandler(new CustomAuthenticationFailureHandler())
		.successHandler(new CustomAuthenticationSuccessHandler())// .defaultSuccessUrl("/user/login/result")
		// csrf
		.and().csrf().disable()
		// header
		.headers().frameOptions().disable()
		// 로그아웃 설정
		.and().logout().logoutRequestMatcher(new AntPathRequestMatcher("/user/logout")).permitAll()
		.logoutSuccessUrl("/user/logout/result").invalidateHttpSession(true).deleteCookies("JSESSIONID")
		// 403 예외처리 핸들링 & entrypoint
		.and().exceptionHandling().accessDeniedPage("/user/denied")
		.authenticationEntryPoint(authenticationEntryPoint).accessDeniedHandler(new CustomAccessDeniedHandler())
		// 로깅 필터
		.and().addFilterBefore(new LoggingFilter(), WebAsyncManagerIntegrationFilter.class)
		// request cache , RestAPI에서는 필수
		.requestCache().requestCache(new NullRequestCache());

    }

    @Override
    public void configure(AuthenticationManagerBuilder auth) throws Exception {
	auth.userDetailsService(memberService).passwordEncoder(passwordEncoder);
    }

}
