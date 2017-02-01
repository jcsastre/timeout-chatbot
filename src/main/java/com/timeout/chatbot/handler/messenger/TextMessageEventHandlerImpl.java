package com.timeout.chatbot.handler.messenger;

import com.github.messenger4j.receive.events.TextMessageEvent;
import com.github.messenger4j.receive.handlers.TextMessageEventHandler;
import com.timeout.chatbot.block.ErrorBlock;
import com.timeout.chatbot.domain.page.PageUid;
import com.timeout.chatbot.handler.TextHandler;
import com.timeout.chatbot.session.Session;
import com.timeout.chatbot.session.SessionPool;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class TextMessageEventHandlerImpl implements TextMessageEventHandler {

    private final SessionPool sessionPool;
    private final ErrorBlock errorBlock;
    private final TextHandler textHandler;

    @Autowired
    public TextMessageEventHandlerImpl(
        SessionPool sessionPool,
        ErrorBlock errorBlock,
        TextHandler textHandler
    ) {
        this.sessionPool = sessionPool;
        this.errorBlock = errorBlock;
        this.textHandler = textHandler;
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
            textHandler.handle(
                event.getText(),
                session
            );
        } catch (Exception e) {
            e.printStackTrace();
            errorBlock.send(session.getUser());
        }
    }
}
