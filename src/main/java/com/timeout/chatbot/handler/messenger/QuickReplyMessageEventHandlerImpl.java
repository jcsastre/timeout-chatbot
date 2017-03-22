package com.timeout.chatbot.handler.messenger;

import com.github.messenger4j.receive.events.QuickReplyMessageEvent;
import com.github.messenger4j.receive.handlers.QuickReplyMessageEventHandler;
import org.springframework.stereotype.Component;

@Component
public class QuickReplyMessageEventHandlerImpl implements QuickReplyMessageEventHandler {

    private final QuickReplyMessageEventHandlerAsyncImpl quickReplyMessageEventHandlerAsyncImpl;

    public QuickReplyMessageEventHandlerImpl(QuickReplyMessageEventHandlerAsyncImpl quickReplyMessageEventHandlerAsyncImpl) {
        this.quickReplyMessageEventHandlerAsyncImpl = quickReplyMessageEventHandlerAsyncImpl;
    }

    @Override
    public void handle(
        QuickReplyMessageEvent event
    ) {
        quickReplyMessageEventHandlerAsyncImpl.handle(event);
    }
}
