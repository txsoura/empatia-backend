package br.com.empatia.app.database.seeds;

import br.com.empatia.app.enums.ChallengeType;
import br.com.empatia.app.models.Challenge;
import br.com.empatia.app.repositories.ChallengeRepository;
import com.github.javafaker.Faker;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Component
@AllArgsConstructor
public class ChallengeSeeder {
    public static Faker faker = new Faker();
    ChallengeRepository repository;

    public Challenge create() {
        int type = new Random().nextInt(ChallengeType.values().length);

        return repository.save(new Challenge(ChallengeType.values()[type], faker.lorem().sentences(1).toString(), LocalDate.now(), faker.lorem().sentences(2).toString(), faker.number().numberBetween(1, 10)));
    }

    public void run(int x) {
        repository.saveAll(
                seeds(x)
        );
    }

    private List<Challenge> seeds(int x) {
        List<Challenge> challenges = new ArrayList<>();

        for (int i = 0; i < x; i++) {
            challenges.add(create());
        }

        return challenges;
    }
}
