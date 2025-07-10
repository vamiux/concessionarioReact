package com.stage.concessionario.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;
import java.io.Serializable;
import java.util.Date;
import java.util.List;
import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "utente")
public class Utente implements Serializable {

    @Id
    @Column(name = "codice_fiscale_utente", length = 16, unique = true, nullable = false)
    private String codiceFiscaleUtente;

    @Column(name = "nome", nullable = false)
    @Size(min = 1, max = 50)
    private String nome;

    @Column(name = "cognome", nullable = false)
    @Size(min = 1, max = 50)
    private String cognome;

    @Column(name = "data_nascita", nullable = false)
    private Date dataNascita;

    @Column(name = "telefono")
    @Size(max = 20)
    private String telefono;

    @Column(name = "email", nullable = false)
    @Email
    @Size(max = 100)
    private String email;

    @Column(name = "indirizzo", nullable = false)
    @Size(min = 1, max = 200)
    private String indirizzo;

    @OneToMany(mappedBy = "utente")
    @JsonIgnore
    private List<Movimento> movimenti;

    public Utente() {}

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