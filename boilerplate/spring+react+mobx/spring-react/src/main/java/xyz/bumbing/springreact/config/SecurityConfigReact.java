package xyz.bumbing.springreact.config;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@Configuration
@EnableWebSecurity
public class SecurityConfigReact extends WebSecurityConfigurerAdapter {

	@Override
	protected void configure(final HttpSecurity http) throws Exception {
		http.authorizeRequests()// 페이지 권한 설정
				.antMatchers("/**").permitAll() 
				.and() // 로그인 설정
				.formLogin().disable()
				.csrf().disable();
	}
}