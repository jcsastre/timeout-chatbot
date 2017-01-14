package com.timeout.chatbot;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@ConfigurationProperties(prefix = "apiai")
@Component
public class ApiaiConfiguration {

    private String clientAccessToken;

    public String getClientAccessToken() {
        return clientAccessToken;
    }

    public void setClientAccessToken(String clientAccessToken) {
        this.clientAccessToken = clientAccessToken;
    }
}
