package com.timeout.chatbot.graffitti.uri;

import com.timeout.chatbot.graffitti.domain.GraffittiType;
import io.mikael.urlbuilder.UrlBuilder;

import java.net.URI;

public class FilmsEndpoint extends SearchUri {

    public static URI buildGeolocatedUri(
        Double latitude,
        Double longitude,
        Integer pageNumber
    ) {
        return
            UrlBuilder.empty().withScheme(SCHEMA).withHost(HOST).withPath(PATH)
                .addParameter(GraffittiQueryParameterType.LOCALE.getValue(), LOCALE)
                .addParameter(GraffittiQueryParameterType.TYPE.getValue(), GraffittiType.FILM.toString())
                .addParameter(GraffittiQueryParameterType.WHAT.getValue(), "node-7073")
                .addParameter(GraffittiQueryParameterType.WHEN.getValue(), "today")
                .addParameter(GraffittiQueryParameterType.LATITUDE.getValue(), latitude.toString())
                .addParameter(GraffittiQueryParameterType.LONGITUDE.getValue(), longitude.toString())
                .addParameter(GraffittiQueryParameterType.PAGE_SIZE.getValue(), PAGE_SIZE)
                .addParameter(GraffittiQueryParameterType.PAGE_NUMBER.getValue(), pageNumber.toString())
                .addParameter(GraffittiQueryParameterType.SORT.getValue(), "published-at")
                .toUri();
    }
}
