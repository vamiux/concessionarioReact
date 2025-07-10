package com.stage.concessionario.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import java.io.Serializable;
import java.util.List;

@Entity
@Table(name = "veicolo")
public class Veicolo implements Serializable {

    @Id
    @Column(name = "numero_telaio", nullable = false)
    private String numeroTelaio;

    @Column(name = "marca", nullable = false)
    @Size(min = 1, max = 50)
    private String marca;

    @Column(name = "modello", nullable = false)
    @Size(min = 1, max = 50)
    private String modello;

    @Column(name = "anno_immatricolazione", nullable = false)
    private int annoImmatricolazione;

    @Column(name = "chilometraggio", nullable = false)
    private int chilometraggio;

    @Column(name = "disponibile", nullable = false)
    private boolean disponibile = true;

    @ManyToOne
    @JoinColumn(name = "id_configurazione", nullable = false)
    private Configurazione configurazione;

    @OneToMany(mappedBy = "veicolo")
    private List<Movimento> movimenti;

    public Veicolo() {}

    public String getNumeroTelaio() {
        return numeroTelaio;
    }

    public void setNumeroTelaio(String numeroTelaio) {
        this.numeroTelaio = numeroTelaio;
    }

    public String getMarca() {
        return marca;
    }

    public void setMarca(String marca) {
        this.marca = marca;
    }

    public String getModello() {
        return modello;
    }

    public void setModello(String modello) {
        this.modello = modello;
    }

    public int getAnnoImmatricolazione() {
        return annoImmatricolazione;
    }

    public void setAnnoImmatricolazione(int annoImmatricolazione) {
        this.annoImmatricolazione = annoImmatricolazione;
    }

    public int getChilometraggio() {
        return chilometraggio;
    }

    public void setChilometraggio(int chilometraggio) {
        this.chilometraggio = chilometraggio;
    }

    public boolean isDisponibile() {
        return disponibile;
    }

    public void setDisponibile(boolean disponibile) {
        this.disponibile = disponibile;
    }

    public Configurazione getConfigurazione() {
        return configurazione;
    }

    public void setConfigurazione(Configurazione configurazione) {
        this.configurazione = configurazione;
    }
}