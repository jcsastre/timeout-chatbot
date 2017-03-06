package com.timeout.chatbot.handler.messenger;

import com.github.messenger4j.exceptions.MessengerApiException;
import com.github.messenger4j.exceptions.MessengerIOException;
import com.github.messenger4j.receive.events.AttachmentMessageEvent;
import com.github.messenger4j.send.MessengerSendClient;
import com.timeout.chatbot.block.BlockError;
import com.timeout.chatbot.domain.Geolocation;
import com.timeout.chatbot.domain.entities.Category;
import com.timeout.chatbot.domain.page.PageUid;
import com.timeout.chatbot.handler.intent.IntentFindVenuesHandler;
import com.timeout.chatbot.handler.intent.IntentSeeItem;
import com.timeout.chatbot.session.Session;
import com.timeout.chatbot.session.SessionPool;
import com.timeout.chatbot.session.bag.SessionStateSearchingBag;
import com.timeout.chatbot.session.state.SessionState;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Date;

@Component
public class AttachmentMessageEventAsyncHandler {

    private final SessionPool sessionPool;
    private final IntentFindVenuesHandler findRestaurantsHandler;
    private final BlockError blockError;
    private final MessengerSendClient messengerSendClient;
    private final IntentSeeItem intentSeeItem;

    @Autowired
    public AttachmentMessageEventAsyncHandler(
        SessionPool sessionPool,
        IntentFindVenuesHandler findRestaurantsHandler,
        BlockError blockError,
        MessengerSendClient messengerSendClient,
        IntentSeeItem intentSeeItem
    ) {
        this.sessionPool = sessionPool;
        this.findRestaurantsHandler = findRestaurantsHandler;
        this.blockError = blockError;
        this.messengerSendClient = messengerSendClient;
        this.intentSeeItem = intentSeeItem;
    }

    @Async
    public void handle(
        AttachmentMessageEvent event
    ) {
        final Session session =
            sessionPool.getSession(
                new PageUid(event.getRecipient().getId()),
                event.getSender().getId()
            );

        boolean proceed = true;
        final Date currentTimestamp = session.getCurrentTimestamp();
        if (currentTimestamp != null) {
            if (currentTimestamp.equals(event.getTimestamp())) {
                proceed = false;
            }
        }

        if (proceed) {
            session.setCurrentTimestamp(event.getTimestamp());

            try {
                for (AttachmentMessageEvent.Attachment attachment : event.getAttachments()) {
                    if (attachment.getType() == AttachmentMessageEvent.AttachmentType.LOCATION) {
                        handleLocation(session, attachment);
                    } else if (attachment.getType() == AttachmentMessageEvent.AttachmentType.IMAGE) {
                        handleImage(session, attachment);
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
                blockError.send(session.getUser());
            }
        }

    }

    private void handleLocation(
        Session session,
        AttachmentMessageEvent.Attachment attachment
    ) throws MessengerApiException, MessengerIOException, IOException, InterruptedException {

        final AttachmentMessageEvent.LocationPayload locationPayload =
            attachment.getPayload().asLocationPayload();

        final Geolocation geolocation =
            new Geolocation(
                locationPayload.getCoordinates().getLatitude(),
                locationPayload.getCoordinates().getLongitude()
            );

        final SessionStateSearchingBag lookingBag = session.getSessionStateSearchingBag();
        lookingBag.setGeolocation(geolocation);

        if (session.getSessionState() == SessionState.SEARCHING) {
            final Category category = lookingBag.getCategory();
            if (
                category == Category.RESTAURANTS ||
                category == Category.HOTELS
            ) {
                findRestaurantsHandler.fetchAndSend(session);
            }
        }
    }

    private void handleImage(
        Session session,
        AttachmentMessageEvent.Attachment attachment
    ) throws MessengerApiException, MessengerIOException, IOException, InterruptedException {

        final AttachmentMessageEvent.BinaryPayload binaryPayload =
            attachment.getPayload().asBinaryPayload();

        if (session.getSessionState() == SessionState.ITEM) {
            messengerSendClient.sendTextMessage(
                session.getUser().getMessengerId(),
                "Thaks for submitting the image!"
            );

//            final SessionStateItemBag bag = session.getSessionStateItemBag();
//            bag.setGraffittiType(GraffittiType.fromString(payload.getString("item_type")));
//            bag.setItemId(payload.getString("item_id"));
//            session.setSessionState(SessionState.ITEM);
//            intentService.handleSeeItem(session);
            intentSeeItem.handle(session);
        }
    }
}
