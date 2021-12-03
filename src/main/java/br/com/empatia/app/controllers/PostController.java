package br.com.empatia.app.controllers;

import br.com.empatia.app.auth.service.AuthService;
import br.com.empatia.app.dtos.PostStoreDTO;
import br.com.empatia.app.dtos.PostUpdateDTO;
import br.com.empatia.app.models.Post;
import br.com.empatia.app.models.User;
import br.com.empatia.app.resources.PostResource;
import br.com.empatia.app.services.PostService;
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
@RequestMapping("v1/posts")
@RequiredArgsConstructor
public class PostController {
    private final PostService postService;
    private final AuthService authService;

    @GetMapping
    public ResponseEntity<List<PostResource>> index() {
        var posts = postService.index();
        return ResponseEntity.ok(PostResource.collection(posts));
    }

    @PreAuthorize("hasAnyRole('PSYCHOLOGIST')")
    @PostMapping
    public ResponseEntity<PostResource> store(@RequestBody @Valid PostStoreDTO data) {
        User user = authService.getAuthenticatedUser();

        return new ResponseEntity<>(new PostResource(postService.store(user.getId(), data)), HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<PostResource> show(@PathVariable UUID id) {
        User user = authService.getAuthenticatedUser();

        var post = postService.show(id, user);

        return ResponseEntity.ok(new PostResource(post));
    }

    @PreAuthorize("hasAnyRole('PSYCHOLOGIST')")
    @PutMapping("/{id}")
    public ResponseEntity<PostResource> update(@PathVariable UUID id, @Valid @RequestBody PostUpdateDTO data) {
        User user = authService.getAuthenticatedUser();

        var post = new Post(
                data.getContent()
        );

        return ResponseEntity.ok(new PostResource(postService.update(id, user.getId(), post)));
    }

    @PreAuthorize("hasAnyRole('ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> destroy(@PathVariable UUID id) {
        postService.destroy(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PreAuthorize("hasAnyRole('PSYCHOLOGIST')")
    @PostMapping("/{id}/upload")
    public ResponseEntity<PostResource> upload(@PathVariable UUID id, @RequestParam @Valid MultipartFile content) {
        User user = authService.getAuthenticatedUser();

        return new ResponseEntity<>(new PostResource(postService.upload(id, user.getId(), content)), HttpStatus.OK);
    }
}
