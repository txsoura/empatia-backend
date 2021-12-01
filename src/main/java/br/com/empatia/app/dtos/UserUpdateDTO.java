package br.com.empatia.app.dtos;

import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class UserUpdateDTO {
    @NotBlank
    private String role;
}
