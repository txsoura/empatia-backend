package br.com.empatia.app.auth.repository;

import br.com.empatia.app.models.User;
import org.springframework.stereotype.Repository;

@Repository
public interface AuthRepository {
    User getAuthenticatedUser();
}
