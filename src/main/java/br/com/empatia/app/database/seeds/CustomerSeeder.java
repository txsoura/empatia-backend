package br.com.empatia.app.database.seeds;

import br.com.empatia.app.enums.CustomerSex;
import br.com.empatia.app.models.Customer;
import br.com.empatia.app.models.User;
import br.com.empatia.app.repositories.CustomerRepository;
import br.com.empatia.app.repositories.UserRepository;
import com.github.javafaker.Faker;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Component
@AllArgsConstructor
public class CustomerSeeder {
    public static Faker faker = new Faker();
    UserRepository userRepository;
    CustomerRepository repository;

    public static Customer create(User user) {
        int sex = new Random().nextInt(CustomerSex.values().length);

        return new Customer(faker.number().numberBetween(1, 10), LocalDate.now(), CustomerSex.values()[sex], faker.lorem().sentences(5).toString(), faker.lorem().sentences(5).toString(), user);
    }

    public static Customer create() {
        int sex = new Random().nextInt(CustomerSex.values().length);

        User user = UserSeeder.create();

        return new Customer(faker.number().numberBetween(1, 10), LocalDate.now(), CustomerSex.values()[sex], faker.lorem().sentences(5).toString(), faker.lorem().sentences(5).toString(), user);
    }

    public void run(int x) {
        repository.saveAll(
                seeds(x)
        );
    }

    private List<Customer> seeds(int x) {
        List<Customer> customers = new ArrayList<>();

        for (int i = 0; i < x; i++) {
            customers.add(create());
        }

        return customers;
    }
}
