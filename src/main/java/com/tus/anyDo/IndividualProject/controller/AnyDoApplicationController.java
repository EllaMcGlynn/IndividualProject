package com.tus.anyDo.IndividualProject.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class AnyDoApplicationController {

    @RequestMapping("/")
    public String redirectToLandingPage() {
        return "redirect:/landingPage.html";
    }
}

