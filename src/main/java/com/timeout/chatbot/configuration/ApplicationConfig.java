package com.timeout.chatbot.configuration;

import ai.api.AIConfiguration;
import ai.api.AIDataService;
import com.cloudinary.Cloudinary;
import com.github.messenger4j.MessengerPlatform;
import com.github.messenger4j.send.MessengerSendClient;
import com.timeout.chatbot.ApiaiConfiguration;
import com.timeout.chatbot.http.AsyncHeaderRequestInterceptor;
import com.timeout.chatbot.http.HeaderRequestInterceptor;
import com.timeout.chatbot.messenger4j.send.MessengerSendClientWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.AsyncClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.web.client.AsyncRestTemplate;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
    public Cloudinary cloudinary() {
        Map config = new HashMap();
        config.put("cloud_name", "drdyx3lpb");
        config.put("api_key", "414418524479882");
        config.put("api_secret", "iY0YAPSOo-xztbHp-42UO2BS_t4");

        return
            new Cloudinary(config);
    }

    @Bean
    public RestTemplate restTemplate() {
        List<ClientHttpRequestInterceptor> interceptors = new ArrayList<>();
        final HeaderRequestInterceptor interceptor = new HeaderRequestInterceptor(
            "Authorization",
            "Bearer N492pjDHiJ3_I_ZX7FIxUjJ2Gagwt9-Eq_HGcK7FVUQu24OIs1lhDMhUIVAil-9q"
        );
        interceptors.add(interceptor);

        RestTemplate restTemplate = new RestTemplate();
        restTemplate.setInterceptors(interceptors);

        return restTemplate;
    }

    @Bean
    public AsyncRestTemplate asyncRestTemplate() {
        List<AsyncClientHttpRequestInterceptor> interceptors = new ArrayList<>();
        final AsyncHeaderRequestInterceptor interceptor = new AsyncHeaderRequestInterceptor(
            "Authorization",
            "Bearer N492pjDHiJ3_I_ZX7FIxUjJ2Gagwt9-Eq_HGcK7FVUQu24OIs1lhDMhUIVAil-9q"
        );
        interceptors.add(interceptor);

        AsyncRestTemplate asyncRestTemplate = new AsyncRestTemplate();
        asyncRestTemplate.setInterceptors(interceptors);

        return asyncRestTemplate;
    }

    @Bean
    public MessengerSendClient messengerSendClient() {
        return
            MessengerPlatform.newSendClientBuilder(
                messengerConfiguration.getPageAccessToken()
            ).build();
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
