package com.timeout.chatbot.session;

import com.timeout.chatbot.configuration.MessengerConfiguration;
import com.timeout.chatbot.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;

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

//    @Async
//    public ListenableFuture<Session> getSession(
//        PageUid pageUid,
//        String userId
//    ) {
//        Session session = findSession(pageUid, userId);
//
//        if (session != null) {
//            final long lastAccessTime = session.getLastAccessTime();
//            final long currentTimeMillis = System.currentTimeMillis();
//            if (currentTimeMillis - lastAccessTime < 15*60*1000) {
//                session.setLastAccessTime(currentTimeMillis);
//                return new AsyncResult<>(session);
//            } else {
//                sessions.remove(session);
//            }
//        }
//
//        session = buildSession(pageUid, userId);
//
//        sessions.add(session);
//
//        return new AsyncResult<>(session);
//    }

//    private Session buildSession(PageUid pageUid, String userId) {
//        final User user = buildUser(userId);
//        final Page PAGE = new Page(pageUid);
//
//        final Session session =
//            new Session(
//                PAGE,
//                user
//            );
//
//        final String url =
//            "https://graph.facebook.com/v2.6/" + user.messengerId +
//                "?fields=first_name,last_name,profile_pic,locale,timezone,gender&access_token=" +
//                messengerConfiguration.getPageAccessToken();
//
//        session.setFbUserProfile(
//            restTemplate.getForObject(url, FbUserProfile.class)
//        );
//        session.setLastAccessTime(System.currentTimeMillis());
//
//        return session;
//    }

    private Session findSession(String pageId, String userId) {
        synchronized (sessions) {
            for (Session session : sessions) {
                if (
                    session.page.id.equals(pageId) &&
                    session.user.messengerId.equals(userId)
                ) {
                    return session;
                }
            }
        }

        return null;
    }

//    private User buildUser(String messengerId) {
//
////        User user = userRepository.findByMessengerId(messengerId);
////
////        if (user == null) {
////            user = new User(UUID.randomUUID().toString(), messengerId);
////            userRepository.save(user);
////        }
////
////        return user;
//    }
}
