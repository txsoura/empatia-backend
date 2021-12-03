package br.com.empatia.app.services;

import br.com.empatia.app.dtos.PostStoreDTO;
import br.com.empatia.app.enums.PostType;
import br.com.empatia.app.exceptions.InvalidAttributeException;
import br.com.empatia.app.exceptions.ResourceNotFoundException;
import br.com.empatia.app.models.Challenge;
import br.com.empatia.app.models.Post;
import br.com.empatia.app.models.User;
import br.com.empatia.app.repositories.PostRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

@Service
public class PostService {
    private final PostRepository postRepository;
    private final UserService userService;
    private final ChallengeService challengeService;
    private final StorageService storageService;

    @Autowired
    public PostService(PostRepository postRepository, UserService userService, ChallengeService challengeService, StorageService storageService) {
        this.postRepository = postRepository;
        this.userService = userService;
        this.challengeService = challengeService;
        this.storageService = storageService;
    }


    public List<Post> index() {
        return postRepository.findAll();
    }

    public Post store(UUID userId, PostStoreDTO data) {
        User user = userService.show(userId);

        Post repost = null;
        Challenge challenge = null;

        if (data.getPostId() != null) {
            repost = this.show(data.getPostId());
        }

        if (data.getChallengeId() != null) {
            challenge = challengeService.show(data.getChallengeId());
        }

        var post = new Post(
                PostType.cast(data.getType()),
                data.getContent(),
                user,
                repost,
                challenge
        );

        return postRepository.save(post);
    }

    @Async
    void storeView(Post post, User user) {
        if (!post.getUser().getId().equals(user.getId())) {
            List<User> views = post.getViews();

            if (!views.contains(user)) {
                views.add(user);

                post.setViews(views);

                postRepository.save(post);
            }
        }
    }

    public Post show(UUID id, User user) {
        Post post = postRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Post not found"));

        this.storeView(post, user);

        return post;
    }

    public Post show(UUID id) {
        return postRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Post not found"));
    }

    private Post showByUser(UUID id, UUID userId) {
        return postRepository.findByIdAndUserId(id, userId).orElseThrow(() -> new ResourceNotFoundException("Post not found"));
    }

    public Post update(UUID id, UUID userId, Post data) {
        Post post = this.showByUser(id, userId);

        if (!post.getType().equals(PostType.TEXT)) {
            throw new InvalidAttributeException("This post type cannot receive a content text");
        }

        post.setContent(data.getContent());

        return postRepository.save(post);
    }

    public void destroy(UUID id) {
        this.show(id);

        postRepository.deleteById(id);
    }

    public Post upload(UUID id, UUID userId, MultipartFile content) {
        Post post = this.showByUser(id, userId);

        if (content.isEmpty()) {
            throw new InvalidAttributeException("The content cannot be null");
        }

        List<String> allowedMimes = new ArrayList<>();

        switch (post.getType()) {
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
                throw new InvalidAttributeException("This post type cannot receive a content file");
        }


        String avatar = storageService.save("/posts/content", content, post.getId().toString());

        post.setContent(avatar);

        return postRepository.save(post);
    }
}
