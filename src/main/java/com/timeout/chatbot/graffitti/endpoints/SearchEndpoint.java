package com.timeout.chatbot.graffitti.endpoints;

import io.mikael.urlbuilder.UrlBuilder;

import javax.validation.constraints.NotNull;
import java.net.MalformedURLException;
import java.net.URL;

public class SearchEndpoint {

    private static final String SCHEMA = "http";
    private static final String HOST = "graffiti.timeout.com";
    private static final String PATH = "v1/sites/uk-london/search";

    public static URL buildNonGeolocatedUrl(
        @NotNull String locale,
        @NotNull String what,
        @NotNull String type,
        @NotNull Integer pageSize,
        @NotNull Integer pageNumber
    ) {

        final UrlBuilder urlBuilder = UrlBuilder.empty()
            .withScheme(SCHEMA)
            .withHost(HOST)
            .withPath(PATH)
            .addParameter(GraffittiQueryParameterType.LOCALE.getValue(), locale)
            .addParameter(GraffittiQueryParameterType.WHAT.getValue(), what)
            .addParameter(GraffittiQueryParameterType.TYPE.getValue(), type)
            .addParameter(GraffittiQueryParameterType.PAGE_SIZE.getValue(), pageSize.toString())
            .addParameter(GraffittiQueryParameterType.PAGE_NUMBER.getValue(), pageNumber.toString());

        try {
            return urlBuilder.toUri().toURL();
        } catch (MalformedURLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static URL buildGeolocatedUrl(
        @NotNull String locale,
        @NotNull String what,
        @NotNull String type,
        @NotNull Integer pageSize,
        @NotNull Integer pageNumber,
        @NotNull Double latitude,
        @NotNull Double longitude
    ) {

        final URL nonGeolocatedUrl = buildNonGeolocatedUrl(
            locale, what, type, pageSize, pageNumber
        );

        final UrlBuilder urlBuilder = UrlBuilder.fromUrl(nonGeolocatedUrl);

        urlBuilder.addParameter(
            GraffittiQueryParameterType.LATITUDE.getValue(),
            latitude.toString()
        );

        urlBuilder.addParameter(
            GraffittiQueryParameterType.LONGITUDE.getValue(),
            longitude.toString()
        );

        try {
            return urlBuilder.toUri().toURL();
        } catch (MalformedURLException e) {
            e.printStackTrace();
            return null;
        }
    }
}
