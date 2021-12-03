package br.com.empatia.app.database.seeds;

import br.com.empatia.app.enums.UserRole;
import br.com.empatia.app.models.User;
import br.com.empatia.app.repositories.UserRepository;
import com.github.javafaker.Faker;
import lombok.AllArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Component
@AllArgsConstructor
public class UserSeeder {
    public static Faker faker = new Faker();
    UserRepository repository;
    BCryptPasswordEncoder bCrypt;

    public static User create() {
        int role = new Random().nextInt(UserRole.values().length);

        return new User(faker.internet().emailAddress(), faker.random().hex(10), faker.name().name(), UserRole.values()[role]);
    }

    public void run() {
        if (repository.count() <= 3) {
            repository.saveAll(
                    seeds()
            );
        }
    }

    public void run(int x) {
        repository.saveAll(
                seeds(x)
        );
    }

    private List<User> seeds() {
        String password = bCrypt.encode("12345678");

        User admin = new User("admin@admin.com", password, "Admin", UserRole.cast("admin"));
        User psychologist = new User("psychologist@admin.com", password, "Psychologist", UserRole.cast("psychologist"));
        User customer = new User("customer@admin.com", password, "Customer", UserRole.cast("customer"));

        return new ArrayList<>(List.of(
                admin,
                psychologist,
                customer
        ));
    }

    private List<User> seeds(int x) {
        List<User> users = new ArrayList<>();

        for (int i = 0; i < x; i++) {
            users.add(create());
        }

        return users;
    }
}
