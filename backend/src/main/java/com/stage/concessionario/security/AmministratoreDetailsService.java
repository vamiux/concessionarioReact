package com.stage.concessionario.security;

import com.stage.concessionario.model.Amministratore;
import com.stage.concessionario.repository.AmministratoreRepository;

import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Optional;

@Service
public class AmministratoreDetailsService implements UserDetailsService {

    private final AmministratoreRepository amministratoreRepository;

    public AmministratoreDetailsService(AmministratoreRepository amministratoreRepository) {
        this.amministratoreRepository = amministratoreRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Optional<Amministratore> amministratoreOpt = amministratoreRepository.findByEmail(email);
        
        if (amministratoreOpt.isEmpty()) {
            throw new UsernameNotFoundException("Amministratore non trovato con email: " + email);
        }
        
        Amministratore amministratore = amministratoreOpt.get();
        
        return new User(
            amministratore.getEmail(),
            amministratore.getPassword(),
            Collections.singletonList(new SimpleGrantedAuthority("ROLE_" + amministratore.getRuolo()))
        );
    }
}
