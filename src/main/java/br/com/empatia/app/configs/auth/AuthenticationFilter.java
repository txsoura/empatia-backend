package br.com.empatia.app.configs.auth;

import br.com.empatia.app.auth.model.Auth;
import br.com.empatia.app.auth.request.Login;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;

public class AuthenticationFilter extends UsernamePasswordAuthenticationFilter {
    private final AuthenticationManager authenticationManager;
    private final JwtUtils utils;

    public AuthenticationFilter(AuthenticationManager authenticationManager, JwtUtils utils) {
        setAuthenticationFailureHandler(new AuthenticationFailure());
        this.authenticationManager = authenticationManager;
        this.utils = utils;
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest req, HttpServletResponse res) throws AuthenticationException {
        try {
            var credentials = new ObjectMapper().readValue(req.getInputStream(), Login.class);

            var authToken = new UsernamePasswordAuthenticationToken(credentials.getEmail(), credentials.getPassword(), new ArrayList<>());

            return authenticationManager.authenticate(authToken);
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest req, HttpServletResponse res, FilterChain chain, Authentication authResult) throws IOException {
        var user = ((Auth) authResult.getPrincipal());
        var token = utils.generateToken(user);

        String body = "{\"token\": \"" + token + "\"}";
        res.setContentType("Application/json");
        res.setCharacterEncoding("UTF-8");
        res.getWriter().write(body);
    }
}
