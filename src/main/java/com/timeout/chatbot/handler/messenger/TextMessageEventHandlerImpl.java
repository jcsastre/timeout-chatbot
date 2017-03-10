package com.timeout.chatbot.handler.messenger;

import com.github.messenger4j.receive.events.TextMessageEvent;
import com.github.messenger4j.receive.handlers.TextMessageEventHandler;
import com.timeout.chatbot.block.BlockError;
import com.timeout.chatbot.handler.states.DefaultTextHandler;
import com.timeout.chatbot.session.SessionPool;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class TextMessageEventHandlerImpl implements TextMessageEventHandler {

    private final SessionPool sessionPool;
    private final BlockError blockError;
    private final DefaultTextHandler defaultTextHandler;

    @Autowired
    public TextMessageEventHandlerImpl(
        SessionPool sessionPool,
        BlockError blockError,
        DefaultTextHandler defaultTextHandler
    ) {
        this.sessionPool = sessionPool;
        this.blockError = blockError;
        this.defaultTextHandler = defaultTextHandler;
    }

    @Override
    public void handle(
        TextMessageEvent event
    ) {
        handleAsync(event);
    }

//    @Async
    private void handleAsync(
        TextMessageEvent event
    ) {
//        final Session session = this.sessionPool.getSession(
//            new PageUid(event.getRecipient().getId()),
//            event.getSender().getId()
//        );
//
//        try {
//            defaultTextHandler.handle(
//                event.getText(),
//                session
//            );
//        } catch (Exception e) {
//            e.printStackTrace();
//            blockError.send(session.user);
//        }
    }
}
