package com.stage.concessionario.dto;

import java.util.Date;
import com.stage.concessionario.model.TipoMovimento;

public class MovimentoResponseDto {
    private int idMovimento;
    private String codiceFiscaleUtente;
    private String nomeUtente;
    private String cognomeUtente;
    private String numeroTelaio;
    private String marcaVeicolo;
    private String modelloVeicolo;
    private Date dataMovimento;
    private TipoMovimento tipoMovimento;
    private double prezzo;
    private boolean hasComproprietario;
    private String codiceFiscaleComproprietario;
    private String nomeComproprietario;
    private String cognomeComproprietario;
    private Double prezzoPerProprietario;

    public MovimentoResponseDto() {}

    public int getIdMovimento() {
        return idMovimento;
    }

    public void setIdMovimento(int idMovimento) {
        this.idMovimento = idMovimento;
    }

    public String getCodiceFiscaleUtente() {
        return codiceFiscaleUtente;
    }

    public void setCodiceFiscaleUtente(String codiceFiscaleUtente) {
        this.codiceFiscaleUtente = codiceFiscaleUtente;
    }
    
    public String getNomeUtente() {
        return nomeUtente;
    }

    public void setNomeUtente(String nomeUtente) {
        this.nomeUtente = nomeUtente;
    }

    public String getCognomeUtente() {
        return cognomeUtente;
    }

    public void setCognomeUtente(String cognomeUtente) {
        this.cognomeUtente = cognomeUtente;
    }

    public String getNumeroTelaio() {
        return numeroTelaio;
    }

    public void setNumeroTelaio(String numeroTelaio) {
        this.numeroTelaio = numeroTelaio;
    }
    
    public String getMarcaVeicolo() {
        return marcaVeicolo;
    }

    public void setMarcaVeicolo(String marcaVeicolo) {
        this.marcaVeicolo = marcaVeicolo;
    }

    public String getModelloVeicolo() {
        return modelloVeicolo;
    }

    public void setModelloVeicolo(String modelloVeicolo) {
        this.modelloVeicolo = modelloVeicolo;
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

    public boolean isHasComproprietario() {
        return hasComproprietario;
    }

    public void setHasComproprietario(boolean hasComproprietario) {
        this.hasComproprietario = hasComproprietario;
    }

    public String getCodiceFiscaleComproprietario() {
        return codiceFiscaleComproprietario;
    }

    public void setCodiceFiscaleComproprietario(String codiceFiscaleComproprietario) {
        this.codiceFiscaleComproprietario = codiceFiscaleComproprietario;
    }

    public String getNomeComproprietario() {
        return nomeComproprietario;
    }

    public void setNomeComproprietario(String nomeComproprietario) {
        this.nomeComproprietario = nomeComproprietario;
    }

    public String getCognomeComproprietario() {
        return cognomeComproprietario;
    }

    public void setCognomeComproprietario(String cognomeComproprietario) {
        this.cognomeComproprietario = cognomeComproprietario;
    }

    public Double getPrezzoPerProprietario() {
        return prezzoPerProprietario;
    }

    public void setPrezzoPerProprietario(Double prezzoPerProprietario) {
        this.prezzoPerProprietario = prezzoPerProprietario;
    }
}
