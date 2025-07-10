package com.stage.concessionario.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.stage.concessionario.model.Movimento;

@Repository
public interface MovimentoRepository extends JpaRepository<Movimento, Integer> {
    
    // Trova movimenti per l'utente in base al codice fiscale
    List<Movimento> findByUtente_CodiceFiscaleUtente(String codiceFiscaleUtente);
    
    // Trova movimenti per il veicolo in base al numero telaio
    List<Movimento> findByVeicoloNumeroTelaio(String numeroTelaio);
    
    // List<Movimento> findByTipoMovimento(TipoMovimento tipoMovimento);
    
    // Trova movimenti per utente e tipo
    List<Movimento> findByUtenteCodiceFiscaleUtenteAndTipoMovimento(String codiceFiscaleUtente, String tipoMovimento);
    
    // Trova movimenti per veicolo e tipo
    List<Movimento> findByVeicoloNumeroTelaioAndTipoMovimento(String numeroTelaio, String tipoMovimento);
    
    // Trova l'ultimo movimento per un veicolo specifico
    Optional<Movimento> findByVeicolo_NumeroTelaioOrderByDataMovimentoDesc(String numeroTelaio);
}