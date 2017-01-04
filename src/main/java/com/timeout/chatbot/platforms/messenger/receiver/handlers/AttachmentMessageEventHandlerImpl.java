package com.timeout.chatbot.platforms.messenger.receiver.handlers;

import com.github.messenger4j.receive.events.AttachmentMessageEvent;
import com.github.messenger4j.receive.handlers.AttachmentMessageEventHandler;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class AttachmentMessageEventHandlerImpl implements AttachmentMessageEventHandler {
    @Override
    public void handle(AttachmentMessageEvent event) {
        final List<AttachmentMessageEvent.Attachment> attachments = event.getAttachments();
        for (AttachmentMessageEvent.Attachment attachment : attachments) {
            if (attachment.getType() == AttachmentMessageEvent.AttachmentType.LOCATION) {
                final AttachmentMessageEvent.LocationPayload locationPayload = attachment.getPayload().asLocationPayload();
                locationPayload.getCoordinates().getLatitude();
            }
        }
    }
}
