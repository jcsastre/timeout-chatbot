package com.timeout.chatbot.handler.textmessage;

import com.github.messenger4j.receive.events.TextMessageEvent;
import com.github.messenger4j.receive.handlers.TextMessageEventHandler;
import com.timeout.chatbot.domain.page.PageUid;
import com.timeout.chatbot.session.Session;
import com.timeout.chatbot.session.SessionPool;
import com.timeout.chatbot.session.context.SessionState;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class TextMessageEventHandlerImpl implements TextMessageEventHandler {

    private final SessionPool sessionPool;

    private final TextMessageEventOnUndefinedStateHandler textMessageEventOnUndefinedStateHandler;

    @Autowired
    public TextMessageEventHandlerImpl(
        SessionPool sessionPool,
        TextMessageEventOnUndefinedStateHandler textMessageEventOnUndefinedStateHandler
    ) {
        this.sessionPool = sessionPool;
        this.textMessageEventOnUndefinedStateHandler = textMessageEventOnUndefinedStateHandler;
    }

    @Override
    public void handle(
        TextMessageEvent textMessageEvent
    ) {
        final String text = textMessageEvent.getText();
        final String recipitientId = textMessageEvent.getRecipient().getId();

        final Session session = sessionPool.getSession(
            new PageUid(recipitientId),
            textMessageEvent.getSender().getId()
        );

        final SessionState sessionState = session.getSessionState();

        switch (sessionState) {

            case UNDEFINED:
                textMessageEventOnUndefinedStateHandler.handle(text, session);
                break;

            case LOOKING:
                handleTextOnStateLooking(text);
                break;

            case BOOKING:
                //TODO;
                break;

            default:
                sendTextMessage("Sorry, an error occurred");
                break;
        }

        session.handleTextMessageEvent(textMessageEvent);
    }
}
