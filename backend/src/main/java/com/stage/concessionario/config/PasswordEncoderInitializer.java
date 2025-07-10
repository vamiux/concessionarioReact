package com.stage.concessionario.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.stage.concessionario.model.Amministratore;
import com.stage.concessionario.repository.AmministratoreRepository;

import java.util.List;

/**
 * Componente che si occupa di codificare automaticamente le password in chiaro
 * degli amministratori nel database all'avvio dell'applicazione.
 */
@Component
public class PasswordEncoderInitializer implements CommandLineRunner {

    private final AmministratoreRepository amministratoreRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public PasswordEncoderInitializer(AmministratoreRepository amministratoreRepository, PasswordEncoder passwordEncoder) {
        this.amministratoreRepository = amministratoreRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    @Transactional
    public void run(String... args) throws Exception {
        System.out.println("Verifica e codifica delle password degli amministratori...");
        
        // Recupera tutti gli amministratori dal database
        List<Amministratore> amministratori = amministratoreRepository.findAll();
        int passwordCodificate = 0;
        
        for (Amministratore amministratore : amministratori) {
            if (amministratore.getPassword() != null && !isPasswordEncoded(amministratore.getPassword())) {
                // La password è in chiaro, codificala
                String passwordInChiaro = amministratore.getPassword();
                String passwordCodificata = passwordEncoder.encode(passwordInChiaro);
                amministratore.setPassword(passwordCodificata);
                amministratoreRepository.save(amministratore);
                passwordCodificate++;
                
                System.out.println("Password codificata per l'amministratore: " + amministratore.getEmail());
            }
        }
        
        if (passwordCodificate > 0) {
            System.out.println("Codificate " + passwordCodificate + " password di amministratori.");
        } else {
            System.out.println("Nessuna password da codificare trovata.");
        }
    }
    
    /**
     * Verifica se una password è già codificata.
     * Le password codificate con BCrypt iniziano con "$2a$", "$2b$" o "$2y$".
     * 
     * @param password La password da verificare
     * @return true se la password è già codificata, false altrimenti
     */
    private boolean isPasswordEncoded(String password) {
        return password.startsWith("$2a$") || password.startsWith("$2b$") || password.startsWith("$2y$");
    }
}
