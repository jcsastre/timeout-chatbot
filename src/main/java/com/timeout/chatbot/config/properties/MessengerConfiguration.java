package com.timeout.chatbot.config.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@ConfigurationProperties(prefix = "messenger")
@Component
public class MessengerConfiguration {

    private String secret;
    private String pageAccessToken;
    private String webhookVerificationToken;

    public String getPageAccessToken() {
        return pageAccessToken;
    }

    public void setPageAccessToken(String pageAccessToken) {
        this.pageAccessToken = pageAccessToken;
    }

    public String getSecret() {
        return secret;
    }

    public void setSecret(String secret) {
        this.secret = secret;
    }

    public String getWebhookVerificationToken() {
        return webhookVerificationToken;
    }

    public void setWebhookVerificationToken(String webhookVerificationToken) {
        this.webhookVerificationToken = webhookVerificationToken;
    }
}
