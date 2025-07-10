package com.stage.concessionario.dto;

public class VeicoloRequestDto {
    private String numeroTelaio;
    private String marca;
    private String modello;
    private int annoImmatricolazione;
    private int chilometraggio;
    private boolean disponibile;
    private Integer idConfigurazione;

    public VeicoloRequestDto() {}

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
    
    public Integer getIdConfigurazione() {
        return idConfigurazione;
    }

    public void setIdConfigurazione(Integer idConfigurazione) {
        this.idConfigurazione = idConfigurazione;
    }
}
