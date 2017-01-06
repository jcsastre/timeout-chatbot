package com.timeout.chatbot.config;

import ai.api.AIConfiguration;
import ai.api.AIDataService;
import com.github.messenger4j.MessengerPlatform;
import com.github.messenger4j.send.MessengerSendClient;
import com.timeout.chatbot.config.properties.ApiaiConfiguration;
import com.timeout.chatbot.config.properties.MessengerConfiguration;
import com.timeout.chatbot.http.HeaderRequestInterceptor;
import com.timeout.chatbot.messenger4j.send.MessengerSendClientWrapper;
import com.timeout.chatbot.platforms.messenger.send.blocks.WelcomeMessageSendBlock;
import com.timeout.chatbot.services.GraffittiService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;

@Configuration
public class ApplicationConfig {

    private final MessengerConfiguration messengerConfiguration;
    private final ApiaiConfiguration apiaiConfiguration;

    @Autowired
    public ApplicationConfig(
        MessengerConfiguration messengerConfiguration,
        ApiaiConfiguration apiaiConfiguration
    ) {
        this.messengerConfiguration = messengerConfiguration;
        this.apiaiConfiguration = apiaiConfiguration;
    }

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
        return new GraffittiService(restTemplate());
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
            messengerConfiguration.getPageAccessToken()
        ).build();

        return new MessengerSendClientWrapper(messengerSendClient);
    }

    @Bean
    public AIDataService aIDataService() {
        AIConfiguration aiConfiguration = new AIConfiguration(apiaiConfiguration.getClientAccessToken());
        return new AIDataService(aiConfiguration);
    }
}
