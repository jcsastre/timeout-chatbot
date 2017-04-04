package com.timeout.chatbot.handler.messenger;

import com.github.messenger4j.receive.events.PostbackEvent;
import com.github.messenger4j.receive.handlers.PostbackEventHandler;
import org.springframework.stereotype.Component;

@Component
public class PostbackEventHandlerImpl implements PostbackEventHandler {

    private final PostbackEventHandlerAsyncImpl postbackEventHandlerAsync;

    public PostbackEventHandlerImpl(
        PostbackEventHandlerAsyncImpl postbackEventHandlerAsync
    ) {
        this.postbackEventHandlerAsync = postbackEventHandlerAsync;
    }

    @Override
    public void handle(
        PostbackEvent event
    ) {
        postbackEventHandlerAsync.handle(event);
    }
}
