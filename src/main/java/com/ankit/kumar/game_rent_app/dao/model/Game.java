package com.ankit.kumar.game_rent_app.dao.model;

import jakarta.validation.constraints.NotEmpty;
import lombok.*;

/**
 * This is Data Object class for GAME entries
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Game {

    @NotEmpty
    @NonNull
    private String title;

    @NotEmpty
    @NonNull
    private String studio;
}
