package com.ankit.kumar.game_rent_app.dao.model;

import jakarta.validation.constraints.NotEmpty;
import lombok.*;

/**
 * This is Data Object class for USER entries
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class User {

    @NonNull
    private String id;

    @NonNull
    @NotEmpty
    private String username;

    @NonNull
    @NotEmpty
    private String name;
}
