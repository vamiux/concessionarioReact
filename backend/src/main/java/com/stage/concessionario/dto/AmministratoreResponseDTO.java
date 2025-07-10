package com.stage.concessionario.dto;

public class AmministratoreResponseDTO {

    private String codiceFiscaleAmministratore;
    private String nome;
    private String cognome;
    private String telefono;
    private String email;

    public String getCodiceFiscaleAmministratore() {
        return codiceFiscaleAmministratore;
    }

    public void setCodiceFiscaleAmministratore(String codiceFiscaleAmministratore) {
        this.codiceFiscaleAmministratore = codiceFiscaleAmministratore;
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
}