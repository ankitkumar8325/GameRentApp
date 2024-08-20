package com.ankit.kumar.game_rent_app.dao.model;

import jakarta.validation.constraints.NotEmpty;
import lombok.*;

/**
 * This is Data Object class for GENRE entries
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Genre {

    @NonNull
    private String id;

    @NonNull
    @NotEmpty
    private String genreName;

    @NotEmpty
    private String genreDetail;
}
