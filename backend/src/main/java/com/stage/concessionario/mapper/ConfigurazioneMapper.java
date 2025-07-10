package com.stage.concessionario.mapper;

import com.stage.concessionario.dto.ConfigurazioneRequestDto;
import com.stage.concessionario.dto.ConfigurazioneResponseDto;
import com.stage.concessionario.model.Configurazione;
import org.springframework.stereotype.Component;

@Component
public class ConfigurazioneMapper {

    public ConfigurazioneResponseDto toDto(Configurazione configurazione) {
        if (configurazione == null) {
            return null;
        }

        ConfigurazioneResponseDto dto = new ConfigurazioneResponseDto();
        dto.setIdConfigurazione(configurazione.getIdConfigurazione());
        dto.setNomeConfigurazione(configurazione.getNomeConfigurazione());
        return dto;
    }

    public Configurazione toEntityFromDtoRequest(ConfigurazioneRequestDto dto) {
        if (dto == null) {
            return null;
        }

        Configurazione configurazione = new Configurazione();
        configurazione.setNomeConfigurazione(dto.getNomeConfigurazione());
        return configurazione;
    }

    public void updateConfigurazioneFromDtoInput(ConfigurazioneRequestDto dto, Configurazione configurazione) {
        if (dto == null) {
            return;
        }

        configurazione.setNomeConfigurazione(dto.getNomeConfigurazione());
    }
}