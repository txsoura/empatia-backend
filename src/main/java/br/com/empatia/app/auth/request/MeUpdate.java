package br.com.empatia.app.auth.request;

import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class MeUpdate {
    @NotBlank
    private String name;
}
