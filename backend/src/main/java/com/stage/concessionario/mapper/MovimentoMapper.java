package com.stage.concessionario.mapper;

import com.stage.concessionario.dto.MovimentoRequestDto;
import com.stage.concessionario.dto.MovimentoResponseDto;
import com.stage.concessionario.model.Movimento;
import com.stage.concessionario.model.Utente;
import com.stage.concessionario.model.Veicolo;
import org.springframework.stereotype.Component;

import java.time.format.DateTimeFormatter;
import java.util.Date;

@Component
public class MovimentoMapper {

    public MovimentoResponseDto toDto(Movimento movimento) {
        if (movimento == null) {
            return null;
        }

        MovimentoResponseDto dto = new MovimentoResponseDto();
        dto.setIdMovimento(movimento.getIdMovimento());
        dto.setCodiceFiscaleUtente(movimento.getUtente().getCodiceFiscaleUtente());
        dto.setNomeUtente(movimento.getUtente().getNome());
        dto.setCognomeUtente(movimento.getUtente().getCognome());
        dto.setNumeroTelaio(movimento.getVeicolo().getNumeroTelaio()); 
        dto.setMarcaVeicolo(movimento.getVeicolo().getMarca());
        dto.setModelloVeicolo(movimento.getVeicolo().getModello());
        dto.setTipoMovimento(movimento.getTipoMovimento());
        dto.setPrezzo(movimento.getPrezzo());
        dto.setDataMovimento(movimento.getDataMovimento());
        
        // Gestione comproprietà
        dto.setHasComproprietario(movimento.isHasComproprietario());
        if (movimento.isHasComproprietario() && movimento.getComproprietario() != null) {
            dto.setCodiceFiscaleComproprietario(movimento.getComproprietario().getCodiceFiscaleUtente());
            dto.setNomeComproprietario(movimento.getComproprietario().getNome());
            dto.setCognomeComproprietario(movimento.getComproprietario().getCognome());
            dto.setPrezzoPerProprietario(movimento.getPrezzoPerProprietario());
        }
        
        return dto;
    }

    public Movimento toEntityFromDtoInput(MovimentoRequestDto dto) {
        if (dto == null) {
            return null;
        }

        Movimento movimento = new Movimento();
        movimento.setTipoMovimento(dto.getTipoMovimento());
        movimento.setPrezzo(dto.getPrezzo());
        movimento.setDataMovimento(dto.getDataMovimento());

        Utente utente = new Utente();
        utente.setCodiceFiscaleUtente(dto.getCodiceFiscaleUtente());
        movimento.setUtente(utente);

        Veicolo veicolo = new Veicolo();
        veicolo.setNumeroTelaio(dto.getNumeroTelaio());
        movimento.setVeicolo(veicolo);
        
        // Gestione comproprietà
        movimento.setHasComproprietario(dto.isHasComproprietario());
        if (dto.isHasComproprietario() && dto.getCodiceFiscaleComproprietario() != null) {
            Utente comproprietario = new Utente();
            comproprietario.setCodiceFiscaleUtente(dto.getCodiceFiscaleComproprietario());
            movimento.setComproprietario(comproprietario);
            movimento.setPrezzoPerProprietario(dto.getPrezzoPerProprietario());
        }

        return movimento;
    }

    public String formatData(Date data) {
        if (data == null) {
            return null;
        }
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        return data.toInstant().atZone(java.time.ZoneId.systemDefault()).format(formatter);
    }
}