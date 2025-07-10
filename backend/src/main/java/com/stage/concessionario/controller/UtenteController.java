package com.stage.concessionario.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.stage.concessionario.dto.UtenteRequestDto;
import com.stage.concessionario.dto.UtenteResponseDto;
import com.stage.concessionario.dto.UtenteUpdateDto;
import com.stage.concessionario.service.UtenteService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/utenti")
public class UtenteController {

    private final UtenteService utenteService;
    private static final Logger logger = LoggerFactory.getLogger(UtenteController.class);

    public UtenteController(UtenteService utenteService) {
        this.utenteService = utenteService;
    }

    @GetMapping
    public List<UtenteResponseDto> getUtenti() {
        return utenteService.getUtenti();
    }
    
    @GetMapping(params = "codiceFiscale")
    public UtenteResponseDto getUtenteByCodiceFiscale(@RequestParam String codiceFiscale) {
        return utenteService.getUtenteByCodiceFiscale(codiceFiscale);
    }
    
    @GetMapping("/{codiceFiscale}")
    public UtenteResponseDto getUtenteByCodiceFiscalePath(@PathVariable String codiceFiscale) {
        return utenteService.getUtenteByCodiceFiscale(codiceFiscale);
    }
    
    @GetMapping("/search")
    public List<UtenteResponseDto> searchUtenti(
            @RequestParam(required = false) String nome,
            @RequestParam(required = false) String cognome,
            @RequestParam(required = false) String email) {
        return utenteService.searchUtenti(nome, cognome, email);
    }
    
    @PostMapping
    public ResponseEntity<?> insert(@Valid @RequestBody UtenteRequestDto utenteRequest) {
        try {
            logger.info("Ricevuta richiesta di inserimento utente: {}", utenteRequest);
            
            // Validazione campi obbligatori
            if (utenteRequest.getCodiceFiscaleUtente() == null || utenteRequest.getCodiceFiscaleUtente().trim().isEmpty()) {
                String errMsg = "Il codice fiscale è obbligatorio";
                logger.error(errMsg);
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errMsg);
            }
            if (utenteRequest.getNome() == null || utenteRequest.getNome().trim().isEmpty()) {
                String errMsg = "Il nome è obbligatorio";
                logger.error(errMsg);
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errMsg);
            }
            if (utenteRequest.getCognome() == null || utenteRequest.getCognome().trim().isEmpty()) {
                String errMsg = "Il cognome è obbligatorio";
                logger.error(errMsg);
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errMsg);
            }
            if (utenteRequest.getDataNascita() == null) {
                String errMsg = "La data di nascita è obbligatoria";
                logger.error(errMsg);
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errMsg);
            }
            if (utenteRequest.getEmail() == null || utenteRequest.getEmail().trim().isEmpty()) {
                String errMsg = "L'email è obbligatoria";
                logger.error(errMsg);
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errMsg);
            }
            if (utenteRequest.getIndirizzo() == null || utenteRequest.getIndirizzo().trim().isEmpty()) {
                String errMsg = "L'indirizzo è obbligatorio";
                logger.error(errMsg);
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errMsg);
            }

            UtenteResponseDto utenteResponseDto = utenteService.insert(utenteRequest);
            logger.info("Utente inserito con successo: {}", utenteResponseDto);
            return ResponseEntity.status(HttpStatus.CREATED).body(utenteResponseDto);
        } catch (IllegalArgumentException e) {
            logger.error("Errore durante l'inserimento dell'utente: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
        } catch (Exception e) {
            logger.error("Errore imprevisto durante l'inserimento dell'utente: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Errore interno del server: " + e.getMessage());
        }
    }
    
    @PutMapping(params = "codiceFiscale")
    public ResponseEntity<UtenteResponseDto> update(
            @Valid @RequestParam String codiceFiscale, 
            @RequestBody UtenteUpdateDto utenteUpdateDto) {
        UtenteResponseDto utenteResponseDto = utenteService.update(utenteUpdateDto, codiceFiscale);
        
        if (utenteResponseDto == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
        
        return ResponseEntity.status(HttpStatus.OK).body(utenteResponseDto);
    }
    
    @PutMapping("/{codiceFiscale}")
    public ResponseEntity<UtenteResponseDto> updateByPath(
            @Valid @PathVariable String codiceFiscale, 
            @RequestBody UtenteUpdateDto utenteUpdateDto) {
        UtenteResponseDto utenteResponseDto = utenteService.update(utenteUpdateDto, codiceFiscale);
        
        if (utenteResponseDto == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
        
        return ResponseEntity.status(HttpStatus.OK).body(utenteResponseDto);
    }
    
}
