package com.stage.concessionario.security;

import com.stage.concessionario.model.Amministratore;
import com.stage.concessionario.repository.AmministratoreRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AmministratoreSecurityService {

    private final AmministratoreRepository amministratoreRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public AmministratoreSecurityService(AmministratoreRepository amministratoreRepository, PasswordEncoder passwordEncoder) {
        this.amministratoreRepository = amministratoreRepository;
        this.passwordEncoder = passwordEncoder;
    }

    /**
     * Verifica le credenziali di un amministratore
     * @param email Email dell'amministratore
     * @param password Password in chiaro
     * @return true se le credenziali sono valide, false altrimenti
     */
    public boolean verificaCredenziali(String email, String password) {
        Optional<Amministratore> amministratoreOpt = amministratoreRepository.findByEmail(email);
        
        if (amministratoreOpt.isEmpty()) {
            return false;
        }
        
        Amministratore amministratore = amministratoreOpt.get();
        return passwordEncoder.matches(password, amministratore.getPassword());
    }
    
    /**
     * Codifica la password di un amministratore
     * @param amministratore Amministratore di cui codificare la password
     */
    public void codificaPassword(Amministratore amministratore) {
        if (amministratore != null && amministratore.getPassword() != null) {
            String passwordCodificata = passwordEncoder.encode(amministratore.getPassword());
            amministratore.setPassword(passwordCodificata);
        }
    }
}
