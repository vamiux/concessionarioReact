package com.stage.concessionario.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import java.io.Serializable;
import java.util.List;

@Entity
@Table(name = "configurazione")
public class Configurazione implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int idConfigurazione;

    @Column(name = "nome_configurazione", nullable = false)
    @Size(min = 1, max = 100)
    private String nomeConfigurazione;

    @OneToMany(mappedBy = "configurazione")
    private List<Veicolo> veicoli;

    public Configurazione() {}

    public int getIdConfigurazione() {
        return idConfigurazione;
    }

    public void setIdConfigurazione(int idConfigurazione) {
        this.idConfigurazione = idConfigurazione;
    }

    public String getNomeConfigurazione() {
        return nomeConfigurazione;
    }

    public void setNomeConfigurazione(String nomeConfigurazione) {
        this.nomeConfigurazione = nomeConfigurazione;
    }

    public List<Veicolo> getVeicoli() {
        return veicoli;
    }

    public void setVeicoli(List<Veicolo> veicoli) {
        this.veicoli = veicoli;
    }
}