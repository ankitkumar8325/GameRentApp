package com.ankit.kumar.game_rent_app.model;

import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Same request is used for return game request as well
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RentGameRequest {

    @NotEmpty
    private String userId;

    @NotEmpty
    private String gameTitle;

    @NotEmpty
    private String gameStudio;

    private Boolean returnGameRequest;

}
