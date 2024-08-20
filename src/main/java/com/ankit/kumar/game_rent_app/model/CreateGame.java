package com.ankit.kumar.game_rent_app.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CreateGame {

    @NotEmpty
    private String gameTitle;

    @NotEmpty
    private String gameStudio;

    @NotEmpty
    private List<@NotBlank String> genres;
}
