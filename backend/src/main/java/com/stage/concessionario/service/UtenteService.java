package com.stage.concessionario.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;

import com.stage.concessionario.dto.UtenteRequestDto;
import com.stage.concessionario.dto.UtenteResponseDto;
import com.stage.concessionario.dto.UtenteUpdateDto;
import com.stage.concessionario.mapper.UtenteMapper;
import com.stage.concessionario.model.Utente;
import com.stage.concessionario.repository.UtenteRepository;

@Service
public class UtenteService {

    private final UtenteRepository utenteRepository;
    private final UtenteMapper utenteMapper;
    private static final Logger logger = LogManager.getLogger(UtenteService.class);

    public UtenteService(UtenteRepository utenteRepository, UtenteMapper utenteMapper) {
        this.utenteRepository = utenteRepository;
        this.utenteMapper = utenteMapper;
    }

    public List<UtenteResponseDto> getUtenti() {
        List<Utente> utenti = utenteRepository.findAll();
        logger.info("Lista utenti visualizzata correttamente");
        return utenti.stream().map(utenteMapper::toDto).collect(Collectors.toList());
    }

    public UtenteResponseDto getUtenteByCodiceFiscale(String codiceFiscale) {
        Optional<Utente> utente = utenteRepository.findByCodiceFiscaleUtente(codiceFiscale);
        if (utente.isPresent()) {
            logger.info("Utente con codice fiscale {} trovato correttamente", codiceFiscale);
            return utenteMapper.toDto(utente.get());
        } else {
            logger.error("Utente con codice fiscale {} non esistente", codiceFiscale);
            return null;
        }
    }

    public List<UtenteResponseDto> searchUtenti(String nome, String cognome, String email) {
        List<Utente> risultati = utenteRepository.findAll();

        if (nome != null && !nome.isEmpty()) {
            risultati = utenteRepository.findByNomeContainingIgnoreCase(nome);
        }
        if (cognome != null && !cognome.isEmpty()) {
            risultati = utenteRepository.findByCognomeContainingIgnoreCase(cognome);
        }
        if (email != null && !email.isEmpty()) {
            risultati = utenteRepository.findByEmailContainingIgnoreCase(email);
        }

        logger.info("Ricerca utenti completata con {} risultati", risultati.size());
        return risultati.stream().map(utenteMapper::toDto).collect(Collectors.toList());
    }

    public UtenteResponseDto insert(UtenteRequestDto utenteRequestDto) {
        logger.debug("Tentativo di inserimento utente: {}", utenteRequestDto);

        // Validazione input
        if (utenteRequestDto == null) {
            logger.error("UtenteRequestDto è null");
            throw new IllegalArgumentException("I dati dell'utente non possono essere null");
        }

        if (utenteRequestDto.getCodiceFiscaleUtente() == null || utenteRequestDto.getCodiceFiscaleUtente().trim().isEmpty()) {
            logger.error("Codice fiscale mancante");
            throw new IllegalArgumentException("Il codice fiscale è obbligatorio");
        }

        Optional<Utente> existingUtente = utenteRepository.findByCodiceFiscaleUtente(utenteRequestDto.getCodiceFiscaleUtente());
        if (existingUtente.isPresent()) {
            logger.error("Esiste già un utente con questo codice fiscale: {}", utenteRequestDto.getCodiceFiscaleUtente());
            throw new IllegalArgumentException("Codice fiscale già in uso");
        }

        try {
            Utente utente = utenteMapper.toEntityFromDtoRequest(utenteRequestDto);
            
            // Log dei dati prima del salvataggio
            logger.debug("Dati utente prima del salvataggio: nome={}, cognome={}, dataNascita={}, email={}, indirizzo={}", 
                utente.getNome(), 
                utente.getCognome(), 
                utente.getDataNascita(), 
                utente.getEmail(),
                utente.getIndirizzo()
            );
            
            Utente savedUtente = utenteRepository.save(utente);
            logger.info("Utente con codice fiscale {} inserito correttamente", savedUtente.getCodiceFiscaleUtente());
            
            return utenteMapper.toDto(savedUtente);
        } catch (Exception e) {
            logger.error("Errore durante il salvataggio dell'utente: {}", e.getMessage(), e);
            throw new IllegalArgumentException("Errore durante il salvataggio dell'utente: " + e.getMessage());
        }
    }

    public UtenteResponseDto update(UtenteUpdateDto utenteUpdateDto, String codiceFiscale) {
        Optional<Utente> existingUtenteOpt = utenteRepository.findByCodiceFiscaleUtente(codiceFiscale);
        
        if (!existingUtenteOpt.isPresent()) {
            logger.error("Utente con codice fiscale {} non trovato per l'aggiornamento", codiceFiscale);
            return null;
        }

        Utente existingUtente = existingUtenteOpt.get();
        utenteMapper.updateUtenteFromDtoRequest(utenteUpdateDto, existingUtente);

        utenteRepository.save(existingUtente);

        logger.info("Utente con codice fiscale {} aggiornato correttamente", codiceFiscale);

        return utenteMapper.toDto(existingUtente);
    }
}
