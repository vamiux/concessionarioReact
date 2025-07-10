package com.stage.concessionario.controller;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.stage.concessionario.model.TipoMovimento;
import com.stage.concessionario.service.MovimentoService;

import java.util.Date;

@Controller
@RequestMapping("/movimenti")
public class MovimentoViewController {

    private final MovimentoService movimentoService;

    public MovimentoViewController(MovimentoService movimentoService) {
        this.movimentoService = movimentoService;
    }

    @GetMapping
    public String getMovimentiPage(Model model) {
        model.addAttribute("movimenti", movimentoService.getMovimenti());
        return "movimenti";
    }
    
    @GetMapping("/search")
    public String searchMovimenti(
            @RequestParam(required = false) String tipo,
            @RequestParam(required = false) String numeroTelaio,
            @RequestParam(required = false) String codiceFiscaleUtente,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Date dataInizio,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Date dataFine,
            Model model) {
        
        // Converti la stringa del tipo in enum se presente
        TipoMovimento tipoMovimento = null;
        if (tipo != null && !tipo.isEmpty()) {
            try {
                tipoMovimento = TipoMovimento.valueOf(tipo);
            } catch (IllegalArgumentException e) {
                // Ignora se il tipo non è valido
            }
        }
        
        // Chiama il servizio per cercare i movimenti con i filtri specificati
        model.addAttribute("movimenti", movimentoService.searchMovimenti(
                tipoMovimento, numeroTelaio, codiceFiscaleUtente, dataInizio, dataFine));
        
        return "movimenti";
    }
}
