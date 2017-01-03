package com.timeout.chatbot.config;

import ai.api.AIConfiguration;
import ai.api.AIDataService;
import com.github.messenger4j.MessengerPlatform;
import com.github.messenger4j.send.MessengerSendClient;
import com.timeout.chatbot.http.HeaderRequestInterceptor;
import com.timeout.chatbot.messenger4j.send.MessengerSendClientWrapper;
import com.timeout.chatbot.platforms.messenger.send.blocks.WelcomeMessageSendBlock;
import com.timeout.chatbot.services.GraffittiService;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;

@Configuration
@EnableConfigurationProperties
@ConfigurationProperties
public class ApplicationConfig {

    @Bean
    public WelcomeMessageSendBlock welcomeMessageSendBlock() {
        return
            new WelcomeMessageSendBlock(
                graffittiService(),
                messengerSendClientWrapper()
            );
    }

    @Bean
    public GraffittiService graffittiService() {
        return new GraffittiService();
    }

    @Bean
    public RestTemplate restTemplate() {
        List<ClientHttpRequestInterceptor> interceptors = new ArrayList<ClientHttpRequestInterceptor>();
        interceptors.add(
            new HeaderRequestInterceptor(
                "Authorization",
                "Bearer 8EOpBX2cpcZkCf3l7bBh476rzlpRtcKPzZVv4t1TGNMu24OIs1lhDMhUIVAil-9q"
            )
        );

        RestTemplate restTemplate = new RestTemplate();
        restTemplate.setInterceptors(interceptors);

        return restTemplate;
    }

    @Bean
    public MessengerSendClientWrapper messengerSendClientWrapper() {
        final MessengerSendClient messengerSendClient = MessengerPlatform.newSendClientBuilder(
            getMessenger().getApp().getPageAccessToken()
        ).build();

        return new MessengerSendClientWrapper(messengerSendClient);
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
