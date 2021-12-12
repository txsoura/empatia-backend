package br.com.empatia.app.auth.controller;

import br.com.empatia.app.auth.request.MeCustomerUpdate;
import br.com.empatia.app.auth.service.AuthService;
import br.com.empatia.app.auth.service.MeCustomerService;
import br.com.empatia.app.enums.CustomerSex;
import br.com.empatia.app.models.Challenge;
import br.com.empatia.app.models.Customer;
import br.com.empatia.app.models.User;
import br.com.empatia.app.repositories.ChallengeRepository;
import br.com.empatia.app.resources.ChallengeResource;
import br.com.empatia.app.resources.CustomerResource;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@CrossOrigin
@RestController
@RequestMapping("auth/user/customer")
@RequiredArgsConstructor
public class MeCustomerController {
    private final AuthService authService;
    private final MeCustomerService meCustomerService;
    private final ChallengeRepository challengeRepository;

    @PreAuthorize("hasAnyRole('CUSTOMER')")
    @GetMapping
    public ResponseEntity<CustomerResource> me() {
        User user = authService.getAuthenticatedUser();

        return new ResponseEntity<>(new CustomerResource(meCustomerService.me(user.getId())), HttpStatus.OK);
    }

    @PreAuthorize("hasAnyRole('CUSTOMER')")
    @PutMapping
    public ResponseEntity<CustomerResource> update(@RequestBody @Valid MeCustomerUpdate data) {
        User user = authService.getAuthenticatedUser();

        var customer = new Customer(
                data.getBirthdate(),
                CustomerSex.cast(data.getSex()),
                data.getFreeTimeHabits(),
                data.getFreeTime(),
                data.getPreferredMedias()
        );

        return new ResponseEntity<>(new CustomerResource(meCustomerService.update(user.getId(), customer)), HttpStatus.OK);
    }

    @PreAuthorize("hasAnyRole('CUSTOMER')")
    @GetMapping("/challenges")
    public ResponseEntity<List<ChallengeResource>> challenges() {
        User user = authService.getAuthenticatedUser();
        Customer customer = meCustomerService.me(user.getId());

        var challenges = customer.getChallenges();
        return ResponseEntity.ok(ChallengeResource.collection(challenges));
    }

    @PreAuthorize("hasAnyRole('CUSTOMER')")
    @GetMapping("/challenges/new")
    public ResponseEntity<List<ChallengeResource>> newChallenges() {
        User user = authService.getAuthenticatedUser();
        Customer customer = meCustomerService.me(user.getId());

        List<UUID> ids = new ArrayList<>();

        for (Challenge challenge : customer.getChallenges()
        ) {
            ids.add(challenge.getId());
        }

        var challenges = challengeRepository.findByIdNotIn(ids);

        return ResponseEntity.ok(ChallengeResource.collection(challenges));
    }
}
