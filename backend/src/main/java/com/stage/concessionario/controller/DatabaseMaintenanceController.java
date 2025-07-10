package com.stage.concessionario.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/api/database")
public class DatabaseMaintenanceController {

    @Autowired
    private JdbcTemplate jdbcTemplate;
    
    @GetMapping("/reset-movimento-sequence")
    @ResponseBody
    public String resetMovimentoSequence() {
        try {
            jdbcTemplate.execute("ALTER TABLE movimento AUTO_INCREMENT = 1");
            return "Sequenza ID movimenti resettata con successo";
        } catch (Exception e) {
            return "Errore durante il reset della sequenza: " + e.getMessage();
        }
    }
}
