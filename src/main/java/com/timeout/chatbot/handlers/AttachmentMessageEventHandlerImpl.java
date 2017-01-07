package com.timeout.chatbot.handlers;

import com.github.messenger4j.receive.events.AttachmentMessageEvent;
import com.github.messenger4j.receive.handlers.AttachmentMessageEventHandler;
import com.timeout.chatbot.domain.session.Session;
import com.timeout.chatbot.domain.session.SessionPool;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class AttachmentMessageEventHandlerImpl implements AttachmentMessageEventHandler {

    private final SessionPool sessionPool;

    @Autowired
    public AttachmentMessageEventHandlerImpl(SessionPool sessionPool) {
        this.sessionPool = sessionPool;
    }

    @Override
    public void handle(AttachmentMessageEvent event) {
        final Session session =
            sessionPool.getSession(
                event.getRecipient().getId(),
                event.getSender().getId()
            );

        final List<AttachmentMessageEvent.Attachment> attachments = event.getAttachments();
        for (AttachmentMessageEvent.Attachment attachment : attachments) {
            if (attachment.getType() == AttachmentMessageEvent.AttachmentType.LOCATION) {
                final AttachmentMessageEvent.LocationPayload locationPayload =
                    attachment.getPayload().asLocationPayload();

                session.applyLocation(
                    locationPayload.getCoordinates().getLatitude(),
                    locationPayload.getCoordinates().getLongitude()
                );
            }
        }
    }
}
