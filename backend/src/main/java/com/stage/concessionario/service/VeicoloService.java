package com.stage.concessionario.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;

import com.stage.concessionario.dto.VeicoloRequestDto;
import com.stage.concessionario.dto.VeicoloResponseDto;
import com.stage.concessionario.dto.VeicoloUpdateDto;
import com.stage.concessionario.mapper.VeicoloMapper;
import com.stage.concessionario.model.Configurazione;
import com.stage.concessionario.model.Veicolo;
import com.stage.concessionario.repository.ConfigurazioneRepository;
import com.stage.concessionario.repository.VeicoloRepository;

@Service
public class VeicoloService {

    private final VeicoloRepository veicoloRepository;
    private final ConfigurazioneRepository configurazioneRepository;
    private final VeicoloMapper veicoloMapper;
    private static final Logger logger = LogManager.getLogger(VeicoloService.class);

    public VeicoloService(VeicoloRepository veicoloRepository, ConfigurazioneRepository configurazioneRepository, VeicoloMapper veicoloMapper) {
        this.veicoloRepository = veicoloRepository;
        this.configurazioneRepository = configurazioneRepository;
        this.veicoloMapper = veicoloMapper;
    }

    public List<VeicoloResponseDto> getVeicoli() {
        List<Veicolo> veicoli = veicoloRepository.findAll();
        logger.info("Lista veicoli visualizzata correttamente");
        return veicoli.stream().map(veicoloMapper::toDto).collect(Collectors.toList());
    }
    
    public List<VeicoloResponseDto> getVeicoliDisponibili() {
        List<Veicolo> veicoli = veicoloRepository.findByDisponibileTrue();
        logger.info("Lista veicoli disponibili visualizzata correttamente");
        return veicoli.stream().map(veicoloMapper::toDto).collect(Collectors.toList());
    }

    public VeicoloResponseDto getVeicoloByNumeroTelaio(String numeroTelaio) {
        Optional<Veicolo> veicolo = veicoloRepository.findByNumeroTelaio(numeroTelaio);
        if (veicolo.isPresent()) {
            logger.info("Veicolo con numero telaio {} trovato correttamente", numeroTelaio);
            return veicoloMapper.toDto(veicolo.get());
        } else {
            logger.error("Veicolo con numero telaio {} non esistente", numeroTelaio);
            return null;
        }
    }

    public List<VeicoloResponseDto> searchVeicoli(String numeroTelaio, String marca, String modello) {
        List<Veicolo> veicoli;
        
        if (numeroTelaio != null && !numeroTelaio.isEmpty()) {
            veicoli = veicoloRepository.findByNumeroTelaioContaining(numeroTelaio);
            logger.info("Ricerca veicoli per numero telaio {}", numeroTelaio);
            
            // Filtriamo ulteriormente per marca e modello se specificati
            if (marca != null && !marca.isEmpty()) {
                veicoli = veicoli.stream()
                    .filter(v -> v.getMarca().toLowerCase().contains(marca.toLowerCase()))
                    .collect(Collectors.toList());
                logger.info("Filtro applicato per marca {}", marca);
            }
            
            if (modello != null && !modello.isEmpty()) {
                veicoli = veicoli.stream()
                    .filter(v -> v.getModello().toLowerCase().contains(modello.toLowerCase()))
                    .collect(Collectors.toList());
                logger.info("Filtro applicato per modello {}", modello);
            }
        } else if (marca != null && !marca.isEmpty() && modello != null && !modello.isEmpty()) {
            veicoli = veicoloRepository.findByMarcaAndModello(marca, modello);
            logger.info("Ricerca veicoli per marca {} e modello {}", marca, modello);
        } else if (marca != null && !marca.isEmpty()) {
            veicoli = veicoloRepository.findByMarca(marca);
            logger.info("Ricerca veicoli per marca {}", marca);
        } else if (modello != null && !modello.isEmpty()) {
            veicoli = veicoloRepository.findByModello(modello);
            logger.info("Ricerca veicoli per modello {}", modello);
        } else {
            veicoli = veicoloRepository.findAll();
            logger.info("Nessun criterio di ricerca specificato, restituiti tutti i veicoli");
        }
        
        return veicoli.stream().map(veicoloMapper::toDto).collect(Collectors.toList());
    }

    public VeicoloResponseDto insert(VeicoloRequestDto veicoloRequestDto) {
        logger.debug("Tentativo di inserimento veicolo: {}", veicoloRequestDto.getNumeroTelaio());

        Optional<Veicolo> existingVeicolo = veicoloRepository.findByNumeroTelaio(veicoloRequestDto.getNumeroTelaio());
        if (existingVeicolo.isPresent()) {
            logger.error("Esiste già un veicolo con questo numero telaio: {}", veicoloRequestDto.getNumeroTelaio());
            return null;
        }

        Veicolo veicolo = veicoloMapper.toEntityFromDtoRequest(veicoloRequestDto);
        
        // Gestione dell'assegnazione della configurazione
        Optional<Configurazione> configurazione = configurazioneRepository.findById(1);
        if (configurazione.isEmpty()) {
            // Se non esiste una configurazione con ID 1, creiamo una configurazione di default
            Configurazione nuovaConfigurazione = new Configurazione();
            nuovaConfigurazione.setNomeConfigurazione("Configurazione Standard");
            configurazione = Optional.of(configurazioneRepository.save(nuovaConfigurazione));
            logger.info("Creata nuova configurazione di default con ID: {}", nuovaConfigurazione.getIdConfigurazione());
        }
        
        veicolo.setConfigurazione(configurazione.get());
        veicoloRepository.save(veicolo);
        logger.info("Veicolo con numero telaio {} inserito correttamente", veicolo.getNumeroTelaio());

        return veicoloMapper.toDto(veicolo);
    }

    public VeicoloResponseDto update(VeicoloUpdateDto veicoloUpdateDto, String numeroTelaio) {
        Optional<Veicolo> existingVeicoloOpt = veicoloRepository.findByNumeroTelaio(numeroTelaio);
        
        if (!existingVeicoloOpt.isPresent()) {
            logger.error("Veicolo con numero telaio {} non trovato per l'aggiornamento", numeroTelaio);
            return null;
        }

        Veicolo existingVeicolo = existingVeicoloOpt.get();
        veicoloMapper.updateVeicoloFromDtoRequest(veicoloUpdateDto, existingVeicolo);

        veicoloRepository.save(existingVeicolo);

        logger.info("Veicolo con numero telaio {} aggiornato correttamente", numeroTelaio);

        return veicoloMapper.toDto(existingVeicolo);
    }
    
    public boolean delete(String numeroTelaio) {
        Optional<Veicolo> existingVeicolo = veicoloRepository.findByNumeroTelaio(numeroTelaio);
        
        if (!existingVeicolo.isPresent()) {
            logger.error("Veicolo con numero telaio {} non trovato per l'eliminazione", numeroTelaio);
            return false;
        }
        
        veicoloRepository.delete(existingVeicolo.get());
        logger.info("Veicolo con numero telaio {} eliminato correttamente", numeroTelaio);
        
        return true;
    }
}