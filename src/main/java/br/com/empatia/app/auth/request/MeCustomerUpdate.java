package br.com.empatia.app.auth.request;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;

@Data
public class MeCustomerUpdate {
    @NotNull
    private LocalDate birthdate;

    @NotBlank
    private String sex;

    @NotBlank
    private String freeTimeHabits;

    @NotBlank
    private String preferredMedias;
}
