package com.stage.concessionario.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.stage.concessionario.dto.ConfigurazioneRequestDto;
import com.stage.concessionario.dto.ConfigurazioneResponseDto;
import com.stage.concessionario.service.ConfigurazioneService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/configurazioni")
public class ConfigurazioneController {

    private final ConfigurazioneService configurazioneService;

    public ConfigurazioneController(ConfigurazioneService configurazioneService) {
        this.configurazioneService = configurazioneService;
    }

    @GetMapping
    public List<ConfigurazioneResponseDto> getConfigurazioni() {
        return configurazioneService.getConfigurazioni();
    }
    
    @GetMapping(params = "id")
    public ResponseEntity<ConfigurazioneResponseDto> getConfigurazioneById(@RequestParam Integer id) {
        ConfigurazioneResponseDto configurazioneResponseDto = configurazioneService.getConfigurazioneById(id);
        
        if (configurazioneResponseDto == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
        
        return ResponseEntity.status(HttpStatus.OK).body(configurazioneResponseDto);
    }
    
    @GetMapping(params = "nome")
    public ResponseEntity<ConfigurazioneResponseDto> getConfigurazioneByNome(@RequestParam String nome) {
        ConfigurazioneResponseDto configurazioneResponseDto = configurazioneService.getConfigurazioneByNome(nome);
        
        if (configurazioneResponseDto == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
        
        return ResponseEntity.status(HttpStatus.OK).body(configurazioneResponseDto);
    }
    
    @PostMapping
    public ResponseEntity<ConfigurazioneResponseDto> insert(@Valid @RequestBody ConfigurazioneRequestDto configurazioneRequestDto) {
        ConfigurazioneResponseDto configurazioneResponseDto = configurazioneService.insert(configurazioneRequestDto);
        
        if (configurazioneResponseDto == null) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(null);
        }
        
        return ResponseEntity.status(HttpStatus.CREATED).body(configurazioneResponseDto);
    }
    
    @PutMapping(params = "id")
    public ResponseEntity<ConfigurazioneResponseDto> update(
            @Valid @RequestParam Integer id, 
            @RequestBody ConfigurazioneRequestDto configurazioneRequestDto) {
        ConfigurazioneResponseDto configurazioneResponseDto = configurazioneService.update(configurazioneRequestDto, id);
        
        if (configurazioneResponseDto == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
        
        return ResponseEntity.status(HttpStatus.OK).body(configurazioneResponseDto);
    }
}
