package br.com.empatia.app.database.seeds;

import br.com.empatia.app.enums.PostType;
import br.com.empatia.app.models.Post;
import br.com.empatia.app.models.User;
import br.com.empatia.app.repositories.PostRepository;
import com.github.javafaker.Faker;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Component
@AllArgsConstructor
public class PostSeeder {
    public static Faker faker = new Faker();
    PostRepository repository;
    UserSeeder userSeeder;

    public Post create() {
        int type = new Random().nextInt(PostType.values().length);

        User user = userSeeder.create();

        return repository.save(new Post(PostType.values()[type], faker.lorem().sentences(2).toString(), user));
    }

    public void run(int x) {
        repository.saveAll(
                seeds(x)
        );
    }

    private List<Post> seeds(int x) {
        List<Post> posts = new ArrayList<>();

        for (int i = 0; i < x; i++) {
            posts.add(create());
        }

        return posts;
    }
}
