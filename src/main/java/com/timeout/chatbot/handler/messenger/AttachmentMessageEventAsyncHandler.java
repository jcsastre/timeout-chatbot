package com.timeout.chatbot.handler.messenger;

import com.github.messenger4j.exceptions.MessengerApiException;
import com.github.messenger4j.exceptions.MessengerIOException;
import com.github.messenger4j.receive.events.AttachmentMessageEvent;
import com.github.messenger4j.send.MessengerSendClient;
import com.timeout.chatbot.block.BlockError;
import com.timeout.chatbot.domain.Geolocation;
import com.timeout.chatbot.graffitti.domain.GraffittiCategory;
import com.timeout.chatbot.handler.intent.IntentFindVenuesHandler;
import com.timeout.chatbot.action.SeeItemAction;
import com.timeout.chatbot.services.SessionService;
import com.timeout.chatbot.session.Session;
import com.timeout.chatbot.session.bag.SessionStateSearchingBag;
import com.timeout.chatbot.session.state.SessionState;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class AttachmentMessageEventAsyncHandler {

    private final SessionService sessionService;
    private final IntentFindVenuesHandler findRestaurantsHandler;
    private final BlockError blockError;
    private final MessengerSendClient messengerSendClient;
    private final SeeItemAction seeItemAction;

    @Autowired
    public AttachmentMessageEventAsyncHandler(
        SessionService sessionService,
        IntentFindVenuesHandler findRestaurantsHandler,
        BlockError blockError,
        MessengerSendClient messengerSendClient,
        SeeItemAction seeItemAction
    ) {
        this.sessionService = sessionService;
        this.findRestaurantsHandler = findRestaurantsHandler;
        this.blockError = blockError;
        this.messengerSendClient = messengerSendClient;
        this.seeItemAction = seeItemAction;
    }

    @Async
    public void handle(
        AttachmentMessageEvent event
    ) {
        final Session session =
            sessionService.getSession(
                event.getRecipient().getId(),
                event.getSender().getId()
            );

        handleInternal(event);

        sessionService.persistSession(session);
    }

    private void handleInternal(
        AttachmentMessageEvent event
    ) {
        Session session =
            sessionService.getSession(
                event.getRecipient().getId(),
                event.getSender().getId()
            );

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
            blockError.send(session.user.messengerId);
        }
    }

    private void handleLocation(
        Session session,
        AttachmentMessageEvent.Attachment attachment
    ) throws MessengerApiException, MessengerIOException, IOException, InterruptedException {

        final AttachmentMessageEvent.LocationPayload locationPayload =
            attachment.getPayload().asLocationPayload();

        final Geolocation geolocation = new Geolocation();
        geolocation.latitude = locationPayload.getCoordinates().getLatitude();
        geolocation.longitude = locationPayload.getCoordinates().getLongitude();

        final SessionStateSearchingBag bag = session.bagSearching;

        bag.geolocation = geolocation;

        if (session.state == SessionState.SEARCHING) {
            if (
                bag.graffittiCategory == GraffittiCategory.RESTAURANTS ||
                bag.graffittiCategory == GraffittiCategory.HOTELS
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

        if (session.state == SessionState.ITEM) {
            messengerSendClient.sendTextMessage(
                session.user.messengerId,
                "Thaks for submitting the image!"
            );

//            final SessionStateItemBag bag = session.getSessionStateItemBag();
//            bag.setGraffittiType(GraffittiType.fromString(payload.getString("item_type")));
//            bag.setItemId(payload.getString("item_id"));
//            session.state = SessionState.ITEM;
//            intentService.handleSeeItem(session);
            seeItemAction.perform(session);
        }
    }
}
