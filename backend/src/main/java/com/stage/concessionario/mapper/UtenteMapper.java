package com.stage.concessionario.mapper;

import com.stage.concessionario.dto.UtenteRequestDto;
import com.stage.concessionario.dto.UtenteResponseDto;
import com.stage.concessionario.dto.UtenteUpdateDto;
import com.stage.concessionario.model.Utente;
import org.springframework.stereotype.Component;

@Component
public class UtenteMapper {

    public UtenteResponseDto toDto(Utente utente) {
        if (utente == null) {
            return null;
        }

        UtenteResponseDto dto = new UtenteResponseDto();
        dto.setCodiceFiscaleUtente(utente.getCodiceFiscaleUtente());
        dto.setNome(utente.getNome());
        dto.setCognome(utente.getCognome());
        dto.setDataNascita(utente.getDataNascita());
        dto.setTelefono(utente.getTelefono());
        dto.setEmail(utente.getEmail());
        dto.setIndirizzo(utente.getIndirizzo());
        return dto;
    }

    public Utente toEntityFromDtoRequest(UtenteRequestDto dto) {
        if (dto == null) {
            return null;
        }

        Utente utente = new Utente();
        try {
            utente.setCodiceFiscaleUtente(dto.getCodiceFiscaleUtente());
            utente.setNome(dto.getNome());
            utente.setCognome(dto.getCognome());
            utente.setDataNascita(dto.getDataNascita());
            utente.setTelefono(dto.getTelefono());
            utente.setEmail(dto.getEmail());
            utente.setIndirizzo(dto.getIndirizzo());
            return utente;
        } catch (Exception e) {
            throw new IllegalArgumentException("Errore durante la conversione dei dati: " + e.getMessage());
        }
    }

    public void updateUtenteFromDtoRequest(UtenteUpdateDto dto, Utente utente) {
        if (dto == null) {
            return;
        }
    
        if (dto.getNome() != null) {
            utente.setNome(dto.getNome());
        }
        
        if (dto.getCognome() != null) {
            utente.setCognome(dto.getCognome());
        }
        
        if (dto.getDataNascita() != null) {
            utente.setDataNascita(dto.getDataNascita());
        }
        
        if (dto.getTelefono() != null) {
            utente.setTelefono(dto.getTelefono());
        }
        
        if (dto.getEmail() != null) {
            utente.setEmail(dto.getEmail());
        }
        
        if (dto.getIndirizzo() != null) {
            utente.setIndirizzo(dto.getIndirizzo());
        }
    }
}