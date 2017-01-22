package com.timeout.chatbot.session;

import com.timeout.chatbot.MessengerConfiguration;
import com.timeout.chatbot.block.booking.BookingBlocksHelper;
import com.timeout.chatbot.domain.FbUserProfile;
import com.timeout.chatbot.domain.Page;
import com.timeout.chatbot.domain.User;
import com.timeout.chatbot.messenger4j.send.MessengerSendClientWrapper;
import com.timeout.chatbot.repository.UserRepository;
import com.timeout.chatbot.services.ApiAiService;
import com.timeout.chatbot.services.BlockService;
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
    private final BlockService blockService;
    private final UserRepository userRepository;
    private final BookingBlocksHelper bookingBlocksHelper;

    @Autowired
    public SessionPool(
        MessengerConfiguration messengerConfiguration,
        RestTemplate restTemplate,
        GraffittiService campingpongAPIService,
        ApiAiService apiAiService,
        MessengerSendClientWrapper messengerSendClientWrapper,
        BlockService blockService,
        UserRepository userRepository,
        BookingBlocksHelper bookingBlocksHelper
    ) {
        this.messengerConfiguration = messengerConfiguration;
        this.restTemplate = restTemplate;
        this.campingpongAPIService = campingpongAPIService;
        this.apiAiService = apiAiService;
        this.messengerSendClientWrapper = messengerSendClientWrapper;
        this.blockService = blockService;
        this.userRepository = userRepository;
        this.bookingBlocksHelper = bookingBlocksHelper;
    }

    public Session getSession(String pageUid, String userId) {
        Session session = findSession(pageUid, userId);

        if (session != null) {
            final long lastAccessTime = session.getLastAccessTime();
            final long currentTimeMillis = System.currentTimeMillis();
            if (currentTimeMillis - lastAccessTime < 15*60*1000) {
                session.setLastAccessTime(currentTimeMillis);
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
                blockService,
                userRepository,
                bookingBlocksHelper,
                lookingContext);

        session.setLastAccessTime(System.currentTimeMillis());

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

    private User buildUser(String messengerId) {
        User user = userRepository.findByMessengerId(messengerId);

        if (user == null) {
            user = new User(messengerId);
            userRepository.save(user);
        }

        final String url =
            "https://graph.facebook.com/v2.6/" + messengerId +
                "?fields=first_name,last_name,profile_pic,locale,timezone,gender&access_token=" +
                messengerConfiguration.getPageAccessToken();
        final FbUserProfile fbUserProfile = restTemplate.getForObject(url, FbUserProfile.class);

        user.setFbUserProfile(fbUserProfile);

        return user;
    }
}
