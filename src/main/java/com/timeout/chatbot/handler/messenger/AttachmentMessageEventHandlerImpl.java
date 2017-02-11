package com.timeout.chatbot.handler.messenger;

import com.github.messenger4j.exceptions.MessengerApiException;
import com.github.messenger4j.exceptions.MessengerIOException;
import com.github.messenger4j.receive.events.AttachmentMessageEvent;
import com.github.messenger4j.receive.handlers.AttachmentMessageEventHandler;
import com.github.messenger4j.send.MessengerSendClient;
import com.timeout.chatbot.block.ErrorBlock;
import com.timeout.chatbot.block.quickreply.QuickReplyBuilderForCurrentSessionState;
import com.timeout.chatbot.domain.Geolocation;
import com.timeout.chatbot.domain.What;
import com.timeout.chatbot.domain.page.PageUid;
import com.timeout.chatbot.handler.intent.IntentFindRestaurantsHandler;
import com.timeout.chatbot.handler.intent.IntentSeeItem;
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
    private final ErrorBlock errorBlock;
    private final MessengerSendClient messengerSendClient;
    private final QuickReplyBuilderForCurrentSessionState quickReplyBuilderForCurrentSessionState;
    private final IntentSeeItem intentSeeItem;

    @Autowired
    public AttachmentMessageEventHandlerImpl(
        SessionPool sessionPool,
        IntentFindRestaurantsHandler findRestaurantsHandler,
        ErrorBlock errorBlock,
        MessengerSendClient messengerSendClient,
        QuickReplyBuilderForCurrentSessionState quickReplyBuilderForCurrentSessionState,
        IntentSeeItem intentSeeItem) {
        this.sessionPool = sessionPool;
        this.findRestaurantsHandler = findRestaurantsHandler;
        this.errorBlock = errorBlock;
        this.messengerSendClient = messengerSendClient;
        this.quickReplyBuilderForCurrentSessionState = quickReplyBuilderForCurrentSessionState;
        this.intentSeeItem = intentSeeItem;
    }

    @Override
    public void handle(AttachmentMessageEvent event) {

        final String recipitientId = event.getRecipient().getId();

        final Session session =
            sessionPool.getSession(
                new PageUid(recipitientId),
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
            errorBlock.send(session.getUser());
        }
    }

    private void handleLocation(
        Session session,
        AttachmentMessageEvent.Attachment attachment
    ) throws MessengerApiException, MessengerIOException {

        final AttachmentMessageEvent.LocationPayload locationPayload =
            attachment.getPayload().asLocationPayload();

        final Geolocation geolocation =
            new Geolocation(
                locationPayload.getCoordinates().getLatitude(),
                locationPayload.getCoordinates().getLongitude()
            );

        final SessionStateLookingBag lookingBag = session.getSessionStateLookingBag();
        lookingBag.setGraffittiPageNumber(1);
        lookingBag.setNeighborhood(null);
        lookingBag.setGeolocation(geolocation);

        if (session.getSessionState() == SessionState.SEARCHING) {
            final What what = lookingBag.getWhat();
            if (what == What.RESTAURANT) {
                findRestaurantsHandler.fetchAndSend(session);
            }
        }
    }

    private void handleImage(
        Session session,
        AttachmentMessageEvent.Attachment attachment
    ) throws MessengerApiException, MessengerIOException {

        final AttachmentMessageEvent.BinaryPayload binaryPayload =
            attachment.getPayload().asBinaryPayload();

        if (session.getSessionState() == SessionState.ITEM) {
            messengerSendClient.sendTextMessage(
                session.getUser().getMessengerId(),
                "Thaks for submitting the image!",
                quickReplyBuilderForCurrentSessionState.build(session)
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
