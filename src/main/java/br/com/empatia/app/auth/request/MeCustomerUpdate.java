package br.com.empatia.app.auth.request;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.time.LocalTime;

@Data
public class MeCustomerUpdate {
    @NotNull
    private LocalDate birthdate;

    @NotBlank
    private String sex;

    private String freeTimeHabits;

    private LocalTime freeTime;

    private String preferredMedias;
}
