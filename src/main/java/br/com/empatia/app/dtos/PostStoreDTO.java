package br.com.empatia.app.dtos;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.util.UUID;

@Data
public class PostStoreDTO {
    @NotBlank
    private String type;

    @NotBlank
    private String content;

    private UUID postId;

    private UUID challengeId;
}
