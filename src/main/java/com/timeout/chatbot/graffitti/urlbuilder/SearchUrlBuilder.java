package com.timeout.chatbot.graffitti.urlbuilder;

import com.timeout.chatbot.graffitti.uri.GraffittiQueryParameterType;
import io.mikael.urlbuilder.UrlBuilder;

public class SearchUrlBuilder {

    private static final String SCHEMA = "http";
    private static final String HOST = "graffiti.timeout.com";
    private static final String PATH = "v1/sites/uk-london/search";

    protected static final String LOCALE ="en-GB";
    protected static final String PAGE_SIZE = "10";

    public static UrlBuilder buildBase(
        String what,
        String type,
        Integer pageNumber
    ) {
        return
            UrlBuilder.empty().withScheme(SCHEMA).withHost(HOST).withPath(PATH)
                .addParameter(GraffittiQueryParameterType.LOCALE.getValue(), LOCALE)
                .addParameter(GraffittiQueryParameterType.WHAT.getValue(), what)
                .addParameter(GraffittiQueryParameterType.TYPE.getValue(), type)
                .addParameter(GraffittiQueryParameterType.PAGE_SIZE.getValue(), PAGE_SIZE)
                .addParameter(GraffittiQueryParameterType.PAGE_NUMBER.getValue(), pageNumber.toString());
    }

    public static UrlBuilder buildWithGeolocation(
        String what,
        String type,
        Integer pageNumber,
        Double latitude,
        Double longitude
    ) {
        return
            buildBase(what, type, pageNumber)
                .addParameter(
                    GraffittiQueryParameterType.LATITUDE.getValue(),
                    latitude.toString()
                )
                .addParameter(
                    GraffittiQueryParameterType.LONGITUDE.getValue(),
                    longitude.toString()
                );
    }

    protected static UrlBuilder buildWithWhere(
        String what,
        String type,
        Integer pageNumber,
        String where
    ) {
        return
            buildBase(what, type, pageNumber)
                .addParameter(
                    GraffittiQueryParameterType.WHERE.getValue(),
                    where
                );
    }
}
