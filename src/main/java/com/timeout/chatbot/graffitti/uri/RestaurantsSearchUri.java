package com.timeout.chatbot.graffitti.uri;

import com.timeout.chatbot.graffitti.domain.GraffittiType;
import io.mikael.urlbuilder.UrlBuilder;

import java.net.URI;

public class RestaurantsSearchUri extends SearchUri {

    private static final String WHAT ="node-7083";

    public static URI buildNonGeolocatedUri(
        Integer pageNumber
    ) {
        return
            UrlBuilder.empty().withScheme(SCHEMA).withHost(HOST).withPath(PATH)
                .addParameter(GraffittiQueryParameterType.LOCALE.getValue(), LOCALE)
                .addParameter(GraffittiQueryParameterType.TYPE.getValue(), GraffittiType.VENUE.toString())
                .addParameter(GraffittiQueryParameterType.WHAT.getValue(), WHAT)
                .addParameter(GraffittiQueryParameterType.PAGE_SIZE.getValue(), PAGE_SIZE)
                .addParameter(GraffittiQueryParameterType.PAGE_NUMBER.getValue(), pageNumber.toString())
                .toUri();
    }

    public static URI buildGeolocatedUri(
        Double latitude,
        Double longitude,
        Integer pageNumber
    ) {
        return
            UrlBuilder.empty().withScheme(SCHEMA).withHost(HOST).withPath(PATH)
                .addParameter(GraffittiQueryParameterType.LOCALE.getValue(), LOCALE)
                .addParameter(GraffittiQueryParameterType.TYPE.getValue(), GraffittiType.VENUE.toString())
                .addParameter(GraffittiQueryParameterType.WHAT.getValue(), WHAT)
                .addParameter(GraffittiQueryParameterType.LATITUDE.getValue(), latitude.toString())
                .addParameter(GraffittiQueryParameterType.LONGITUDE.getValue(), longitude.toString())
                .addParameter(GraffittiQueryParameterType.PAGE_SIZE.getValue(), PAGE_SIZE)
                .addParameter(GraffittiQueryParameterType.PAGE_NUMBER.getValue(), pageNumber.toString())
                .toUri();
    }
}
