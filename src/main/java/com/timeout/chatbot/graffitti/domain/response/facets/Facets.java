package com.timeout.chatbot.graffitti.domain.response.facets;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Facets {

    private What what;

    public What getWhat() {
        return what;
    }

    public void setWhat(What what) {
        this.what = what;
    }
}
