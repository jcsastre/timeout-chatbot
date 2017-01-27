package com.timeout.chatbot.handler.messenger;

import com.github.messenger4j.receive.events.AttachmentMessageEvent;
import com.github.messenger4j.receive.handlers.AttachmentMessageEventHandler;
import com.timeout.chatbot.domain.Geolocation;
import com.timeout.chatbot.domain.What;
import com.timeout.chatbot.domain.page.PageUid;
import com.timeout.chatbot.handler.intent.IntentFindRestaurantsHandler;
import com.timeout.chatbot.session.Session;
import com.timeout.chatbot.session.SessionPool;
import com.timeout.chatbot.session.SessionStateLookingBag;
import com.timeout.chatbot.session.context.SessionState;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class AttachmentMessageEventHandlerImpl implements AttachmentMessageEventHandler {

    private final SessionPool sessionPool;
    private final IntentFindRestaurantsHandler findRestaurantsHandler;

    @Autowired
    public AttachmentMessageEventHandlerImpl(
        SessionPool sessionPool,
        IntentFindRestaurantsHandler findRestaurantsHandler
    ) {
        this.sessionPool = sessionPool;
        this.findRestaurantsHandler = findRestaurantsHandler;
    }

    @Override
    public void handle(AttachmentMessageEvent event) {

        final String recipitientId = event.getRecipient().getId();

        final Session session =
            sessionPool.getSession(
                new PageUid(recipitientId),
                event.getSender().getId()
            );

        for (AttachmentMessageEvent.Attachment attachment : event.getAttachments()) {
            if (attachment.getType() == AttachmentMessageEvent.AttachmentType.LOCATION) {
                final AttachmentMessageEvent.LocationPayload locationPayload =
                    attachment.getPayload().asLocationPayload();

                final Geolocation geolocation =
                    new Geolocation(
                        locationPayload.getCoordinates().getLatitude(),
                        locationPayload.getCoordinates().getLongitude()
                    );

                final SessionStateLookingBag lookingBag = session.getSessionStateLookingBag();
                lookingBag.setGeolocation(geolocation);

                if (session.getSessionState() == SessionState.LOOKING) {
                    final What what = lookingBag.getWhat();
                    if (what == What.RESTAURANT) {
                        findRestaurantsHandler.handleOtherThanBooking(session);
                    }
                }
            }
        }
    }
}
