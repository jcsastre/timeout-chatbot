package com.timeout.chatbot.handler.messenger;

import com.github.messenger4j.receive.events.AttachmentMessageEvent;
import com.github.messenger4j.receive.handlers.AttachmentMessageEventHandler;
import org.springframework.stereotype.Component;

@Component
public class AttachmentMessageEventHandlerImpl implements AttachmentMessageEventHandler {

    private final AttachmentMessageEventAsyncHandler attachmentMessageEventAsyncHandler;

    public AttachmentMessageEventHandlerImpl(
        AttachmentMessageEventAsyncHandler attachmentMessageEventAsyncHandler
    ) {
        this.attachmentMessageEventAsyncHandler = attachmentMessageEventAsyncHandler;
    }

    @Override
    public void handle(
        AttachmentMessageEvent event
    ) {
        attachmentMessageEventAsyncHandler.handle(event);
    }
}
