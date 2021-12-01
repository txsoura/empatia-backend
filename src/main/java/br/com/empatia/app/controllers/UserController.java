package br.com.empatia.app.controllers;

import br.com.empatia.app.dtos.UserStoreDTO;
import br.com.empatia.app.dtos.UserUpdateDTO;
import br.com.empatia.app.enums.UserRole;
import br.com.empatia.app.models.User;
import br.com.empatia.app.resources.UserResource;
import br.com.empatia.app.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.UUID;

@CrossOrigin
@RestController
@RequestMapping("v1/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;
    private final BCryptPasswordEncoder bCrypt;

    @PreAuthorize("hasAnyRole('ADMIN')")
    @GetMapping
    public ResponseEntity<List<UserResource>> index() {
        var users = userService.index();
        return ResponseEntity.ok(UserResource.collection(users));
    }

    @PreAuthorize("hasAnyRole('ADMIN')")
    @PostMapping
    public ResponseEntity<UserResource> store(@RequestBody @Valid UserStoreDTO data) {
        var user = new User(
                data.getEmail().toLowerCase(),
                bCrypt.encode(data.getPassword()),
                data.getName(),
                UserRole.cast(data.getRole())
        );

        return new ResponseEntity<>(new UserResource(userService.store(user)), HttpStatus.CREATED);
    }

    @PreAuthorize("hasAnyRole('ADMIN')")
    @GetMapping("/{id}")
    public ResponseEntity<UserResource> show(@PathVariable UUID id) {
        var user = userService.show(id);
        return ResponseEntity.ok(new UserResource(user));
    }

    @PreAuthorize("hasAnyRole('ADMIN')")
    @PutMapping("/{id}")
    public ResponseEntity<UserResource> update(@PathVariable UUID id, @Valid @RequestBody UserUpdateDTO data) {
        var user = new User(
                UserRole.cast(data.getRole())
        );

        return ResponseEntity.ok(new UserResource(userService.update(id, user)));
    }

    @PreAuthorize("hasAnyRole('ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> destroy(@PathVariable UUID id) {
        userService.destroy(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PreAuthorize("hasAnyRole('ADMIN')")
    @PutMapping("/{id}/activate")
    public ResponseEntity<UserResource> activate(@PathVariable UUID id) {
        return ResponseEntity.ok(new UserResource(userService.activate(id, true)));
    }

    @PreAuthorize("hasAnyRole('ADMIN')")
    @PutMapping("/{id}/deactivate")
    public ResponseEntity<UserResource> deactivate(@PathVariable UUID id) {
        return ResponseEntity.ok(new UserResource(userService.activate(id, false)));
    }
}
