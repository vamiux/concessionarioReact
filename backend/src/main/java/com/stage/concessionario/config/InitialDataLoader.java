package com.stage.concessionario.config;

import com.stage.concessionario.model.Amministratore;
import com.stage.concessionario.repository.AmministratoreRepository;
import com.stage.concessionario.security.AmministratoreSecurityService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class InitialDataLoader {

    /**
     * Crea un amministratore di default se non ne esiste già uno nel database.
     * Questo permette di avere un account amministratore predefinito per il primo accesso.
     */
    @Bean
    public CommandLineRunner initializeDefaultAdmin(
            AmministratoreRepository amministratoreRepository,
            AmministratoreSecurityService securityService) {
        
        return args -> {
            // Commentato per utilizzare gli amministratori inseriti manualmente nel database
            /*
            // Verifica se esiste già almeno un amministratore
            if (amministratoreRepository.count() == 0) {
                System.out.println("Creazione amministratore predefinito...");
                
                // Crea un nuovo amministratore con credenziali predefinite
                Amministratore admin = new Amministratore();
                admin.setCodiceFiscaleAmministratore("ADMIN0000000XXXX");
                admin.setNome("Admin");
                admin.setCognome("Default");
                admin.setEmail("admin@concessionario.com");
                admin.setPassword("password"); // Sarà codificata automaticamente dal listener
                admin.setRuolo("ADMIN");
                
                // Salva l'amministratore nel database
                amministratoreRepository.save(admin);
                
                System.out.println("Amministratore predefinito creato con successo.");
                System.out.println("Email: admin@concessionario.com");
                System.out.println("Password: password");
                System.out.println("Si consiglia di modificare queste credenziali dopo il primo accesso.");
            }
            */
            
            // Stampa un messaggio informativo
            System.out.println("Utilizzo degli amministratori configurati nel database.");
        };
    }
}
