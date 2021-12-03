package br.com.empatia.app.services;

import br.com.empatia.app.auth.service.MeCustomerService;
import br.com.empatia.app.enums.ChallengeType;
import br.com.empatia.app.exceptions.BusinessRuleException;
import br.com.empatia.app.exceptions.InvalidAttributeException;
import br.com.empatia.app.exceptions.ResourceNotFoundException;
import br.com.empatia.app.models.Challenge;
import br.com.empatia.app.models.Customer;
import br.com.empatia.app.models.User;
import br.com.empatia.app.repositories.ChallengeRepository;
import br.com.empatia.app.repositories.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

@Service
public class ChallengeService {
    private final ChallengeRepository challengeRepository;
    private final CustomerRepository customerRepository;
    private final MeCustomerService meCustomerService;
    private final StorageService storageService;

    @Autowired
    public ChallengeService(ChallengeRepository challengeRepository, CustomerRepository customerRepository, MeCustomerService meCustomerService, StorageService storageService) {
        this.challengeRepository = challengeRepository;
        this.customerRepository = customerRepository;
        this.meCustomerService = meCustomerService;
        this.storageService = storageService;
    }


    public List<Challenge> index() {
        return challengeRepository.findAll();
    }

    public Challenge store(Challenge challenge) {
        return challengeRepository.save(challenge);
    }

    @Async
    void storeView(Challenge challenge, User user) {
        List<User> views = challenge.getViews();

        if (!views.contains(user)) {
            views.add(user);

            challenge.setViews(views);

            challengeRepository.save(challenge);
        }
    }

    public Challenge show(UUID id, User user) {
        Challenge challenge = challengeRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Challenge not found"));

        this.storeView(challenge, user);

        return challenge;
    }

    public Challenge show(UUID id) {
        return challengeRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Challenge not found"));
    }

    public Challenge update(UUID id, Challenge data) {
        Challenge challenge = this.show(id);

        if (!challenge.getType().equals(ChallengeType.TEXT)) {
            throw new InvalidAttributeException("This challenge type cannot receive a content text");
        }

        challenge.setContent(data.getContent());
        challenge.setDate(data.getDate());
        challenge.setTitle(data.getTitle());

        return challengeRepository.save(challenge);
    }

    public void destroy(UUID id) {
        this.show(id);

        challengeRepository.deleteById(id);
    }

    public Challenge finalize(UUID id, User user) {
        Customer customer = meCustomerService.me(user.getId());
        Challenge challenge = this.show(id);

        List<Challenge> challenges = customer.getChallenges();

        if (challenges.contains(challenge)) {
            throw new BusinessRuleException("You have already completed this challenge");
        }

        challenges.add(challenge);

        customer.setChallenges(challenges);
        customer.setPoints(customer.getPoints() + challenge.getPoints());

        customerRepository.save(customer);

        return challenge;
    }

    public Challenge upload(UUID id, MultipartFile content) {
        Challenge challenge = this.show(id);

        if (content.isEmpty()) {
            throw new InvalidAttributeException("The content cannot be null");
        }

        List<String> allowedMimes = new ArrayList<>();

        switch (challenge.getType()) {
            case AUDIO:
                allowedMimes.addAll(Arrays.asList("audio/basic", "audio/x-aiff", "audio/x-wav", "audio/x-mpeg", "audio/x-mpeg-2"));

                if (!allowedMimes.contains(content.getContentType())) {
                    throw new InvalidAttributeException("The content must be an audio");
                }
                break;
            case VIDEO:
                allowedMimes.addAll(Arrays.asList("video/mpeg", "video/mpeg-2", "video/quicktime", "video/x-msvideo", "video/x-sgi-movie"));

                if (!allowedMimes.contains(content.getContentType())) {
                    throw new InvalidAttributeException("The content must be a video");
                }
                break;
            case IMAGE:
                allowedMimes.addAll(Arrays.asList("image/png", "image/jpeg", "image/gif", "image/bmp"));

                if (!allowedMimes.contains(content.getContentType())) {
                    throw new InvalidAttributeException("The content must be an image");
                }
                break;
            default:
                throw new InvalidAttributeException("This challenge type cannot receive a content file");
        }


        String avatar = storageService.save("/challenges/content", content, challenge.getId().toString());

        challenge.setContent(avatar);

        return challengeRepository.save(challenge);
    }
}
