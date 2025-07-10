package com.stage.concessionario.service;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Service
public class DatabaseService {

    private final JdbcTemplate jdbcTemplate;
    private static final Logger logger = LogManager.getLogger(DatabaseService.class);

    public DatabaseService(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Transactional
    public void resetSequence(String tableName) {
        // Verifica se ci sono record nella tabella
        Integer count = jdbcTemplate.queryForObject(
                "SELECT COUNT(*) FROM concessionario." + tableName, Integer.class);
        
        if (count == null || count == 0) {
            // Se la tabella è vuota, reimposta l'auto_increment a 1
            jdbcTemplate.execute("ALTER TABLE concessionario." + tableName + " AUTO_INCREMENT = 1");
        } else {
            // Se ci sono record, reimposta l'auto_increment al valore massimo + 1
            Integer maxId = jdbcTemplate.queryForObject(
                    "SELECT MAX(id_" + tableName + ") FROM concessionario." + tableName, Integer.class);
            
            if (maxId != null) {
                jdbcTemplate.execute("ALTER TABLE concessionario." + tableName + " AUTO_INCREMENT = " + (maxId + 1));
            }
        }
    }
    
    @Transactional
    public void createTriggerForMovimentiDelete() {
        try {
            // Elimina il trigger se esiste già
            jdbcTemplate.execute("DROP TRIGGER IF EXISTS concessionario.after_movimento_delete");
            
            // Crea il trigger per aggiornare la disponibilità del veicolo dopo l'eliminazione di un movimento
            String triggerSql = 
                "CREATE TRIGGER concessionario.after_movimento_delete " +
                "AFTER DELETE ON concessionario.movimento " +
                "FOR EACH ROW " +
                "BEGIN " +
                "    UPDATE concessionario.veicolo " +
                "    SET disponibile = true " +
                "    WHERE numero_telaio = OLD.numero_telaio; " +
                "END;";
            
            jdbcTemplate.execute(triggerSql);
            logger.info("Trigger after_movimento_delete creato con successo");
        } catch (Exception e) {
            logger.error("Errore nella creazione del trigger: {}", e.getMessage());
            throw e;
        }
    }
}
