package br.com.empatia.app.auth.controller;

import br.com.empatia.app.auth.request.MeEmailUpdate;
import br.com.empatia.app.auth.request.MePasswordUpdate;
import br.com.empatia.app.auth.request.MeUpdate;
import br.com.empatia.app.auth.service.AuthService;
import br.com.empatia.app.auth.service.MeService;
import br.com.empatia.app.models.User;
import br.com.empatia.app.resources.UserResource;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;

@CrossOrigin
@RestController
@RequestMapping("auth/user")
@RequiredArgsConstructor
public class MeController {
    private final AuthService authService;
    private final MeService meService;

    @GetMapping
    public ResponseEntity<UserResource> me() {
        return new ResponseEntity<>(new UserResource(authService.getAuthenticatedUser()), HttpStatus.OK);
    }

    @PutMapping
    public ResponseEntity<UserResource> update(@RequestBody @Valid MeUpdate data) {
        User user = authService.getAuthenticatedUser();

        return new ResponseEntity<>(new UserResource(meService.update(user.getId(), data.getName())), HttpStatus.OK);
    }

    @PutMapping("/email")
    public ResponseEntity<UserResource> updateEmail(@RequestBody @Valid MeEmailUpdate data) {
        User user = authService.getAuthenticatedUser();

        return new ResponseEntity<>(new UserResource(meService.updateEmail(user.getId(), data.getEmail().toLowerCase())), HttpStatus.OK);
    }

    @PutMapping("/password")
    public ResponseEntity<UserResource> updatePassword(@RequestBody @Valid MePasswordUpdate data) {
        User user = authService.getAuthenticatedUser();

        return new ResponseEntity<>(new UserResource(meService.updatePassword(user.getId(), data.getCurrentPassword(), data.getPassword(), data.getPasswordConfirmation())), HttpStatus.OK);
    }

    @PostMapping("/avatar")
    public ResponseEntity<UserResource> updateAvatar(@RequestParam @Valid MultipartFile avatar) {
        User user = authService.getAuthenticatedUser();

        return new ResponseEntity<>(new UserResource(meService.updateAvatar(user.getId(), avatar)), HttpStatus.OK);
    }
}

