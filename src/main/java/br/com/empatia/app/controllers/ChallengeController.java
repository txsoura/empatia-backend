package br.com.empatia.app.controllers;

import br.com.empatia.app.auth.service.AuthService;
import br.com.empatia.app.dtos.ChallengeStoreDTO;
import br.com.empatia.app.dtos.ChallengeUpdateDTO;
import br.com.empatia.app.enums.ChallengeType;
import br.com.empatia.app.models.Challenge;
import br.com.empatia.app.models.User;
import br.com.empatia.app.resources.ChallengeResource;
import br.com.empatia.app.services.ChallengeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.util.List;
import java.util.UUID;

@CrossOrigin
@RestController
@RequestMapping("v1/challenges")
@RequiredArgsConstructor
public class ChallengeController {
    private final ChallengeService challengeService;
    private final AuthService authService;

    @GetMapping
    public ResponseEntity<List<ChallengeResource>> index() {
        var challenges = challengeService.index();
        return ResponseEntity.ok(ChallengeResource.collection(challenges));
    }

    @PreAuthorize("hasAnyRole('PSYCHOLOGIST')")
    @PostMapping
    public ResponseEntity<ChallengeResource> store(@RequestBody @Valid ChallengeStoreDTO data) {
        var challenge = new Challenge(
                ChallengeType.cast(data.getType()),
                data.getContent(),
                data.getDate(),
                data.getTitle(),
                data.getPoints()
        );

        return new ResponseEntity<>(new ChallengeResource(challengeService.store(challenge)), HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ChallengeResource> show(@PathVariable UUID id) {
        User user = authService.getAuthenticatedUser();

        var challenge = challengeService.show(id, user);

        return ResponseEntity.ok(new ChallengeResource(challenge));
    }

    @PreAuthorize("hasAnyRole('PSYCHOLOGIST')")
    @PutMapping("/{id}")
    public ResponseEntity<ChallengeResource> update(@PathVariable UUID id, @Valid @RequestBody ChallengeUpdateDTO data) {
        var challenge = new Challenge(
                data.getContent(),
                data.getDate(),
                data.getTitle()
        );

        return ResponseEntity.ok(new ChallengeResource(challengeService.update(id, challenge)));
    }

    @PreAuthorize("hasAnyRole('ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> destroy(@PathVariable UUID id) {
        challengeService.destroy(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PreAuthorize("hasAnyRole('PSYCHOLOGIST')")
    @PostMapping("/{id}/upload")
    public ResponseEntity<ChallengeResource> upload(@PathVariable UUID id, @RequestParam @Valid MultipartFile content) {
        return new ResponseEntity<>(new ChallengeResource(challengeService.upload(id, content)), HttpStatus.OK);
    }

    @PreAuthorize("hasAnyRole('CUSTOMER')")
    @PutMapping("/{id}/finalize")
    public ResponseEntity<ChallengeResource> finalize(@PathVariable UUID id) {
        User user = authService.getAuthenticatedUser();

        return ResponseEntity.ok(new ChallengeResource(challengeService.finalize(id, user)));
    }
}
