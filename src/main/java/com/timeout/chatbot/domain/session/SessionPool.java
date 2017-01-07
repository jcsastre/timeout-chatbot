package com.timeout.chatbot.domain.session;

import com.timeout.chatbot.blocks.MainOptionsSendBlock;
import com.timeout.chatbot.blocks.RestaurantSummarySendBlock;
import com.timeout.chatbot.blocks.RestaurantsPageSendBlock;
import com.timeout.chatbot.blocks.WelcomeMessageSendBlock;
import com.timeout.chatbot.config.properties.MessengerConfiguration;
import com.timeout.chatbot.domain.FbUserProfile;
import com.timeout.chatbot.domain.Page;
import com.timeout.chatbot.domain.User;
import com.timeout.chatbot.messenger4j.send.MessengerSendClientWrapper;
import com.timeout.chatbot.repository.UserRepository;
import com.timeout.chatbot.services.ApiAiService;
import com.timeout.chatbot.services.GraffittiService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

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
    private final MainOptionsSendBlock mainOptionsSendBlock;
    private final RestaurantsPageSendBlock restaurantsPageSendBlock;

    private final UserRepository userRepository;

    @Autowired
    public SessionPool(
        MessengerConfiguration messengerConfiguration,
        RestTemplate restTemplate,
        GraffittiService campingpongAPIService,
        ApiAiService apiAiService,
        MessengerSendClientWrapper messengerSendClientWrapper,
        WelcomeMessageSendBlock welcomeMessageSendBlock,
        RestaurantSummarySendBlock restaurantSummarySendBlock,
        MainOptionsSendBlock mainOptionsSendBlock,
        RestaurantsPageSendBlock restaurantsPageSendBlock,
        UserRepository userRepository
    ) {
        this.messengerConfiguration = messengerConfiguration;
        this.restTemplate = restTemplate;
        this.campingpongAPIService = campingpongAPIService;
        this.apiAiService = apiAiService;
        this.messengerSendClientWrapper = messengerSendClientWrapper;
        this.welcomeMessageSendBlock = welcomeMessageSendBlock;
        this.restaurantSummarySendBlock = restaurantSummarySendBlock;
        this.mainOptionsSendBlock = mainOptionsSendBlock;
        this.restaurantsPageSendBlock = restaurantsPageSendBlock;
        this.userRepository = userRepository;
    }

    public Session getSession(String pageUid, String userId) {
        Session session = findSession(pageUid, userId);

        if (session != null) {
            final long lastAccessTime = session.getLastAccessTime();
            final long currentTimeMillis = System.currentTimeMillis();
            if (currentTimeMillis - lastAccessTime < TimeUnit.MINUTES.toMillis(1)) {
                return session;
            } else {
                sessions.remove(session);
            }
        }

        session = buildSession(pageUid, userId);

        sessions.add(session);

        return session;
    }

    private Session buildSession(String pageUid, String userId) {
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
                mainOptionsSendBlock,
                restaurantSummarySendBlock,
                restaurantsPageSendBlock,
                userRepository
            );

        return session;
    }

    private Session findSession(String pageUid, String userId) {
        synchronized (sessions) {
            for (Session session : sessions) {
                if (
                    session.getPage().getUid().equals(pageUid) &&
                    session.getUser().getMessengerId().equals(userId)
                ) {
                    return session;
                }
            }
        }

        return null;
    }

    private User buildUser(String userId) {

        final String url =
            "https://graph.facebook.com/v2.6/" + userId +
                "?fields=first_name,last_name,profile_pic,locale,timezone,gender&access_token=" +
                messengerConfiguration.getPageAccessToken();

        final FbUserProfile fbUserProfile = restTemplate.getForObject(url, FbUserProfile.class);

        final User user = new User(userId);
        user.setFbUserProfile(fbUserProfile);

        return user;
    }
}
