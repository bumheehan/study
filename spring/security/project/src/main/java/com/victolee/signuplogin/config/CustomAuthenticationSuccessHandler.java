package com.victolee.signuplogin.config;

import java.io.IOException;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.victolee.signuplogin.domain.Role;

/*
 * 인증 성공 되었을때
 * */
public class CustomAuthenticationSuccessHandler implements AuthenticationSuccessHandler {

    private static final Logger log = LoggerFactory.getLogger(CustomAuthenticationSuccessHandler.class);

    private ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
	    Authentication authentication) throws IOException, ServletException {

	// 권한마다 세션 타임아웃설정
	HttpSession sess = request.getSession();
	for (GrantedAuthority auth : authentication.getAuthorities()) {
	    log.info(auth.getAuthority());
	    if (Role.ADMIN.getValue().equals(auth.getAuthority())) {
		sess.setMaxInactiveInterval(300);
	    } else {
		sess.setMaxInactiveInterval(30);
	    }
	}

	response.setStatus(HttpServletResponse.SC_OK);
	Map<String, Object> data = new HashMap<>();
	data.put("Status", "AuthenticationSuccessHandler");
	data.put("timestamp", Calendar.getInstance().getTime());
	data.put("sesstion", sess.getMaxInactiveInterval());

	response.getOutputStream().println(objectMapper.writeValueAsString(data));

    }

}