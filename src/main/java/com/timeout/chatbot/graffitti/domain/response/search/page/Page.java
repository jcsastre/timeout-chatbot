package com.timeout.chatbot.graffitti.domain.response.search.page;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Page {

    private Integer number;
    @JsonProperty("requested_size")
    private Integer requestedSize;
    private Integer size;
    @JsonProperty("next_url")
    private String nextUrl;
    @JsonProperty("previous_url")
    private String previousUrl;

    public Integer getNumber() {
        return number;
    }

    public void setNumber(Integer number) {
        this.number = number;
    }

    public Integer getRequestedSize() {
        return requestedSize;
    }

    public void setRequestedSize(Integer requestedSize) {
        this.requestedSize = requestedSize;
    }

    public Integer getSize() {
        return size;
    }

    public void setSize(Integer size) {
        this.size = size;
    }

    public String getNextUrl() {
        return nextUrl;
    }

    public void setNextUrl(String nextUrl) {
        this.nextUrl = nextUrl;
    }

    public String getPreviousUrl() {
        return previousUrl;
    }

    public void setPreviousUrl(String previousUrl) {
        this.previousUrl = previousUrl;
    }
}
