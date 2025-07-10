package com.stage.concessionario.service;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;

import com.stage.concessionario.dto.MovimentoRequestDto;
import com.stage.concessionario.dto.MovimentoResponseDto;
import com.stage.concessionario.mapper.MovimentoMapper;
import com.stage.concessionario.model.Movimento;
import com.stage.concessionario.model.TipoMovimento;
import com.stage.concessionario.model.Utente;
import com.stage.concessionario.model.Veicolo;
import com.stage.concessionario.repository.MovimentoRepository;
import com.stage.concessionario.repository.UtenteRepository;
import com.stage.concessionario.repository.VeicoloRepository;

@Service
public class MovimentoService {

    private final MovimentoRepository movimentoRepository;
    private final UtenteRepository utenteRepository;
    private final VeicoloRepository veicoloRepository;
    private final MovimentoMapper movimentoMapper;
    private static final Logger logger = LogManager.getLogger(MovimentoService.class);

    public MovimentoService(MovimentoRepository movimentoRepository,
            UtenteRepository utenteRepository,
            VeicoloRepository veicoloRepository,
            MovimentoMapper movimentoMapper) {
        this.movimentoRepository = movimentoRepository;
        this.utenteRepository = utenteRepository;
        this.veicoloRepository = veicoloRepository;
        this.movimentoMapper = movimentoMapper;
    }

    public List<MovimentoResponseDto> getMovimenti() {
        List<Movimento> movimenti = movimentoRepository.findAll();
        logger.info("Lista movimenti visualizzata correttamente");
        return movimenti.stream().map(movimentoMapper::toDto).collect(Collectors.toList());
    }

    public MovimentoResponseDto getMovimentoById(Integer id) {
        Optional<Movimento> movimento = movimentoRepository.findById(id);
        if (movimento.isPresent()) {
            logger.info("Movimento con ID {} trovato correttamente", id);
            return movimentoMapper.toDto(movimento.get());
        } else {
            logger.error("Movimento con ID {} non esistente", id);
            return null;
        }
    }

    public List<MovimentoResponseDto> getMovimentiByUtente(String codiceFiscale) {
        List<Movimento> movimenti = movimentoRepository.findByUtente_CodiceFiscaleUtente(codiceFiscale);
        logger.info("Lista movimenti per utente {} visualizzata correttamente", codiceFiscale);
        return movimenti.stream().map(movimentoMapper::toDto).collect(Collectors.toList());
    }

    public List<MovimentoResponseDto> getMovimentiByVeicolo(String numeroTelaio) {
        List<Movimento> movimenti = movimentoRepository.findByVeicoloNumeroTelaio(numeroTelaio);
        logger.info("Lista movimenti per veicolo {} visualizzata correttamente", numeroTelaio);
        return movimenti.stream().map(movimentoMapper::toDto).collect(Collectors.toList());
    }
    
    public List<MovimentoResponseDto> searchMovimenti(
            TipoMovimento tipoMovimento, 
            String numeroTelaio, 
            String codiceFiscaleUtente, 
            Date dataInizio, 
            Date dataFine) {
        
        List<Movimento> movimenti = movimentoRepository.findAll();
        
        // Filtra per tipo movimento
        if (tipoMovimento != null) {
            movimenti = movimenti.stream()
                    .filter(m -> m.getTipoMovimento() == tipoMovimento)
                    .collect(Collectors.toList());
        }
        
        // Filtra per numero telaio
        if (numeroTelaio != null && !numeroTelaio.isEmpty()) {
            movimenti = movimenti.stream()
                    .filter(m -> m.getVeicolo().getNumeroTelaio().contains(numeroTelaio))
                    .collect(Collectors.toList());
        }
        
        // Filtra per codice fiscale utente
        if (codiceFiscaleUtente != null && !codiceFiscaleUtente.isEmpty()) {
            movimenti = movimenti.stream()
                    .filter(m -> m.getUtente().getCodiceFiscaleUtente().contains(codiceFiscaleUtente) ||
                            (m.isHasComproprietario() && m.getComproprietario() != null && 
                             m.getComproprietario().getCodiceFiscaleUtente().contains(codiceFiscaleUtente)))
                    .collect(Collectors.toList());
        }
        
        // Filtra per data inizio
        if (dataInizio != null) {
            movimenti = movimenti.stream()
                    .filter(m -> m.getDataMovimento().compareTo(dataInizio) >= 0)
                    .collect(Collectors.toList());
        }
        
        // Filtra per data fine
        if (dataFine != null) {
            movimenti = movimenti.stream()
                    .filter(m -> m.getDataMovimento().compareTo(dataFine) <= 0)
                    .collect(Collectors.toList());
        }
        
        logger.info("Ricerca movimenti completata: trovati {} risultati", movimenti.size());
        return movimenti.stream().map(movimentoMapper::toDto).collect(Collectors.toList());
    }

    public MovimentoResponseDto insert(MovimentoRequestDto movimentoRequestDto) {
        logger.debug("Tentativo di inserimento movimento: {}", movimentoRequestDto);

        // Verifica che l'utente esista
        Optional<Utente> utente = utenteRepository
                .findByCodiceFiscaleUtente(movimentoRequestDto.getCodiceFiscaleUtente());
        if (!utente.isPresent()) {
            logger.error("Utente con codice fiscale {} non esistente", movimentoRequestDto.getCodiceFiscaleUtente());
            return null;
        }

        // Verifica che il veicolo esista
        Optional<Veicolo> veicolo = veicoloRepository.findByNumeroTelaio(movimentoRequestDto.getNumeroTelaio());
        if (!veicolo.isPresent()) {
            logger.error("Veicolo con numero telaio {} non esistente", movimentoRequestDto.getNumeroTelaio());
            return null;
        }
        
        // Verifica che il comproprietario esista se presente
        Utente comproprietario = null;
        if (movimentoRequestDto.isHasComproprietario() && movimentoRequestDto.getCodiceFiscaleComproprietario() != null) {
            Optional<Utente> comproprietarioOpt = utenteRepository
                    .findByCodiceFiscaleUtente(movimentoRequestDto.getCodiceFiscaleComproprietario());
            if (!comproprietarioOpt.isPresent()) {
                logger.error("Comproprietario con codice fiscale {} non esistente", 
                        movimentoRequestDto.getCodiceFiscaleComproprietario());
                return null;
            }
            comproprietario = comproprietarioOpt.get();
            
            // Verifica che il proprietario e il comproprietario siano diversi
            if (movimentoRequestDto.getCodiceFiscaleUtente().equals(movimentoRequestDto.getCodiceFiscaleComproprietario())) {
                logger.error("Il proprietario e il comproprietario non possono essere la stessa persona");
                return null;
            }
        }

        Movimento movimento = new Movimento();
        movimento.setUtente(utente.get());
        movimento.setVeicolo(veicolo.get());
        movimento.setDataMovimento(
                movimentoRequestDto.getDataMovimento() != null ? movimentoRequestDto.getDataMovimento() : new Date());
        movimento.setTipoMovimento(
                movimentoRequestDto.getTipoMovimento() != null ? movimentoRequestDto.getTipoMovimento()
                        : TipoMovimento.VENDITA);
        movimento.setPrezzo(movimentoRequestDto.getPrezzo());
        
        // Gestione comproprietà
        movimento.setHasComproprietario(movimentoRequestDto.isHasComproprietario());
        if (movimento.isHasComproprietario() && comproprietario != null) {
            movimento.setComproprietario(comproprietario);
            // Calcola il prezzo per proprietario (50% ciascuno)
            double prezzoPerProprietario = movimento.getPrezzo() / 2;
            movimento.setPrezzoPerProprietario(prezzoPerProprietario);
        }

        movimentoRepository.save(movimento);
        
        // Aggiorna la disponibilità del veicolo in base al tipo di movimento
        Veicolo veicoloToUpdate = veicolo.get();
        if (movimento.getTipoMovimento() == TipoMovimento.VENDITA) {
            veicoloToUpdate.setDisponibile(false);
            logger.info("Disponibilità del veicolo con numero telaio {} aggiornata a false (vendita)", veicoloToUpdate.getNumeroTelaio());
        } else if (movimento.getTipoMovimento() == TipoMovimento.ACQUISTO) {
            veicoloToUpdate.setDisponibile(true);
            logger.info("Disponibilità del veicolo con numero telaio {} aggiornata a true (acquisto)", veicoloToUpdate.getNumeroTelaio());
        }
        veicoloRepository.save(veicoloToUpdate);

        logger.info("Movimento inserito correttamente con ID: {}", movimento.getIdMovimento());

        return movimentoMapper.toDto(movimento);
    }

    public MovimentoResponseDto update(MovimentoRequestDto movimentoRequestDto, Integer id) {
        Optional<Movimento> existingMovimentoOpt = movimentoRepository.findById(id);

        if (!existingMovimentoOpt.isPresent()) {
            logger.error("Movimento con ID {} non trovato per l'aggiornamento", id);
            return null;
        }

        Movimento existingMovimento = existingMovimentoOpt.get();

        // Verifica che l'utente esista se è stato cambiato
        if (!movimentoRequestDto.getCodiceFiscaleUtente()
                .equals(existingMovimento.getUtente().getCodiceFiscaleUtente())) {
            Optional<Utente> utente = utenteRepository
                    .findByCodiceFiscaleUtente(movimentoRequestDto.getCodiceFiscaleUtente());
            if (!utente.isPresent()) {
                logger.error("Utente con codice fiscale {} non esistente",
                        movimentoRequestDto.getCodiceFiscaleUtente());
                return null;
            }
            existingMovimento.setUtente(utente.get());
        }

        // Verifica che il veicolo esista se è stato cambiato
        if (!movimentoRequestDto.getNumeroTelaio().equals(existingMovimento.getVeicolo().getNumeroTelaio())) {
            Optional<Veicolo> veicolo = veicoloRepository.findByNumeroTelaio(movimentoRequestDto.getNumeroTelaio());
            if (!veicolo.isPresent()) {
                logger.error("Veicolo con numero telaio {} non esistente", movimentoRequestDto.getNumeroTelaio());
                return null;
            }
            existingMovimento.setVeicolo(veicolo.get());
        }
        
        // Gestione comproprietà
        existingMovimento.setHasComproprietario(movimentoRequestDto.isHasComproprietario());
        
        if (movimentoRequestDto.isHasComproprietario()) {
            // Verifica che il comproprietario esista se è stato cambiato
            String currentComproprietarioCodiceFiscale = existingMovimento.getComproprietario() != null ? 
                    existingMovimento.getComproprietario().getCodiceFiscaleUtente() : null;
            
            if (movimentoRequestDto.getCodiceFiscaleComproprietario() != null && 
                    (currentComproprietarioCodiceFiscale == null || 
                     !movimentoRequestDto.getCodiceFiscaleComproprietario().equals(currentComproprietarioCodiceFiscale))) {
                
                Optional<Utente> comproprietario = utenteRepository
                        .findByCodiceFiscaleUtente(movimentoRequestDto.getCodiceFiscaleComproprietario());
                if (!comproprietario.isPresent()) {
                    logger.error("Comproprietario con codice fiscale {} non esistente",
                            movimentoRequestDto.getCodiceFiscaleComproprietario());
                    return null;
                }
                
                // Verifica che il proprietario e il comproprietario siano diversi
                if (existingMovimento.getUtente().getCodiceFiscaleUtente().equals(
                        movimentoRequestDto.getCodiceFiscaleComproprietario())) {
                    logger.error("Il proprietario e il comproprietario non possono essere la stessa persona");
                    return null;
                }
                
                existingMovimento.setComproprietario(comproprietario.get());
            }
            
            // Aggiorna il prezzo per proprietario (50% ciascuno)
            double prezzoPerProprietario = movimentoRequestDto.getPrezzo() / 2;
            existingMovimento.setPrezzoPerProprietario(prezzoPerProprietario);
        } else {
            // Rimuovi il comproprietario se non c'è più comproprietà
            existingMovimento.setComproprietario(null);
            existingMovimento.setPrezzoPerProprietario(null);
        }

        existingMovimento.setDataMovimento(movimentoRequestDto.getDataMovimento());
        existingMovimento.setTipoMovimento(movimentoRequestDto.getTipoMovimento());
        existingMovimento.setPrezzo(movimentoRequestDto.getPrezzo());

        // Aggiorna la disponibilità del veicolo in base al tipo di movimento
        Veicolo veicoloToUpdate = existingMovimento.getVeicolo();
        if (existingMovimento.getTipoMovimento() == TipoMovimento.VENDITA) {
            veicoloToUpdate.setDisponibile(false);
            logger.info("Disponibilità del veicolo con numero telaio {} aggiornata a false (vendita)", veicoloToUpdate.getNumeroTelaio());
        } else if (existingMovimento.getTipoMovimento() == TipoMovimento.ACQUISTO) {
            veicoloToUpdate.setDisponibile(true);
            logger.info("Disponibilità del veicolo con numero telaio {} aggiornata a true (acquisto)", veicoloToUpdate.getNumeroTelaio());
        }
        veicoloRepository.save(veicoloToUpdate);

        movimentoRepository.save(existingMovimento);
        logger.info("Movimento con ID {} aggiornato correttamente", id);

        return movimentoMapper.toDto(existingMovimento);
    }
}