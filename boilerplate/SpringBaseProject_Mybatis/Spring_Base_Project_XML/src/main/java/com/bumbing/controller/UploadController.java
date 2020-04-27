package com.bumbing.controller;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.multipart.MultipartFile;

import com.bumbing.mapper.TimeMapper;

import lombok.extern.log4j.Log4j;

/**
 * Handles requests for the application home page.
 */
@Controller
@Log4j
public class UploadController {
	
	
	@RequestMapping(value = "/upload", method = RequestMethod.GET)
	public void upload() {
		
		log.info("upload");
	}
	@RequestMapping(value = "/uploadPost")
	public void uploadPost(ArrayList<MultipartFile> files) {
		
		files.forEach(file->{
			log.info("------------------------");
			log.info("name: "+file.getOriginalFilename());
		});
		
		
	}
	
}
