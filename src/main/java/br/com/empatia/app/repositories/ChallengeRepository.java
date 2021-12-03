package br.com.empatia.app.repositories;

import br.com.empatia.app.models.Challenge;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface ChallengeRepository extends JpaRepository<Challenge, UUID> {
    List<Challenge> findByIdNotIn(List<UUID> ids);
}
