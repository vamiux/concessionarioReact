package com.stage.concessionario.repository;

import com.stage.concessionario.model.Amministratore;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AmministratoreRepository extends JpaRepository<Amministratore, String> {
    Optional<Amministratore> findByEmail(String email);
    
    // Utilizziamo l'email come username per l'autenticazione
    default Optional<Amministratore> findByUsername(String username) {
        return findByEmail(username);
    }
}
