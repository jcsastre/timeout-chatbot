package com.timeout.chatbot.graffitti.urlbuilder;

import com.timeout.chatbot.configuration.TimeoutConfiguration;
import com.timeout.chatbot.graffitti.uri.GraffittiQueryParameterType;
import io.mikael.urlbuilder.UrlBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class SearchUrlBuilder {

    private static final String SCHEMA = "http";
    private static final String HOST = "graffiti.timeout.com";

    private static final String PAGE_SIZE = "10";

    private String path;
    private String locale;

    @Autowired
    public SearchUrlBuilder(
        TimeoutConfiguration timeoutConfiguration
    ) {
        path = "v1/sites/" + timeoutConfiguration.getSite() + "/search";
        locale = timeoutConfiguration.getLocale();
    }

    public UrlBuilder buildBase(
        String what,
        String type,
        Integer pageNumber
    ) {
        return
            UrlBuilder.empty().withScheme(SCHEMA).withHost(HOST).withPath(path)
                .addParameter(GraffittiQueryParameterType.LOCALE.getValue(), locale)
                .addParameter(GraffittiQueryParameterType.WHAT.getValue(), what)
                .addParameter(GraffittiQueryParameterType.TYPE.getValue(), type)
                .addParameter(GraffittiQueryParameterType.PAGE_SIZE.getValue(), PAGE_SIZE)
                .addParameter(GraffittiQueryParameterType.PAGE_NUMBER.getValue(), pageNumber.toString())
                .addParameter(GraffittiQueryParameterType.VIEW.getValue(), "complete");
    }

//    public UrlBuilder buildWithGeolocation(
//        String what,
//        String type,
//        Integer pageNumber,
//        Double latitude,
//        Double longitude
//    ) {
//        return
//            buildBase(what, type, pageNumber)
//                .addParameter(
//                    GraffittiQueryParameterType.LATITUDE.getValue(),
//                    latitude.toString()
//                )
//                .addParameter(
//                    GraffittiQueryParameterType.LONGITUDE.getValue(),
//                    longitude.toString()
//                );
//    }
//
//    protected UrlBuilder buildWithWhere(
//        String what,
//        String type,
//        Integer pageNumber,
//        String where
//    ) {
//        return
//            buildBase(what, type, pageNumber)
//                .addParameter(
//                    GraffittiQueryParameterType.WHERE.getValue(),
//                    where
//                );
//    }
}