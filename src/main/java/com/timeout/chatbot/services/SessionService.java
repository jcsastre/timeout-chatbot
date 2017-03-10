package com.timeout.chatbot.services;

import com.timeout.chatbot.configuration.MessengerConfiguration;
import com.timeout.chatbot.domain.FbUserProfile;
import com.timeout.chatbot.domain.page.Page;
import com.timeout.chatbot.domain.user.User;
import com.timeout.chatbot.repository.SessionRepository;
import com.timeout.chatbot.repository.UserRepository;
import com.timeout.chatbot.session.Session;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.UUID;

@Service
public class SessionService {

    private static final Logger logger = LoggerFactory.getLogger(SessionService.class);

    private final MessengerConfiguration messengerConfiguration;
    private final SessionRepository sessionRepository;
    private final UserRepository userRepository;
    private final RestTemplate restTemplate;

    public SessionService(
        MessengerConfiguration messengerConfiguration,
        SessionRepository sessionRepository,
        UserRepository userRepository,
        RestTemplate restTemplate
    ) {
        this.messengerConfiguration = messengerConfiguration;
        this.sessionRepository = sessionRepository;
        this.userRepository = userRepository;
        this.restTemplate = restTemplate;
    }

    public Session getSession(
        String recipientId,
        String senderId
    ) {
        logger.debug("getSession -> getSession(" + recipientId + ", " + senderId + ")");

        logger.debug("getSession -> Looking at redis repo");
        Session session =
            sessionRepository.findOne(
                Session.buildId(recipientId, senderId)
            );

        if (session == null) {
            logger.debug("getSession -> No session found at redis repo. Proceeding to build Session and save it");
            session = buildSession(recipientId, senderId);
            sessionRepository.save(session);
        }

        logger.debug("getSession -> return: " + session);
        return session;
    }

    private Session buildSession(
        String recipientId,
        String senderId
    ) {
        final Page page = new Page();
        page.setId(recipientId);

        User user = userRepository.findByMessengerId(senderId);
        if (user == null) {
            user =
                new User(
                    UUID.randomUUID().toString(),
                    senderId
                );
            userRepository.save(user);
        }

        final String sessionId =
            Session.buildId(
                page.getId(),
                user.getMessengerId()
            );

        Session session = new Session();
        session.setId(sessionId);
        session.setTimeToLiveAsSeconds(300L);
        session.setPage(page);
        session.setUser(user);

        final String url =
            "https://graph.facebook.com/v2.6/" + user.getMessengerId() +
                "?fields=first_name,last_name,profile_pic,locale,timezone,gender&access_token=" +
                messengerConfiguration.getPageAccessToken();

        session.setFbUserProfile(
            restTemplate.getForObject(url, FbUserProfile.class)
        );

        return session;
    }

    public void resetSession(
        Session session
    ) {
        deleteSession(session);
        session.reset();
        persistSession(session);
    }

    public void persistSession(
        Session session
    ) {
        sessionRepository.save(session);
        logger.debug("persistSession("+session+")");
    }

    private void deleteSession(
        Session session
    ) {
        sessionRepository.delete(session);
    }
}
