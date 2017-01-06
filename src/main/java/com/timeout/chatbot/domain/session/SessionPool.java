package com.timeout.chatbot.domain.session;

import com.timeout.chatbot.config.properties.MessengerConfiguration;
import com.timeout.chatbot.domain.messenger.Page;
import com.timeout.chatbot.domain.messenger.User;
import com.timeout.chatbot.messenger4j.send.MessengerSendClientWrapper;
import com.timeout.chatbot.platforms.messenger.domain.UserProfile;
import com.timeout.chatbot.platforms.messenger.send.blocks.RestaurantSummarySendBlock;
import com.timeout.chatbot.platforms.messenger.send.blocks.RestaurantsPageSendBlock;
import com.timeout.chatbot.platforms.messenger.send.blocks.WelcomeMessageSendBlock;
import com.timeout.chatbot.services.ApiAiService;
import com.timeout.chatbot.services.GraffittiService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;

@Component
public class SessionPool {
    private final MessengerConfiguration messengerConfiguration;

    private final RestTemplate restTemplate;

    private final GraffittiService campingpongAPIService;

    private final ApiAiService apiAiService;

    private final MessengerSendClientWrapper messengerSendClientWrapper;

    private final List<Session> sessions = new ArrayList<>();

    private final WelcomeMessageSendBlock welcomeMessageSendBlock;

    private final RestaurantSummarySendBlock restaurantSummarySendBlock;
    private final RestaurantsPageSendBlock restaurantsPageSendBlock;

    @Autowired
    public SessionPool(
        MessengerConfiguration messengerConfiguration,
        RestTemplate restTemplate,
        GraffittiService campingpongAPIService,
        ApiAiService apiAiService,
        MessengerSendClientWrapper messengerSendClientWrapper,
        WelcomeMessageSendBlock welcomeMessageSendBlock,
        RestaurantSummarySendBlock restaurantSummarySendBlock,
        RestaurantsPageSendBlock restaurantsPageSendBlock
    ) {
        this.messengerConfiguration = messengerConfiguration;
        this.restTemplate = restTemplate;
        this.campingpongAPIService = campingpongAPIService;
        this.apiAiService = apiAiService;
        this.messengerSendClientWrapper = messengerSendClientWrapper;
        this.welcomeMessageSendBlock = welcomeMessageSendBlock;
        this.restaurantSummarySendBlock = restaurantSummarySendBlock;
        this.restaurantsPageSendBlock = restaurantsPageSendBlock;
    }

    public Session getSession(String pageUid, String userId) {
        synchronized (sessions) {
            for (Session session : sessions) {
                if (
                    session.getPage().getUid().equals(pageUid) &&
                    session.getUser().getUid().equals(userId)
                ) {
                    return session;
                }
            }
        }

        final User user = buildUser(userId);
        final Page page = new Page(pageUid);

        final Session session =
            new Session(
                restTemplate,
                campingpongAPIService,
                apiAiService,
                messengerSendClientWrapper,
                page,
                user,
                welcomeMessageSendBlock,
                restaurantSummarySendBlock,
                restaurantsPageSendBlock
            );

        sessions.add(session);

        return session;
    }

    private User buildUser(String userId) {
        final User user = new User(userId);

        final String url =
            "https://graph.facebook.com/v2.6/" + userId +
            "?fields=first_name,last_name,profile_pic,locale,timezone,gender&access_token=" +
            messengerConfiguration.getPageAccessToken();

        final UserProfile userProfile = restTemplate.getForObject(url, UserProfile.class);
        user.setUserProfile(userProfile);

        return user;
    }
}
