package com.tus.anyDo.IndividualProject.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class AnyDoApplicationController {

	@GetMapping("/")
    public String loadIndexPage() {
        return "index.html"; 
    }
}

