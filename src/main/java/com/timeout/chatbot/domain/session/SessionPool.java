package com.timeout.chatbot.domain.session;

import com.github.messenger4j.send.MessengerSendClient;
import com.timeout.chatbot.domain.messenger.Page;
import com.timeout.chatbot.domain.messenger.Recipient;
import com.timeout.chatbot.services.ApiAiService;
import com.timeout.chatbot.services.GraffittiService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class SessionPool {
    @Autowired
    private GraffittiService campingpongAPIService;

    @Autowired
    private ApiAiService apiAiService;

    @Autowired
    private MessengerSendClient messengerSendClient;

    private final List<Session> sessions = new ArrayList<>();

    public Session getSession(String pageUid, String recipientUid) {
        synchronized (sessions) {
            for (Session session : sessions) {
                if (
                    session.getPage().getUid().equals(pageUid) &&
                        session.getRecipient().getUid().equals(recipientUid)
                    ) {
                    return session;
                }
            }
        }

        final Session session = new Session(
            campingpongAPIService,
            apiAiService,
            messengerSendClient,
            new Page(pageUid),
            new Recipient(recipientUid)
        );

        sessions.add(session);

        return session;
    }
}
