package com.timeout.chatbot.graffitti.uri;

public enum GraffittiEndpoints {
    SEARCH_UK_LONDON_VENUES(
        "http://graffiti.timeout.com/v1/sites/uk-london/search?" +
            "locale=en-GB" +
            "&what=%s" +
            "&type=venue" +
            "&page_size=10" +
            "&page_number=%s"
    ),
    SEARCH_UK_LONDON_FILMS(
        "http://graffiti.timeout.com/v1/sites/uk-london/search?" +
            "&locale=en-GB" +
            "sort=published-at" +
            "&when=today" +
            "&what=%s" +
            "&latitude=%s" +
            "&longitude=%s" +
            "&type=film" +
            "&page_size=10" +
            "&page_number=%s"
    ),
//    HOME("http://graffiti.timeout.com/v1/tiles?site=uk-london&locale=en-GB&tile_type=website&tile_type=discover"),
    HOME("http://graffiti.timeout.com/v1/tiles?site=uk-london&locale=en-GB&tile_type=discover"),
    FACETS("http://graffiti.timeout.com/v1/sites/uk-london/facet-groups/browsing-v5?locale=en-GB"),
    FACETS_V5("http://graffiti.timeout.com/v1/sites/uk-london/facet-groups/browsing-v5?locale=en-GB"),
    RESTAURANTS("http://graffiti.timeout.com/v1/sites/uk-london/search?locale=en-GB&what=node-7083&type=venue&page_size=10"),
    BARSANDPUBS("http://graffiti.timeout.com/v1/sites/uk-london/search?locale=en-GB&what=node-7067&type=venue&page_size=10"),
    VENUE("http://graffiti.timeout.com/v1/sites/uk-london/venues/");

    private final String url;

    GraffittiEndpoints(final String url) {
        this.url = url;
    }

    @Override
    public String toString() {
        return url;
    }
}
