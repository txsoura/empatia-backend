package br.com.empatia.app.auth.controller;

import br.com.empatia.app.auth.request.Register;
import br.com.empatia.app.models.Customer;
import br.com.empatia.app.models.User;
import br.com.empatia.app.resources.UserResource;
import br.com.empatia.app.services.CustomerService;
import br.com.empatia.app.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@CrossOrigin
@RestController
@RequestMapping("auth")
@RequiredArgsConstructor
public class AuthController {
    private final UserService userService;
    private final CustomerService customerService;
    private final BCryptPasswordEncoder bCrypt;

    @PostMapping("/register")
    public ResponseEntity<UserResource> register(@RequestBody @Valid Register data) {
        var user = new User(
                data.getEmail().toLowerCase(),
                bCrypt.encode(data.getPassword()),
                data.getName()
        );

        User created = userService.store(user);
        customerService.store(new Customer(user));

        return new ResponseEntity<>(new UserResource(created), HttpStatus.CREATED);
    }
}
