package br.com.empatia.app.resources;

import br.com.empatia.app.enums.ChallengeType;
import br.com.empatia.app.models.Challenge;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ChallengeResource {
    private UUID id;
    private ChallengeType type;
    private String content;
    private LocalDate date;
    private String title;
    private int points;
    private int views;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public ChallengeResource(Challenge challenge) {
        this.id = challenge.getId();
        this.type = challenge.getType();
        this.content = challenge.getContent();
        this.date = challenge.getDate();
        this.title = challenge.getTitle();
        this.points = challenge.getPoints();
        this.views = challenge.getViews().size();
        this.createdAt = challenge.getCreatedAt();
        this.updatedAt = challenge.getUpdatedAt();
    }

    public static List<ChallengeResource> collection(List<Challenge> challenges) {
        return challenges.stream().map(ChallengeResource::new).collect(Collectors.toList());
    }
}
