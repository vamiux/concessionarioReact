package com.stage.concessionario.repository;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import com.stage.concessionario.model.Veicolo;

public interface VeicoloRepository extends JpaRepository<Veicolo, String> {
    List<Veicolo> findByMarcaAndModello(String marca, String modello);
    List<Veicolo> findByMarca(String marca);
    List<Veicolo> findByModello(String modello);
    Optional<Veicolo> findByNumeroTelaio(String numeroTelaio);
    List<Veicolo> findByNumeroTelaioContaining(String numeroTelaio);
    List<Veicolo> findByDisponibileTrue();
}
