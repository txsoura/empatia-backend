package br.com.empatia.app.resources;

import br.com.empatia.app.enums.PostType;
import br.com.empatia.app.models.Post;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PostResource {
    private UUID id;
    private PostType type;
    private String content;
    private UUID userId;
    private String postId;
    private String challengeId;
    private int views;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public PostResource(Post post) {
        this.id = post.getId();
        this.type = post.getType();
        this.content = post.getContent();
        this.userId = post.getUser().getId();
        this.postId = post.getPost() == null ? null : post.getPost().getId().toString();
        this.challengeId = post.getChallenge() == null ? null : post.getChallenge().getId().toString();
        this.views = post.getViews().size();
        this.createdAt = post.getCreatedAt();
        this.updatedAt = post.getUpdatedAt();
    }

    public static List<PostResource> collection(List<Post> posts) {
        return posts.stream().map(PostResource::new).collect(Collectors.toList());
    }
}
