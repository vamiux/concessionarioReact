package com.stage.concessionario.repository;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import com.stage.concessionario.model.Utente;

public interface UtenteRepository extends JpaRepository<Utente, String> {
    Optional<Utente> findByCodiceFiscaleUtente(String codiceFiscaleUtente);
    List<Utente> findByNomeContainingIgnoreCase(String nome);
    List<Utente> findByCognomeContainingIgnoreCase(String cognome);
    List<Utente> findByEmailContainingIgnoreCase(String email);
}
