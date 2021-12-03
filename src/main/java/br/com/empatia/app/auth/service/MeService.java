package br.com.empatia.app.auth.service;

import br.com.empatia.app.exceptions.InvalidAttributeException;
import br.com.empatia.app.exceptions.ResourceNotFoundException;
import br.com.empatia.app.models.User;
import br.com.empatia.app.repositories.UserRepository;
import br.com.empatia.app.services.StorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

@Service
public class MeService {
    private final UserRepository userRepository;
    private final StorageService storageService;
    private final BCryptPasswordEncoder bCrypt;

    @Autowired
    public MeService(UserRepository userRepository, StorageService storageService, BCryptPasswordEncoder bCrypt) {
        this.userRepository = userRepository;
        this.storageService = storageService;
        this.bCrypt = bCrypt;
    }

    public User getUser(UUID id) {
        return userRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("User not found"));
    }

    public User update(UUID id, String name) {
        User user = this.getUser(id);

        user.setName(name);

        return userRepository.save(user);
    }

    public User updateEmail(UUID id, String email) {
        User user = this.getUser(id);

        user.setEmail(email);

        return userRepository.save(user);
    }

    public User updatePassword(UUID id, String currentPassword, String password, String passwordConfirmation) {
        User user = this.getUser(id);

        if (!bCrypt.matches(currentPassword, user.getPassword())) {
            throw new InvalidAttributeException("The current password is incorrect");
        }

        if (!password.equals(passwordConfirmation)) {
            throw new InvalidAttributeException("The password and password confirmation didn't match");
        }

        user.setPassword(bCrypt.encode(password));

        return userRepository.save(user);
    }

    public User updateAvatar(UUID id, MultipartFile file) {
        if (file.isEmpty()) {
            throw new InvalidAttributeException("The avatar cannot be null");
        }

        List<String> allowedMimes = Arrays.asList("image/png", "image/jpeg", "image/gif", "image/bmp");

        if (!allowedMimes.contains(file.getContentType())) {
            throw new InvalidAttributeException("The avatar must be an image");
        }

        User user = this.getUser(id);

        String avatar = storageService.save("/users/avatars", file, user.getId().toString());

        user.setAvatar(avatar);

        return userRepository.save(user);
    }
}
