package com.eleks.academy.whoami.model.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CharacterSuggestion {

    @NotNull
    @Size(min = 2, max = 50, message = "nickname length must be between {min} and {max}!")
    @NotBlank(message = "must not be blank")
    private String nickname;

    @NotNull
    @Size(min = 2, max = 50, message = "character length must be between {min} and {max}!")
    @NotBlank(message = "must not be blank")
    private String character;

}
