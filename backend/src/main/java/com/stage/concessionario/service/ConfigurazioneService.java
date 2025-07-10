package com.stage.concessionario.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;

import com.stage.concessionario.dto.ConfigurazioneRequestDto;
import com.stage.concessionario.dto.ConfigurazioneResponseDto;
import com.stage.concessionario.mapper.ConfigurazioneMapper;
import com.stage.concessionario.model.Configurazione;
import com.stage.concessionario.repository.ConfigurazioneRepository;

@Service
public class ConfigurazioneService {

    private final ConfigurazioneRepository configurazioneRepository;
    private final ConfigurazioneMapper configurazioneMapper;
    private static final Logger logger = LogManager.getLogger(ConfigurazioneService.class);

    public ConfigurazioneService(ConfigurazioneRepository configurazioneRepository, ConfigurazioneMapper configurazioneMapper) {
        this.configurazioneRepository = configurazioneRepository;
        this.configurazioneMapper = configurazioneMapper;
    }

    public List<ConfigurazioneResponseDto> getConfigurazioni() {
        List<Configurazione> configurazioni = configurazioneRepository.findAll();
        logger.info("Lista configurazioni visualizzata correttamente");
        return configurazioni.stream().map(configurazioneMapper::toDto).collect(Collectors.toList());
    }

    public ConfigurazioneResponseDto getConfigurazioneById(Integer id) {
        Optional<Configurazione> configurazione = configurazioneRepository.findById(id);
        if (configurazione.isPresent()) {
            logger.info("Configurazione con ID {} trovata correttamente", id);
            return configurazioneMapper.toDto(configurazione.get());
        } else {
            logger.error("Configurazione con ID {} non esistente", id);
            return null;
        }
    }

    public ConfigurazioneResponseDto getConfigurazioneByNome(String nomeConfigurazione) {
        Optional<Configurazione> configurazione = configurazioneRepository.findByNomeConfigurazione(nomeConfigurazione);
        if (configurazione.isPresent()) {
            logger.info("Configurazione con nome {} trovata correttamente", nomeConfigurazione);
            return configurazioneMapper.toDto(configurazione.get());
        } else {
            logger.error("Configurazione con nome {} non esistente", nomeConfigurazione);
            return null;
        }
    }

    public ConfigurazioneResponseDto insert(ConfigurazioneRequestDto configurazioneRequestDto) {
        logger.debug("Tentativo di inserimento configurazione: {}", configurazioneRequestDto);

        Optional<Configurazione> existingConfig = configurazioneRepository.findByNomeConfigurazione(configurazioneRequestDto.getNomeConfigurazione());
        if (existingConfig.isPresent()) {
            logger.error("Esiste già una configurazione con questo nome: {}", configurazioneRequestDto.getNomeConfigurazione());
            return null;
        }

        Configurazione savedConfigurazione = configurazioneMapper.toEntityFromDtoRequest(configurazioneRequestDto);
        configurazioneRepository.save(savedConfigurazione);
        logger.info("Configurazione con ID {} inserita correttamente", savedConfigurazione.getIdConfigurazione());

        return configurazioneMapper.toDto(savedConfigurazione);
    }

    public ConfigurazioneResponseDto update(ConfigurazioneRequestDto configurazioneRequestDto, Integer id) {
        Optional<Configurazione> existingConfigOpt = configurazioneRepository.findById(id);
        
        if (!existingConfigOpt.isPresent()) {
            logger.error("Configurazione con ID {} non trovata per l'aggiornamento", id);
            return null;
        }

        Configurazione existingConfig = existingConfigOpt.get();
        existingConfig.setNomeConfigurazione(configurazioneRequestDto.getNomeConfigurazione());

        configurazioneRepository.save(existingConfig);

        logger.info("Configurazione con ID {} aggiornata correttamente", id);

        return configurazioneMapper.toDto(existingConfig);
    }
}