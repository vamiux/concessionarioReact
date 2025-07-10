package com.stage.concessionario.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.stage.concessionario.dto.VeicoloRequestDto;
import com.stage.concessionario.dto.VeicoloResponseDto;
import com.stage.concessionario.dto.VeicoloUpdateDto;
import com.stage.concessionario.service.VeicoloService;
import com.stage.concessionario.service.ConfigurazioneService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/veicoli")
public class VeicoloController {

    private final VeicoloService veicoloService;
    private final ConfigurazioneService configurazioneService;

    public VeicoloController(VeicoloService veicoloService, ConfigurazioneService configurazioneService) {
        this.veicoloService = veicoloService;
        this.configurazioneService = configurazioneService;
    }

    @GetMapping
    public List<VeicoloResponseDto> getVeicoli() {
        return veicoloService.getVeicoli();
    }
    
    @GetMapping("/disponibili")
    public List<VeicoloResponseDto> getVeicoliDisponibili() {
        return veicoloService.getVeicoliDisponibili();
    }
    
    @GetMapping("/{numeroTelaio}")
    public ResponseEntity<VeicoloResponseDto> getVeicoloByNumeroTelaio(@PathVariable String numeroTelaio) {
        VeicoloResponseDto veicoloResponseDto = veicoloService.getVeicoloByNumeroTelaio(numeroTelaio);
        
        if (veicoloResponseDto == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
        
        return ResponseEntity.status(HttpStatus.OK).body(veicoloResponseDto);
    }
    
    @GetMapping("/search")
    public List<VeicoloResponseDto> searchVeicoli(
            @RequestParam(required = false) String numeroTelaio,
            @RequestParam(required = false) String marca, 
            @RequestParam(required = false) String modello) {
        return veicoloService.searchVeicoli(numeroTelaio, marca, modello);
    }
    
    @PostMapping
    public ResponseEntity<VeicoloResponseDto> insert(@Valid @RequestBody VeicoloRequestDto veicoloRequest) {
        VeicoloResponseDto veicoloResponseDto = veicoloService.insert(veicoloRequest);
        
        if (veicoloResponseDto == null) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(null);
        }
        
        return ResponseEntity.status(HttpStatus.CREATED).body(veicoloResponseDto);
    }

    @PutMapping("/{numeroTelaio}")
    public ResponseEntity<VeicoloResponseDto> update(
            @PathVariable String numeroTelaio, 
            @Valid @RequestBody VeicoloUpdateDto veicoloUpdateDto) {
        VeicoloResponseDto veicoloResponseDto = veicoloService.update(veicoloUpdateDto, numeroTelaio);
        
        if (veicoloResponseDto == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
        
        return ResponseEntity.status(HttpStatus.OK).body(veicoloResponseDto);
    }
}