package com.timeout.chatbot.graffitti.urlbuilder;

import com.timeout.chatbot.configuration.TimeoutConfiguration;
import com.timeout.chatbot.graffitti.uri.GraffittiQueryParameterType;
import io.mikael.urlbuilder.UrlBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class TilesDiscoverUrlBuilder {

    private final TimeoutConfiguration timeoutConfiguration;

    @Autowired
    public TilesDiscoverUrlBuilder(
        TimeoutConfiguration timeoutConfiguration
    ) {
        this.timeoutConfiguration = timeoutConfiguration;
    }

    public UrlBuilder build() {
        return
            UrlBuilder.empty().withScheme(SCHEMA).withHost(HOST).withPath(PATH)
                .addParameter(GraffittiQueryParameterType.SITE.getValue(), timeoutConfiguration.getSite())
                .addParameter(GraffittiQueryParameterType.LOCALE.getValue(), timeoutConfiguration.getLocale())
                .addParameter(GraffittiQueryParameterType.TILE_TYPE.getValue(), "discover");
    }

    private static final String SCHEMA = "http";
    private static final String HOST = "graffiti.timeout.com";
    private static final String PATH = "v1/tiles";
}
