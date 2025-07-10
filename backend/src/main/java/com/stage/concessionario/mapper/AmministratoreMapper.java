package com.stage.concessionario.mapper;

import com.stage.concessionario.dto.AmministratoreRequestDTO;
import com.stage.concessionario.dto.AmministratoreResponseDTO;
import com.stage.concessionario.model.Amministratore;
import com.stage.concessionario.security.AmministratoreSecurityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class AmministratoreMapper {

    private final AmministratoreSecurityService securityService;

    @Autowired
    public AmministratoreMapper(AmministratoreSecurityService securityService) {
        this.securityService = securityService;
    }

    public AmministratoreResponseDTO toDto(Amministratore amministratore) {
        if (amministratore == null) {
            return null;
        }

        AmministratoreResponseDTO dto = new AmministratoreResponseDTO();
        dto.setCodiceFiscaleAmministratore(amministratore.getCodiceFiscaleAmministratore());
        dto.setNome(amministratore.getNome());
        dto.setCognome(amministratore.getCognome());
        dto.setTelefono(amministratore.getTelefono());
        dto.setEmail(amministratore.getEmail());
        return dto;
    }

    public Amministratore toEntityFromDtoRequest(AmministratoreRequestDTO dto) {
        if (dto == null) {
            return null;
        }

        Amministratore amministratore = new Amministratore();
        amministratore.setCodiceFiscaleAmministratore(dto.getCodiceFiscaleAmministratore());
        amministratore.setNome(dto.getNome());
        amministratore.setCognome(dto.getCognome());
        amministratore.setTelefono(dto.getTelefono());
        amministratore.setEmail(dto.getEmail());
        amministratore.setPassword(dto.getPassword());
        
        // Codifica la password prima di salvare l'entità
        securityService.codificaPassword(amministratore);
        
        return amministratore;
    }

    public void updateAmministratoreFromDtoInput(AmministratoreRequestDTO dto, Amministratore amministratore) {
        if (dto == null) {
            return;
        }

        amministratore.setCodiceFiscaleAmministratore(dto.getCodiceFiscaleAmministratore());
        amministratore.setNome(dto.getNome());
        amministratore.setCognome(dto.getCognome());
        amministratore.setTelefono(dto.getTelefono());
        amministratore.setEmail(dto.getEmail());
        
        // Aggiorna la password solo se è stata fornita una nuova
        if (dto.getPassword() != null && !dto.getPassword().isEmpty()) {
            amministratore.setPassword(dto.getPassword());
            securityService.codificaPassword(amministratore);
        }
    }
}
