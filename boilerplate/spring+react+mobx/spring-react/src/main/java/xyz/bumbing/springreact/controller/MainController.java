package xyz.bumbing.springreact.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class MainController {
 
    @GetMapping("/")
    public String page(Model model) {
        return "index.html";
    }
 
}
