package com.timeout.chatbot.graffitti.urlbuilder;

import com.timeout.chatbot.configuration.TimeoutConfiguration;
import com.timeout.chatbot.graffitti.uri.GraffittiQueryParameterType;
import io.mikael.urlbuilder.UrlBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class VenuesUrlBuilder {

    private static final String SCHEMA = "http";
    private static final String HOST = "graffiti.timeout.com";

    private String path;
    private String locale;

    @Autowired
    public VenuesUrlBuilder(
        TimeoutConfiguration timeoutConfiguration
    ) {
        path = "v1/sites/" + timeoutConfiguration.getSite() + "/venues";
        locale = timeoutConfiguration.getLocale();
    }

    public UrlBuilder build(
        String venueId
    ) {
        return
            UrlBuilder.empty().withScheme(SCHEMA).withHost(HOST).withPath(path + "/" + venueId)
                .addParameter(GraffittiQueryParameterType.LOCALE.getValue(), locale);
    }

    public UrlBuilder buildImages(
        String venueId
    ) {
        return
            UrlBuilder.empty().withScheme(SCHEMA).withHost(HOST).withPath(path + "/" + venueId + "/images")
                .addParameter(GraffittiQueryParameterType.LOCALE.getValue(), locale);
    }
}
