package com.timeout.chatbot.graffitti.uri;

import com.timeout.chatbot.configuration.TimeoutConfiguration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class GraffittiEndpoints {

    private String facetsV4;
    private String facetsV5;
    private String venues;

    @Autowired
    public GraffittiEndpoints(
        TimeoutConfiguration timeoutConfiguration
    ) {
        facetsV4 =
            "http://graffiti.timeout.com/v1/sites/" +
                timeoutConfiguration.getSite() +
                "/facet-groups/browsing-v4?" +
                "locale=" + timeoutConfiguration.getLocale();

        facetsV5 =
            "http://graffiti.timeout.com/v1/sites/" +
            timeoutConfiguration.getSite() +
            "/facet-groups/browsing-v5?" +
            "locale=" + timeoutConfiguration.getLocale();

        venues =
            "http://graffiti.timeout.com/v1/sites/" +
            timeoutConfiguration.getSite() +
            "/venues/";
    }

    public String getFacetsV5() {
        return facetsV5;
    }

    public String getFacetsV4() {
        return facetsV4;
    }

    public String getVenues() {
        return venues;
    }

    //    SEARCH_UK_LONDON_VENUES(
//        "http://graffiti.timeout.com/v1/sites/uk-london/search?" +
//            "locale=en-GB" +
//            "&what=%s" +
//            "&type=venue" +
//            "&page_size=10" +
//            "&page_number=%s"
//    ),
//    SEARCH_UK_LONDON_FILMS(
//        "http://graffiti.timeout.com/v1/sites/uk-london/search?" +
//            "&locale=en-GB" +
//            "sort=published-at" +
//            "&when=today" +
//            "&what=%s" +
//            "&latitude=%s" +
//            "&longitude=%s" +
//            "&type=film" +
//            "&page_size=10" +
//            "&page_number=%s"
//    ),
//    HOME("http://graffiti.timeout.com/v1/tiles?site=uk-london&locale=en-GB&tile_type=website&tile_type=discover"),
//    HOME("http://graffiti.timeout.com/v1/tiles?site=uk-london&locale=en-GB&tile_type=discover"),
}
