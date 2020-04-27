package com.victolee.signuplogin.config;

import java.io.IOException;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;

import com.victolee.signuplogin.ApplicationConfig;

import lombok.extern.slf4j.Slf4j;

/*
 * user권한으로 /admin 페이지 들어갈때
 * */
@Slf4j
public class CustomAccessDeniedHandler implements AccessDeniedHandler {

    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response,
	    AccessDeniedException accessDeniedException) throws IOException, ServletException {
	log.debug("CustomAccessDeniedHandler - start");

	log.error(accessDeniedException.getMessage());

	// 실패 했을 시 응답 json
	Map<String, Object> resultMap = new HashMap<>();

	resultMap.put("Status", "AccessDeniedHandler");
	resultMap.put("timestamp", Calendar.getInstance().getTime());
	resultMap.put("exception", accessDeniedException.getMessage());

	// 실패코드 401
	response.setStatus(HttpServletResponse.SC_FORBIDDEN);
	// json 리턴 및 한글깨짐 수정.
	response.setContentType("application/json;charset=utf-8");
	response.getOutputStream().println(ApplicationConfig.ObjectMapper.writeValueAsString(resultMap));

	log.debug("CustomAccessDeniedHandler - end");
    }

}
