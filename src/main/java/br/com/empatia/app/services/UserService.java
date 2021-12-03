package br.com.empatia.app.services;

import br.com.empatia.app.exceptions.InvalidAttributeException;
import br.com.empatia.app.exceptions.ResourceNotFoundException;
import br.com.empatia.app.models.User;
import br.com.empatia.app.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class UserService {
    private final UserRepository userRepository;

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }


    public List<User> index() {
        return userRepository.findAll();
    }

    public User store(User user) {
        Optional<User> userExists = userRepository.findByEmail(user.getEmail().toLowerCase());

        if (userExists.isPresent()) {
            throw new InvalidAttributeException("This email is already registered to a user");
        }

        return userRepository.save(user);
    }

    public User show(UUID id) {
        return userRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("User not found"));
    }

    public User update(UUID id, User data) {
        User user = this.show(id);

        user.setRole(data.getRole());

        return userRepository.save(user);
    }

    public void destroy(UUID id) {
        this.show(id);

        userRepository.deleteById(id);
    }

    public User activate(UUID id, boolean activate) {
        User user = this.show(id);

        user.setActive(activate);

        return userRepository.save(user);
    }
}
