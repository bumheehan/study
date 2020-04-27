package com.victolee.signuplogin.config;

import java.io.IOException;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import com.victolee.signuplogin.ApplicationConfig;

/*
 * 인증안된 상태로 /admin 들어갈때, RestAPI에서는 필수 
 * */
@Component
public class CustomAuthenticationEntryPoint implements AuthenticationEntryPoint {

    private static final Logger log = LoggerFactory.getLogger(CustomAuthenticationEntryPoint.class);

    public void commence(HttpServletRequest request, HttpServletResponse response,
	    AuthenticationException authException) throws IOException, ServletException {
	log.debug("CustomAuthenticationEntryPoint - start");

	log.error(authException.getMessage());

	// 실패 했을 시 응답 json
	Map<String, Object> resultMap = new HashMap<>();

	resultMap.put("Status", "AuthenticationEntryPoint");
	resultMap.put("timestamp", Calendar.getInstance().getTime());
	resultMap.put("exception", authException.getMessage());

	// 실패코드 401
	response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
	// json 리턴 및 한글깨짐 수정.
	response.setContentType("application/json;charset=utf-8");

	response.getOutputStream().println(ApplicationConfig.ObjectMapper.writeValueAsString(resultMap));

	log.debug("CustomAuthenticationEntryPoint - end");

    }

}
