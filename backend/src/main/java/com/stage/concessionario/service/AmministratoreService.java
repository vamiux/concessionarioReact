package com.stage.concessionario.service;

import com.stage.concessionario.dto.AmministratoreRequestDTO;
import com.stage.concessionario.dto.AmministratoreResponseDTO;
import com.stage.concessionario.mapper.AmministratoreMapper;
import com.stage.concessionario.model.Amministratore;
import com.stage.concessionario.repository.AmministratoreRepository;
import com.stage.concessionario.security.AmministratoreSecurityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class AmministratoreService {

    private final AmministratoreRepository amministratoreRepository;
    private final AmministratoreMapper amministratoreMapper;
    private final AmministratoreSecurityService securityService;

    @Autowired
    public AmministratoreService(AmministratoreRepository amministratoreRepository, 
                                AmministratoreMapper amministratoreMapper,
                                AmministratoreSecurityService securityService) {
        this.amministratoreRepository = amministratoreRepository;
        this.amministratoreMapper = amministratoreMapper;
        this.securityService = securityService;
    }

    public List<AmministratoreResponseDTO> getAllAmministratori() {
        return amministratoreRepository.findAll().stream()
                .map(amministratoreMapper::toDto)
                .collect(Collectors.toList());
    }

    public AmministratoreResponseDTO getAmministratoreByCodiceFiscale(String codiceFiscale) {
        return amministratoreRepository.findById(codiceFiscale)
                .map(amministratoreMapper::toDto)
                .orElse(null);
    }

    public AmministratoreResponseDTO getAmministratoreByEmail(String email) {
        return amministratoreRepository.findByEmail(email)
                .map(amministratoreMapper::toDto)
                .orElse(null);
    }

    @Transactional
    public AmministratoreResponseDTO createAmministratore(AmministratoreRequestDTO amministratoreDTO) {
        Amministratore amministratore = amministratoreMapper.toEntityFromDtoRequest(amministratoreDTO);
        Amministratore savedAmministratore = amministratoreRepository.save(amministratore);
        return amministratoreMapper.toDto(savedAmministratore);
    }

    @Transactional
    public AmministratoreResponseDTO updateAmministratore(String codiceFiscale, AmministratoreRequestDTO amministratoreDTO) {
        Optional<Amministratore> amministratoreOptional = amministratoreRepository.findById(codiceFiscale);
        
        if (amministratoreOptional.isPresent()) {
            Amministratore amministratore = amministratoreOptional.get();
            amministratoreMapper.updateAmministratoreFromDtoInput(amministratoreDTO, amministratore);
            Amministratore updatedAmministratore = amministratoreRepository.save(amministratore);
            return amministratoreMapper.toDto(updatedAmministratore);
        }
        
        return null;
    }

    @Transactional
    public boolean deleteAmministratore(String codiceFiscale) {
        if (amministratoreRepository.existsById(codiceFiscale)) {
            amministratoreRepository.deleteById(codiceFiscale);
            return true;
        }
        return false;
    }

    /**
     * Verifica le credenziali di un amministratore
     * @param email Email dell'amministratore
     * @param password Password in chiaro
     * @return true se le credenziali sono valide, false altrimenti
     */
    public boolean verificaCredenziali(String email, String password) {
        return securityService.verificaCredenziali(email, password);
    }
}
