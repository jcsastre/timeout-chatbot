package com.timeout.chatbot.graffitti.domain.response.venues;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Meta {
    private static final Logger log = LoggerFactory.getLogger(Meta.class);

    private String url;

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    @Override
    public String toString() {
        return
            "{" +
                "url= "+ url +
            '}';
    }
}
