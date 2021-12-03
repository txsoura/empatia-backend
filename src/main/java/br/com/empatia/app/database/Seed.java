package br.com.empatia.app.database;

import br.com.empatia.app.database.seeds.ChallengeSeeder;
import br.com.empatia.app.database.seeds.CustomerSeeder;
import br.com.empatia.app.database.seeds.PostSeeder;
import br.com.empatia.app.database.seeds.UserSeeder;
import lombok.AllArgsConstructor;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

@AllArgsConstructor
@Component
public class Seed implements ApplicationRunner {
    UserSeeder userSeeder;
    CustomerSeeder customerSeeder;
    ChallengeSeeder challengeSeeder;
    PostSeeder postSeeder;

    @Override
    public void run(ApplicationArguments args) {
        userSeeder.run();
//        userSeeder.run(1);
//        customerSeeder.run(2);
//        challengeSeeder.run(3);
//        postSeeder.run(4);
    }
}
