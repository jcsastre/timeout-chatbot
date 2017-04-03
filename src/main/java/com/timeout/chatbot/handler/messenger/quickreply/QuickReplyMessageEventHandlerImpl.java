package com.timeout.chatbot.handler.messenger.quickreply;

import com.github.messenger4j.receive.events.QuickReplyMessageEvent;
import com.github.messenger4j.receive.handlers.QuickReplyMessageEventHandler;
import org.springframework.stereotype.Component;

@Component
public class QuickReplyMessageEventHandlerImpl implements QuickReplyMessageEventHandler {

    private final QuickReplyMessageEventHandlerAsyncImpl quickReplyMessageEventHandlerAsync;

    public QuickReplyMessageEventHandlerImpl(
        QuickReplyMessageEventHandlerAsyncImpl quickReplyMessageEventHandlerAsync
    ) {
        this.quickReplyMessageEventHandlerAsync = quickReplyMessageEventHandlerAsync;
    }

    @Override
    public void handle(
        QuickReplyMessageEvent event
    ) {
        quickReplyMessageEventHandlerAsync.handle(event);
    }
}
