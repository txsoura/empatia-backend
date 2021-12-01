package br.com.empatia.app.dtos;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;

@Data
public class ChallengeUpdateDTO {
    @NotBlank
    private String content;

    @NotNull
    private LocalDate date;

    @NotBlank
    private String title;
}
