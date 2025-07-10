package com.stage.concessionario.controller;

import com.stage.concessionario.dto.AmministratoreRequestDTO;
import com.stage.concessionario.dto.AmministratoreResponseDTO;
import com.stage.concessionario.service.AmministratoreService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/amministratori")
@PreAuthorize("hasRole('ADMIN')")
public class AmministratoreController {

    private final AmministratoreService amministratoreService;

    @Autowired
    public AmministratoreController(AmministratoreService amministratoreService) {
        this.amministratoreService = amministratoreService;
    }

    @GetMapping
    public String getAllAmministratori(Model model) {
        List<AmministratoreResponseDTO> amministratori = amministratoreService.getAllAmministratori();
        model.addAttribute("amministratori", amministratori);
        return "amministratori/list";
    }

    @GetMapping("/new")
    public String showCreateForm(Model model) {
        model.addAttribute("amministratore", new AmministratoreRequestDTO());
        return "amministratori/form";
    }

    @PostMapping
    public String createAmministratore(@Valid @ModelAttribute("amministratore") AmministratoreRequestDTO amministratoreDTO,
                                     BindingResult result) {
        if (result.hasErrors()) {
            return "amministratori/form";
        }
        
        amministratoreService.createAmministratore(amministratoreDTO);
        return "redirect:/amministratori";
    }

    @GetMapping("/edit/{codiceFiscale}")
    public String showEditForm(@PathVariable String codiceFiscale, Model model) {
        AmministratoreResponseDTO amministratore = amministratoreService.getAmministratoreByCodiceFiscale(codiceFiscale);
        if (amministratore == null) {
            return "redirect:/amministratori";
        }
        
        // Creiamo un DTO di richiesta a partire dal DTO di risposta
        AmministratoreRequestDTO requestDTO = new AmministratoreRequestDTO();
        requestDTO.setCodiceFiscaleAmministratore(amministratore.getCodiceFiscaleAmministratore());
        requestDTO.setNome(amministratore.getNome());
        requestDTO.setCognome(amministratore.getCognome());
        requestDTO.setTelefono(amministratore.getTelefono());
        requestDTO.setEmail(amministratore.getEmail());
        
        model.addAttribute("amministratore", requestDTO);
        return "amministratori/form";
    }

    @PostMapping("/{codiceFiscale}")
    public String updateAmministratore(@PathVariable String codiceFiscale,
                                     @Valid @ModelAttribute("amministratore") AmministratoreRequestDTO amministratoreDTO,
                                     BindingResult result) {
        if (result.hasErrors()) {
            return "amministratori/form";
        }
        
        amministratoreService.updateAmministratore(codiceFiscale, amministratoreDTO);
        return "redirect:/amministratori";
    }

    @GetMapping("/delete/{codiceFiscale}")
    public String deleteAmministratore(@PathVariable String codiceFiscale) {
        amministratoreService.deleteAmministratore(codiceFiscale);
        return "redirect:/amministratori";
    }

    // API REST per eventuali chiamate AJAX
    @GetMapping("/api")
    @ResponseBody
    public ResponseEntity<List<AmministratoreResponseDTO>> getAmministratoriApi() {
        List<AmministratoreResponseDTO> amministratori = amministratoreService.getAllAmministratori();
        return new ResponseEntity<>(amministratori, HttpStatus.OK);
    }

    @GetMapping("/api/{codiceFiscale}")
    @ResponseBody
    public ResponseEntity<AmministratoreResponseDTO> getAmministratoreApi(@PathVariable String codiceFiscale) {
        AmministratoreResponseDTO amministratore = amministratoreService.getAmministratoreByCodiceFiscale(codiceFiscale);
        if (amministratore == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(amministratore, HttpStatus.OK);
    }
}
