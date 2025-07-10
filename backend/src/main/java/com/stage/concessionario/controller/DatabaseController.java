package com.stage.concessionario.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.stage.concessionario.service.DatabaseService;

@RestController
@RequestMapping("/api/database")
public class DatabaseController {

    private final DatabaseService databaseService;

    public DatabaseController(DatabaseService databaseService) {
        this.databaseService = databaseService;
    }

    @PostMapping("/reset-movimento-sequence")
    public ResponseEntity<String> resetMovimentoSequence() {
        databaseService.resetSequence("movimento");
        return ResponseEntity.ok("Sequenza della tabella movimento resettata con successo");
    }

    @PostMapping("/reset-configurazione-sequence")
    public ResponseEntity<String> resetConfigurazioneSequence() {
        databaseService.resetSequence("configurazione");
        return ResponseEntity.ok("Sequenza della tabella configurazione resettata con successo");
    }

    @PostMapping("/reset-amministratore-sequence")
    public ResponseEntity<String> resetAmministratoreSequence() {
        databaseService.resetSequence("amministratore");
        return ResponseEntity.ok("Sequenza della tabella amministratore resettata con successo");
    }
    
    @PostMapping("/create-movimento-delete-trigger")
    public ResponseEntity<String> createMovimentoDeleteTrigger() {
        databaseService.createTriggerForMovimentiDelete();
        return ResponseEntity.ok("Trigger per l'aggiornamento della disponibilità del veicolo creato con successo");
    }
}
