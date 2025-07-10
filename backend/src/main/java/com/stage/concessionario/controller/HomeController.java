package com.stage.concessionario.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.security.core.Authentication;

@Controller
public class HomeController {

    @GetMapping("/")
    public String home(Authentication authentication, Model model) {
        // Ottiene il nome dell'utente autenticato
        String username = authentication.getName();
        model.addAttribute("username", username);
        
        return "index";
    }
}
