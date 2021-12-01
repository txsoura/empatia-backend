package br.com.empatia.app.auth.service;


import br.com.empatia.app.auth.model.Auth;
import br.com.empatia.app.auth.repository.AuthRepository;
import br.com.empatia.app.models.User;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
public class AuthService implements AuthRepository {
    public static Auth authenticated() {
        try {
            return (Auth) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        } catch (Exception ex) {
            return null;
        }
    }

    @Override
    public User getAuthenticatedUser() {
        return authenticated();
    }
}
