package com.stage.concessionario.mapper;

import com.stage.concessionario.dto.VeicoloResponseDto;
import com.stage.concessionario.dto.VeicoloRequestDto;
import com.stage.concessionario.dto.VeicoloUpdateDto;
import com.stage.concessionario.model.Veicolo;
import com.stage.concessionario.model.Configurazione;
import com.stage.concessionario.repository.ConfigurazioneRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class VeicoloMapper {

    @Autowired
    private ConfigurazioneMapper configurazioneMapper;
    
    @Autowired
    private ConfigurazioneRepository configurazioneRepository;

    public VeicoloResponseDto toDto(Veicolo veicolo) {
        if (veicolo == null) {
            return null;
        }

        VeicoloResponseDto dto = new VeicoloResponseDto();
        dto.setNumeroTelaio(veicolo.getNumeroTelaio());
        dto.setMarca(veicolo.getMarca());
        dto.setModello(veicolo.getModello());
        dto.setAnnoImmatricolazione(veicolo.getAnnoImmatricolazione());
        dto.setChilometraggio(veicolo.getChilometraggio());
        dto.setDisponibile(veicolo.isDisponibile());
        
        if (veicolo.getConfigurazione() != null) {
            dto.setConfigurazione(configurazioneMapper.toDto(veicolo.getConfigurazione()));
        }
        
        return dto;
    }

    public Veicolo toEntityFromDtoRequest(VeicoloRequestDto dto) {
        if (dto == null) {
            return null;
        }

        Veicolo veicolo = new Veicolo();
        veicolo.setNumeroTelaio(dto.getNumeroTelaio());
        veicolo.setMarca(dto.getMarca());
        veicolo.setModello(dto.getModello());
        veicolo.setAnnoImmatricolazione(dto.getAnnoImmatricolazione());
        veicolo.setChilometraggio(dto.getChilometraggio());
        veicolo.setDisponibile(dto.isDisponibile());
        
        if (dto.getIdConfigurazione() != null) {
            configurazioneRepository.findById(dto.getIdConfigurazione())
                .ifPresent(veicolo::setConfigurazione);
        }
        
        return veicolo;
    }

    public void updateVeicoloFromDtoRequest(VeicoloUpdateDto dto, Veicolo veicolo) {
        if (dto == null) {
            return;
        }

        veicolo.setMarca(dto.getMarca());
        veicolo.setModello(dto.getModello());
        veicolo.setAnnoImmatricolazione(dto.getAnnoImmatricolazione());
        veicolo.setChilometraggio(dto.getChilometraggio());
        veicolo.setDisponibile(dto.isDisponibile());
        
        if (dto.getIdConfigurazione() != null) {
            configurazioneRepository.findById(dto.getIdConfigurazione())
                .ifPresent(veicolo::setConfigurazione);
        }
    }
}