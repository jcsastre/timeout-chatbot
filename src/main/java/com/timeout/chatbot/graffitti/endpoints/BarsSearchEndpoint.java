package com.timeout.chatbot.graffitti.endpoints;

import javax.validation.constraints.NotNull;

public class BarsSearchEndpoint extends SearchEndpoint {

    public static String getUrl(
        @NotNull String locale,
        @NotNull Integer pageSize,
        @NotNull Integer pageNumber
    ) {
        return
            SearchEndpoint.getUrl(
                locale,
                "node-7067",
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
                "node-7067",
                "venue",
                pageSize,
                pageNumber,
                latitude,
                longitude
            );
    }
}
