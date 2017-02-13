package com.timeout.chatbot.handler.messenger;

import com.github.messenger4j.receive.events.TextMessageEvent;
import com.github.messenger4j.receive.handlers.TextMessageEventHandler;
import com.timeout.chatbot.block.ErrorBlock;
import com.timeout.chatbot.domain.page.PageUid;
import com.timeout.chatbot.handler.TextHandler;
import com.timeout.chatbot.handler.text.TextOnSessionStateSubmittingReviewHandler;
import com.timeout.chatbot.session.Session;
import com.timeout.chatbot.session.SessionPool;
import com.timeout.chatbot.session.state.SessionState;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class TextMessageEventHandlerImpl implements TextMessageEventHandler {

    private final SessionPool sessionPool;
    private final ErrorBlock errorBlock;
    private final TextHandler textHandler;
    private final TextOnSessionStateSubmittingReviewHandler textOnSessionStateSubmittingReviewHandler;

    @Autowired
    public TextMessageEventHandlerImpl(
        SessionPool sessionPool,
        ErrorBlock errorBlock,
        TextHandler textHandler,
        TextOnSessionStateSubmittingReviewHandler textOnSessionStateSubmittingReviewHandler
    ) {
        this.sessionPool = sessionPool;
        this.errorBlock = errorBlock;
        this.textHandler = textHandler;
        this.textOnSessionStateSubmittingReviewHandler = textOnSessionStateSubmittingReviewHandler;
    }

    @Override
    public void handle(
        TextMessageEvent event
    ) {
        final Session session = this.sessionPool.getSession(
            new PageUid(event.getRecipient().getId()),
            event.getSender().getId()
        );

        try {
            final SessionState sessionState = session.getSessionState();

            switch (sessionState) {

                case SUBMITTING_REVIEW:
                    textOnSessionStateSubmittingReviewHandler.handle(
                        event.getText(),
                        session
                    );
                    break;

                default:
                    textHandler.handle(
                        event.getText(),
                        session
                    );
            }
        } catch (Exception e) {
            e.printStackTrace();
            errorBlock.send(session.getUser());
        }
    }
}
