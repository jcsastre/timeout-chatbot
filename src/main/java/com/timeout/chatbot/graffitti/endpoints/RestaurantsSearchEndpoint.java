package com.timeout.chatbot.graffitti.endpoints;

import javax.validation.constraints.NotNull;

public class RestaurantsSearchEndpoint extends SearchEndpoint {

    public static String getUrl(
        @NotNull String locale,
        @NotNull Integer pageSize,
        @NotNull Integer pageNumber
    ) {
        return
            SearchEndpoint.getUrl(
                locale,
                "node-7083",
                "venue",
                pageSize,
                pageNumber
            );
    }

    public static String getUrl(
        @NotNull String locale,
        @NotNull Integer pageSize,
        @NotNull Integer pageNumber,
        @NotNull Double latitude,
        @NotNull Double longitude
    ) {
        return
            SearchEndpoint.getUrl(
                locale,
                "node-7083",
                "venue",
                pageSize,
                pageNumber,
                latitude,
                longitude
            );
    }
}
