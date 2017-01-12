package com.timeout.chatbot.graffitti.endpoints;

import javax.validation.constraints.NotNull;

public class SearchEndpoint {

    private static final String URL =
        "http://graffiti.timeout.com/v1/sites/uk-london/search" +
            "?locale=%s" +
            "&what=%s" +
            "&type=%s" +
            "&page_size=%s" +
            "&page_number=%s";

    private static final String URL_GEO =
        URL +
        "&latitude=%s" +
        "&longitude=%s";

    public static String getUrl(
        @NotNull String locale,
        @NotNull String what,
        @NotNull String type,
        @NotNull Integer pageSize,
        @NotNull Integer pageNumber
    ) {

        return
            String.format(
                URL,
                locale,
                what,
                type,
                pageSize,
                pageNumber
            );
    }

    public static String getUrl(
        @NotNull String locale,
        @NotNull String what,
        @NotNull String type,
        @NotNull Integer pageSize,
        @NotNull Integer pageNumber,
        @NotNull Double latitude,
        @NotNull Double longitude
    ) {

        return
            String.format(
                URL_GEO,
                locale,
                what,
                type,
                pageSize,
                pageNumber,
                latitude,
                longitude
            );
    }
}
