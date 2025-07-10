package com.stage.concessionario.repository;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import com.stage.concessionario.model.Configurazione;

public interface ConfigurazioneRepository extends JpaRepository<Configurazione, Integer> {
    Optional<Configurazione> findByNomeConfigurazione(String nomeConfigurazione);
}
