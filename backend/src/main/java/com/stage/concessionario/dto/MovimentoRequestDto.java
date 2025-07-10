package com.stage.concessionario.dto;

import java.util.Date;
import com.stage.concessionario.model.TipoMovimento;

public class MovimentoRequestDto {
    private String codiceFiscaleUtente;
    private String numeroTelaio;
    private Date dataMovimento;
    private TipoMovimento tipoMovimento = TipoMovimento.VENDITA;
    private double prezzo;
    private boolean hasComproprietario = false;
    private String codiceFiscaleComproprietario;
    private Double prezzoPerProprietario;

    public MovimentoRequestDto() {}

    public String getCodiceFiscaleUtente() {
        return codiceFiscaleUtente;
    }

    public void setCodiceFiscaleUtente(String codiceFiscaleUtente) {
        this.codiceFiscaleUtente = codiceFiscaleUtente;
    }

    public String getNumeroTelaio() {
        return numeroTelaio;
    }

    public void setNumeroTelaio(String numeroTelaio) {
        this.numeroTelaio = numeroTelaio;
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

    public Double getPrezzoPerProprietario() {
        return prezzoPerProprietario;
    }

    public void setPrezzoPerProprietario(Double prezzoPerProprietario) {
        this.prezzoPerProprietario = prezzoPerProprietario;
    }
}
