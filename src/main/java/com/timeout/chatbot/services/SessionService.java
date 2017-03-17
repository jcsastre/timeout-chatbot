package com.timeout.chatbot.services;

import com.timeout.chatbot.configuration.MessengerConfiguration;
import com.timeout.chatbot.domain.FbUserProfile;
import com.timeout.chatbot.domain.page.Page;
import com.timeout.chatbot.domain.user.User;
import com.timeout.chatbot.repository.SessionRepository;
import com.timeout.chatbot.repository.UserRepository;
import com.timeout.chatbot.session.Session;
import com.timeout.chatbot.session.state.SessionState;
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
        page.id = recipientId;

        User user = userRepository.findByMessengerId(senderId);
        if (user == null) {
            user = new User();
            user.id = UUID.randomUUID().toString();
            user.messengerId = senderId;
            userRepository.save(user);
        }

        final String sessionId =
            Session.buildId(
                page.id,
                user.messengerId
            );

        Session session = new Session();
        session.id = sessionId;
        session.timeToLiveAsSeconds = 300L;
        session.page = page;
        session.user = user;

//        final SessionStateSearchingBag bagSearching = new SessionStateSearchingBag();
//        bagSearching.category = Category.RESTAURANTS;
//        bagSearching.subcategory = null;
//        session.bagSearching = bagSearching;

        final String url =
            "https://graph.facebook.com/v2.6/" + user.messengerId +
                "?fields=first_name,last_name,profile_pic,locale,timezone,gender&access_token=" +
                messengerConfiguration.getPageAccessToken();

        session.fbUserProfile =
            restTemplate.getForObject(url, FbUserProfile.class);

        return session;
    }

    public void resetSession(
        Session session
    ) {
        session.state = SessionState.UNDEFINED;
        session.bagSearching = null;
        session.bagItem = null;
        session.bagBooking = null;
        session.bagSubmitting = null;
    }

    public void persistSession(
        Session session
    ) {
        sessionRepository.save(session);
        logger.debug("persistSession -> " + session);
    }

    private void deleteSession(
        Session session
    ) {
        sessionRepository.delete(session);
    }
}
