package com.stage.concessionario.dto;

public class ConfigurazioneResponseDto {
    private Integer idConfigurazione;
    private String nomeConfigurazione;

    public ConfigurazioneResponseDto() {}

    public Integer getIdConfigurazione() {
        return idConfigurazione;
    }

    public void setIdConfigurazione(Integer idConfigurazione) {
        this.idConfigurazione = idConfigurazione;
    }

    public String getNomeConfigurazione() {
        return nomeConfigurazione;
    }

    public void setNomeConfigurazione(String nomeConfigurazione) {
        this.nomeConfigurazione = nomeConfigurazione;
    }
}
