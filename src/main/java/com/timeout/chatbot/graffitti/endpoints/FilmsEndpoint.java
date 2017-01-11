package com.timeout.chatbot.graffitti.endpoints;

public class FilmsEndpoint {

    private static final String SEARCH_UK_LONDON_FILMS =
        "http://graffiti.timeout.com/v1/sites/uk-london/search?" +
            "&locale=en-GB" +
            "&sort=published-at" +
            "&when=today" +
            "&what=%s" +
            "&latitude=%s" +
            "&longitude=%s" +
            "&type=film" +
            "&page_size=10" +
            "&page_number=%s";

    public static String getUrl(
        String what,
        Double latitude,
        Double longitude,
        Integer pageNumber
    ) {
        return
            String.format(
                SEARCH_UK_LONDON_FILMS,
                what,
                latitude,
                longitude,
                pageNumber
            );
    }
}
