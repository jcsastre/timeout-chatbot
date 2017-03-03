package com.timeout.chatbot.session;

import com.timeout.chatbot.configuration.MessengerConfiguration;
import com.timeout.chatbot.domain.FbUserProfile;
import com.timeout.chatbot.domain.page.Page;
import com.timeout.chatbot.domain.page.PageUid;
import com.timeout.chatbot.domain.user.User;
import com.timeout.chatbot.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Component
public class SessionPool {

    private final MessengerConfiguration messengerConfiguration;
    private final RestTemplate restTemplate;
    private final List<Session> sessions = new ArrayList<>();
    private final UserRepository userRepository;

    @Autowired
    public SessionPool(
        MessengerConfiguration messengerConfiguration,
        RestTemplate restTemplate,
        UserRepository userRepository
    ) {
        this.messengerConfiguration = messengerConfiguration;
        this.restTemplate = restTemplate;
        this.userRepository = userRepository;
    }

    public Session getSession(
        PageUid pageUid,
        String userId
    ) {
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

    private Session buildSession(PageUid pageUid, String userId) {
        final User user = buildUser(userId);
        final Page page = new Page(pageUid);

        final Session session =
            new Session(
                page,
                user
            );

        session.setLastAccessTime(System.currentTimeMillis());

        return session;
    }

    private Session findSession(PageUid pageUid, String userId) {
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
            user = new User(UUID.randomUUID(), messengerId);
            userRepository.save(user);
        }

        final String url =
            "https://graph.facebook.com/v2.6/" + messengerId +
                "?fields=first_name,last_name,profile_pic,locale,timezone,gender&access_token=" +
                messengerConfiguration.getPageAccessToken();
        final FbUserProfile fbUserProfile = restTemplate.getForObject(url, FbUserProfile.class);

//        user.setFbUserProfile(fbUserProfile);

        return user;
    }
}
