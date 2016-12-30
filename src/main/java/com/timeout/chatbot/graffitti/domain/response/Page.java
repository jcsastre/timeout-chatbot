package com.timeout.chatbot.graffitti.domain.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Page {

    @JsonProperty
    private Integer number;

    @JsonProperty
    private Integer requested_size;

    @JsonProperty
    private Integer size;

    @JsonProperty
    private String next_url;

    @Override
    public String toString() {
        return
            "number='" + number + "', " +
            "requested_size='" + requested_size + "', " +
            "size='" + size + "', " +
            "next_url=" + next_url;
    }
}
