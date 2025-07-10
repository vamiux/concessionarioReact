package com.stage.concessionario.controller;

import java.util.List;
import java.util.Date;
import org.springframework.format.annotation.DateTimeFormat;
import com.stage.concessionario.model.TipoMovimento;

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

import com.stage.concessionario.dto.MovimentoRequestDto;
import com.stage.concessionario.dto.MovimentoResponseDto;
import com.stage.concessionario.service.MovimentoService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/movimenti")
public class MovimentoController {

    private final MovimentoService movimentoService;

    public MovimentoController(MovimentoService movimentoService) {
        this.movimentoService = movimentoService;
    }

    @GetMapping
    public List<MovimentoResponseDto> getMovimenti() {
        return movimentoService.getMovimenti();
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<MovimentoResponseDto> getMovimentoById(@PathVariable Integer id) {
        MovimentoResponseDto movimentoResponseDto = movimentoService.getMovimentoById(id);
        
        if (movimentoResponseDto == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
        
        return ResponseEntity.status(HttpStatus.OK).body(movimentoResponseDto);
    }
    
    @GetMapping(params = "codiceFiscale")
    public List<MovimentoResponseDto> getMovimentiByUtente(@RequestParam String codiceFiscale) {
        return movimentoService.getMovimentiByUtente(codiceFiscale);
    }
    
    @GetMapping(params = "numeroTelaio")
    public List<MovimentoResponseDto> getMovimentiByVeicolo(@RequestParam String numeroTelaio) {
        return movimentoService.getMovimentiByVeicolo(numeroTelaio);
    }
    
    @GetMapping("/search")
    public List<MovimentoResponseDto> searchMovimenti(
            @RequestParam(required = false) String tipo,
            @RequestParam(required = false) String numeroTelaio,
            @RequestParam(required = false) String codiceFiscaleUtente,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Date dataInizio,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Date dataFine
    ) {
        TipoMovimento tipoMovimento = null;
        if (tipo != null && !tipo.isEmpty()) {
            try {
                tipoMovimento = TipoMovimento.valueOf(tipo);
            } catch (IllegalArgumentException e) {
                // tipo non valido, ignora
            }
        }
        return movimentoService.searchMovimenti(tipoMovimento, numeroTelaio, codiceFiscaleUtente, dataInizio, dataFine);
    }

    // @GetMapping(params = "tipo")
    // public List<MovimentoResponseDto> getMovimentiByTipo(@RequestParam String tipo) {
    //    return movimentoService.getMovimentiByTipo(tipo);
    // }
    
    @PostMapping
    public ResponseEntity<MovimentoResponseDto> insert(@Valid @RequestBody MovimentoRequestDto movimentoRequestDto) {
        // Se non c'è comproprietario, assicurati che il campo codiceFiscaleComproprietario sia null
        if (!movimentoRequestDto.isHasComproprietario()) {
            movimentoRequestDto.setCodiceFiscaleComproprietario(null);
        }
        
        // Verifica che i dati di comproprietà siano coerenti
        if (movimentoRequestDto.isHasComproprietario() && 
            (movimentoRequestDto.getCodiceFiscaleComproprietario() == null || 
             movimentoRequestDto.getCodiceFiscaleComproprietario().isEmpty())) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(null);
        }
        
        // Verifica che il proprietario e il comproprietario siano diversi solo se c'è un comproprietario
        if (movimentoRequestDto.isHasComproprietario() && 
            movimentoRequestDto.getCodiceFiscaleUtente().equals(
                movimentoRequestDto.getCodiceFiscaleComproprietario())) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(null);
        }
        
        // Calcola il prezzo per proprietario se necessario
        if (movimentoRequestDto.isHasComproprietario()) {
            double prezzoPerProprietario = movimentoRequestDto.getPrezzo() / 2;
            movimentoRequestDto.setPrezzoPerProprietario(prezzoPerProprietario);
        }
        
        MovimentoResponseDto movimentoResponseDto = movimentoService.insert(movimentoRequestDto);
        
        if (movimentoResponseDto == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
        
        return ResponseEntity.status(HttpStatus.CREATED).body(movimentoResponseDto);
    }

    @PutMapping("/{id}")
    public ResponseEntity<MovimentoResponseDto> update(
            @PathVariable Integer id, 
            @Valid @RequestBody MovimentoRequestDto movimentoRequestDto) {
        
        // Se non c'è comproprietario, assicurati che il campo codiceFiscaleComproprietario sia null
        if (!movimentoRequestDto.isHasComproprietario()) {
            movimentoRequestDto.setCodiceFiscaleComproprietario(null);
        }
        
        // Verifica che i dati di comproprietà siano coerenti
        if (movimentoRequestDto.isHasComproprietario() && 
            (movimentoRequestDto.getCodiceFiscaleComproprietario() == null || 
             movimentoRequestDto.getCodiceFiscaleComproprietario().isEmpty())) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(null);
        }
        
        // Verifica che il proprietario e il comproprietario siano diversi solo se c'è un comproprietario
        if (movimentoRequestDto.isHasComproprietario() && 
            movimentoRequestDto.getCodiceFiscaleUtente().equals(
                movimentoRequestDto.getCodiceFiscaleComproprietario())) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(null);
        }
        
        // Calcola il prezzo per proprietario se necessario
        if (movimentoRequestDto.isHasComproprietario()) {
            double prezzoPerProprietario = movimentoRequestDto.getPrezzo() / 2;
            movimentoRequestDto.setPrezzoPerProprietario(prezzoPerProprietario);
        }
        
        MovimentoResponseDto movimentoResponseDto = movimentoService.update(movimentoRequestDto, id);
        
        if (movimentoResponseDto == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
        
        return ResponseEntity.status(HttpStatus.OK).body(movimentoResponseDto);
    }
}