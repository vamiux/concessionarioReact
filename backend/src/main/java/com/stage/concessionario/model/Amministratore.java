package com.stage.concessionario.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import java.io.Serializable;
import java.util.List;

@Entity
@Table(name = "amministratore", schema = "concessionario")
public class Amministratore implements Serializable {

    @Id
    @Column(name = "codice_fiscale_amministratore", length = 16)
    private String codiceFiscaleAmministratore;

    @Column(name = "nome", nullable = false)
    @Size(max = 50)
    private String nome;

    @Column(name = "cognome", nullable = false)
    @Size(max = 50)
    private String cognome;

    @Column(name = "telefono", length = 15)
    private String telefono;

    @Column(name = "email", nullable = false)
    private String email;

    @Column(name = "password", nullable = false)
    private String password;

    @Column(name = "ruolo", nullable = false)
    private String ruolo = "ADMIN";

    @OneToMany(mappedBy = "amministratore")
    private List<Movimento> movimenti;

    public Amministratore() {}

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

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
    
    public String getRuolo() {
        return ruolo;
    }
    
    public void setRuolo(String ruolo) {
        this.ruolo = ruolo;
    }
}