package com.timeout.chatbot.handler.messenger;

import com.github.messenger4j.receive.events.TextMessageEvent;
import com.github.messenger4j.receive.handlers.TextMessageEventHandler;
import org.springframework.stereotype.Component;

@Component
public class TextMessageEventHandlerImpl implements TextMessageEventHandler {

    private final TextMessageEventHandlerAsyncImpl textMessageEventHandlerAsyncImpl;

    public TextMessageEventHandlerImpl(TextMessageEventHandlerAsyncImpl textMessageEventHandlerAsyncImpl) {
        this.textMessageEventHandlerAsyncImpl = textMessageEventHandlerAsyncImpl;
    }

    @Override
    public void handle(
        TextMessageEvent event
    ) {
        textMessageEventHandlerAsyncImpl.handle(event);
    }
}
