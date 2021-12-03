package br.com.empatia.app.auth.request;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Data
public class MePasswordUpdate {
    @NotBlank
//    @Size(min=8)
    private String currentPassword;

    @NotBlank
    @Size(min = 8)
    private String password;

    @NotBlank
    @Size(min = 8)
    private String passwordConfirmation;

}
