package br.com.empatia.app.configs.auth;

import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class AuthenticationFailure implements AuthenticationFailureHandler {
    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException ex)
            throws IOException {
        response.setStatus(401);
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.getWriter().write(prepareMessageToJson(ex.getMessage(), request.getRequestURI()));
    }

    private String prepareMessageToJson(String message, String path) {
        var df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssX");
        var date = df.format(new Date());

        return "{\"timestamp\": \"" + date + "\", "
                + "\"status\": 401, "
                + "\"error\": \"Unauthorized\", "
                + "\"message\": \"" + message + "\", "
                + "\"path\": \"" + path + "\" }";
    }
}
