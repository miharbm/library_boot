package com.borodin.libraryboot.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/")

public class StartController {

    @GetMapping()
    public String index(Model model) {
        return "start/start_page";
    }
}
