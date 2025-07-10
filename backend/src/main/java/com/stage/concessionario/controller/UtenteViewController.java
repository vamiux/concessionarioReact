package com.stage.concessionario.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.stage.concessionario.service.UtenteService;

@Controller
@RequestMapping("/utenti")
public class UtenteViewController {

    private final UtenteService utenteService;

    public UtenteViewController(UtenteService utenteService) {
        this.utenteService = utenteService;
    }

    @GetMapping
    public String getUtentiPage(Model model) {
        model.addAttribute("utenti", utenteService.getUtenti());
        return "utenti";
    }
    
    @GetMapping("/search")
    public String searchUtenti(
            @RequestParam(required = false) String codiceFiscale,
            @RequestParam(required = false) String nome,
            @RequestParam(required = false) String cognome,
            Model model) {
        model.addAttribute("utenti", utenteService.searchUtenti(nome, cognome, null));
        return "utenti";
    }
}
