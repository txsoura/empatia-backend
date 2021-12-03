package br.com.empatia.app.auth.service;


import br.com.empatia.app.auth.model.Auth;
import br.com.empatia.app.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserDetailsService {
    private final UserRepository repository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        var user = repository
                .findByEmail(email.toLowerCase())
                .orElseThrow(() -> new UsernameNotFoundException(email));

        return new Auth(user);
    }
}
