package com.ankit.kumar.game_rent_app.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RentedGameResponse {

    private List<SinglGameDetail> gameDetailList;
    private String message;

    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class SinglGameDetail {
        private String gameName;
        private String gameStudio;
        private List<String> gameGenreList;
    }
}
