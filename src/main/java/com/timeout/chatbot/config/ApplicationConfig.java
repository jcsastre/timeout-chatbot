package com.timeout.chatbot.config;

import ai.api.AIConfiguration;
import ai.api.AIDataService;
import com.github.messenger4j.MessengerPlatform;
import com.github.messenger4j.send.MessengerSendClient;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties
@ConfigurationProperties
public class ApplicationConfig {

    @Bean
    public MessengerSendClient messengerSendClient() {
        return
            MessengerPlatform.newSendClientBuilder(
                getMessenger().getApp().getPageAccessToken()
            ).build();
    }

    @Bean
    public AIDataService aIDataService() {
        AIConfiguration aiConfiguration = new AIConfiguration(getApiAi().getClientAccessToken());
        return new AIDataService(aiConfiguration);
    }

    private ApiAi apiAi;

    public ApiAi getApiAi() {
        return apiAi;
    }

    public void setApiAi(ApiAi apiAi) {
        this.apiAi = apiAi;
    }

    public static class ApiAi {
        private String clientAccessToken;

        public String getClientAccessToken() {
            return clientAccessToken;
        }

        public void setClientAccessToken(String clientAccessToken) {
            this.clientAccessToken = clientAccessToken;
        }
    }


    private Messenger messenger;

    public Messenger getMessenger() {
        return messenger;
    }

    public void setMessenger(Messenger messenger) {
        this.messenger = messenger;
    }

    public static class Messenger {
        private App app;

        public App getApp() {
            return app;
        }

        public void setApp(App app) {
            this.app = app;
        }

        public static class App {
            private String pageAccessToken;
            private String secret;
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
    }
}
