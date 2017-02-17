package com.timeout.chatbot.graffitti.uri;

import io.mikael.urlbuilder.UrlBuilder;

import java.net.URI;

public class SearchUri {

    protected static final String SCHEMA = "http";
    protected static final String HOST = "graffiti.timeout.com";
    protected static final String PATH = "v1/sites/uk-london/search";

    protected static final String LOCALE ="en-GB";
    protected static final String PAGE_SIZE = "10";

    protected static URI buildBaseNonGeolocatedUri(
        String locale,
        String what,
        String type,
        Integer pageSize,
        Integer pageNumber
    ) {
        return
            UrlBuilder.empty().withScheme(SCHEMA).withHost(HOST).withPath(PATH)
                .addParameter(GraffittiQueryParameterType.LOCALE.getValue(), locale)
                .addParameter(GraffittiQueryParameterType.WHAT.getValue(), what)
                .addParameter(GraffittiQueryParameterType.TYPE.getValue(), type)
                .addParameter(GraffittiQueryParameterType.PAGE_SIZE.getValue(), pageSize.toString())
                .addParameter(GraffittiQueryParameterType.PAGE_NUMBER.getValue(), pageNumber.toString())
                .toUri();
    }

    protected static URI buildBaseGeolocatedUri(
        String locale,
        String what,
        String type,
        Integer pageSize,
        Integer pageNumber,
        Double latitude,
        Double longitude
    ) {
        return
            UrlBuilder.empty().withScheme(SCHEMA).withHost(HOST).withPath(PATH)
                .addParameter(GraffittiQueryParameterType.LOCALE.getValue(), locale)
                .addParameter(GraffittiQueryParameterType.WHAT.getValue(), what)
                .addParameter(GraffittiQueryParameterType.TYPE.getValue(), type)
                .addParameter(GraffittiQueryParameterType.PAGE_SIZE.getValue(), pageSize.toString())
                .addParameter(GraffittiQueryParameterType.PAGE_NUMBER.getValue(), pageNumber.toString())
                .addParameter(
                    GraffittiQueryParameterType.LATITUDE.getValue(),
                    latitude.toString()
                )
                .addParameter(
                    GraffittiQueryParameterType.LONGITUDE.getValue(),
                    longitude.toString()
                )
                .toUri();
    }
}
