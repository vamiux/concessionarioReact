package com.stage.concessionario.model;

import jakarta.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Entity
@Table(name = "movimento")
public class Movimento implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int idMovimento;

    @ManyToOne
    @JoinColumn(name = "codice_fiscale_amministratore")
    private Amministratore amministratore;

    @ManyToOne
    @JoinColumn(name = "codice_fiscale_utente", nullable = false)
    private Utente utente;

    @ManyToOne
    @JoinColumn(name = "codice_fiscale_comproprietario")
    private Utente comproprietario;

    @Column(name = "has_comproprietario")
    private boolean hasComproprietario = false;

    @ManyToOne
    @JoinColumn(name = "numero_telaio", nullable = false)
    private Veicolo veicolo;

    @Column(name = "data_movimento", nullable = false)
    private Date dataMovimento;

    @Enumerated(EnumType.STRING)
    @Column(name = "tipo_movimento", nullable = false)
    private TipoMovimento tipoMovimento = TipoMovimento.VENDITA;

    @Column(name = "prezzo", nullable = false)
    private double prezzo;

    @Column(name = "prezzo_per_proprietario")
    private Double prezzoPerProprietario;

    public Movimento() {
    }

    public int getIdMovimento() {
        return idMovimento;
    }

    public void setIdMovimento(int idMovimento) {
        this.idMovimento = idMovimento;
    }

    public Utente getUtente() {
        return utente;
    }

    public void setUtente(Utente utente) {
        this.utente = utente;
    }

    public Utente getComproprietario() {
        return comproprietario;
    }

    public void setComproprietario(Utente comproprietario) {
        this.comproprietario = comproprietario;
    }

    public boolean isHasComproprietario() {
        return hasComproprietario;
    }

    public void setHasComproprietario(boolean hasComproprietario) {
        this.hasComproprietario = hasComproprietario;
    }

    public Veicolo getVeicolo() {
        return veicolo;
    }

    public void setVeicolo(Veicolo veicolo) {
        this.veicolo = veicolo;
    }

    public Date getDataMovimento() {
        return dataMovimento;
    }

    public void setDataMovimento(Date dataMovimento) {
        this.dataMovimento = dataMovimento;
    }

    public TipoMovimento getTipoMovimento() {
        return tipoMovimento;
    }

    public void setTipoMovimento(TipoMovimento tipoMovimento) {
        this.tipoMovimento = tipoMovimento;
    }

    public double getPrezzo() {
        return prezzo;
    }

    public void setPrezzo(double prezzo) {
        this.prezzo = prezzo;
    }

    public Double getPrezzoPerProprietario() {
        return prezzoPerProprietario;
    }

    public void setPrezzoPerProprietario(Double prezzoPerProprietario) {
        this.prezzoPerProprietario = prezzoPerProprietario;
    }

    public Amministratore getAmministratore() {
        return amministratore;
    }

    public void setAmministratore(Amministratore amministratore) {
        this.amministratore = amministratore;
    }
}