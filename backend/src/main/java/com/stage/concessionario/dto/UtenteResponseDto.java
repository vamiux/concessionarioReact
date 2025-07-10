package com.stage.concessionario.dto;

import java.util.Date;

public class UtenteResponseDto {
    private String codiceFiscaleUtente;
    private String nome;
    private String cognome;
    private Date dataNascita;
    private String telefono;
    private String email;
    private String indirizzo;

    public UtenteResponseDto() {}

    public String getCodiceFiscaleUtente() {
        return codiceFiscaleUtente;
    }

    public void setCodiceFiscaleUtente(String codiceFiscaleUtente) {
        this.codiceFiscaleUtente = codiceFiscaleUtente;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getCognome() {
        return cognome;
    }

    public void setCognome(String cognome) {
        this.cognome = cognome;
    }

    public Date getDataNascita() {
        return dataNascita;
    }

    public void setDataNascita(Date dataNascita) {
        this.dataNascita = dataNascita;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getIndirizzo() {
        return indirizzo;
    }

    public void setIndirizzo(String indirizzo) {
        this.indirizzo = indirizzo;
    }
}
