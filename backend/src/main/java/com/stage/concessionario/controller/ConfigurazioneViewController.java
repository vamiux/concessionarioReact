package com.stage.concessionario.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.stage.concessionario.dto.ConfigurazioneRequestDto;
import com.stage.concessionario.dto.ConfigurazioneResponseDto;
import com.stage.concessionario.service.ConfigurazioneService;

@Controller
@RequestMapping("/configurazioni")
public class ConfigurazioneViewController {

    private final ConfigurazioneService configurazioneService;

    public ConfigurazioneViewController(ConfigurazioneService configurazioneService) {
        this.configurazioneService = configurazioneService;
    }

    @GetMapping
    public String getConfigurazioniPage(Model model) {
        model.addAttribute("configurazioni", configurazioneService.getConfigurazioni());
        return "configurazioni";
    }
    
    @PutMapping("/modifica/{id}")
    @ResponseBody
    public ResponseEntity<ConfigurazioneResponseDto> updateConfigurazione(
            @PathVariable Integer id, 
            @RequestBody ConfigurazioneRequestDto configurazioneRequestDto) {
        
        ConfigurazioneResponseDto result = configurazioneService.update(configurazioneRequestDto, id);
        
        if (result == null) {
            return ResponseEntity.notFound().build();
        }
        
        return ResponseEntity.ok(result);
    }
    
    @PostMapping("/nuova")
    @ResponseBody
    public ResponseEntity<ConfigurazioneResponseDto> createConfigurazione(
            @RequestBody ConfigurazioneRequestDto configurazioneRequestDto) {
        
        ConfigurazioneResponseDto result = configurazioneService.insert(configurazioneRequestDto);
        
        if (result == null) {
            return ResponseEntity.badRequest().build();
        }
        
        return ResponseEntity.ok(result);
    }
}
