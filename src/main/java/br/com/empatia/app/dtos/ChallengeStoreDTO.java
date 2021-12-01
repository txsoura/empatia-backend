package br.com.empatia.app.dtos;

import lombok.Data;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;

@Data
public class ChallengeStoreDTO {
    @NotBlank
    private String type;

    @NotBlank
    private String content;

    @NotNull
    private LocalDate date;

    @NotBlank
    private String title;

    @NotNull
    @Min(0)
    private int points;
}
