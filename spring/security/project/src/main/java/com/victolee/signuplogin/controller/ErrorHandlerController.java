package com.victolee.signuplogin.controller;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;

import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/error")
public class ErrorHandlerController implements ErrorController {

    // html로 올경우 에러처리
    @RequestMapping(produces = MediaType.TEXT_HTML_VALUE)
    public @ResponseBody Map<String, String> handleErrorHtml(HttpServletRequest request) {
	Object status = request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE);

	Map<String, String> result = new HashMap<>();
	if (status != null) {
	    int statusCode = Integer.valueOf(status.toString());

	    if (statusCode == HttpStatus.NOT_FOUND.value()) {
		result.put("code", "9997");
		result.put("message", "[404] NOT_FOUND");
		return result;
	    }
	    if (statusCode == HttpStatus.FORBIDDEN.value()) {
		result.put("code", "9996");
		result.put("message", "[403] FORBIDDEN");
		return result;
	    }
	} else {
	    result.put("code", "9998");
	    result.put("message", "[error] ERROR");
	}

	return result;
    }

    // html 외 에러처리
    @RequestMapping
    public @ResponseBody Map<String, String> handleError(HttpServletRequest request) {
	Object status = request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE);

	Map<String, String> result = new HashMap<>();
	if (status != null) {
	    int statusCode = Integer.valueOf(status.toString());

	    if (statusCode == HttpStatus.NOT_FOUND.value()) {
		result.put("code", "9997");
		result.put("message", "[404] NOT_FOUND");
		return result;
	    }
	    if (statusCode == HttpStatus.FORBIDDEN.value()) {
		result.put("code", "9996");
		result.put("message", "[403] FORBIDDEN");
		return result;
	    }
	} else {
	    result.put("code", "9998");
	    result.put("message", "[error] ERROR");
	}

	return result;
    }

    @Override
    public String getErrorPath() {
	return "/error";
    }
}
