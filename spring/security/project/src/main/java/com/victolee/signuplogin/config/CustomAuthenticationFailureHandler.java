package com.victolee.signuplogin.config;

import java.io.IOException;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;

import com.fasterxml.jackson.databind.ObjectMapper;

/*
 * 인증 실패 되었을때
 * */
public class CustomAuthenticationFailureHandler implements AuthenticationFailureHandler {

    private ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response,
	    AuthenticationException exception) throws IOException, ServletException {

	response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
	Map<String, Object> data = new HashMap<>();
	data.put("Status", "AuthenticationFailureHandler");
	data.put("timestamp", Calendar.getInstance().getTime());
	data.put("exception", exception.getMessage());

	response.getOutputStream().println(objectMapper.writeValueAsString(data));
    }
}