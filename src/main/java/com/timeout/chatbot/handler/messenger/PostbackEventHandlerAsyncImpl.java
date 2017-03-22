package com.timeout.chatbot.handler.messenger;

import com.github.messenger4j.exceptions.MessengerApiException;
import com.github.messenger4j.exceptions.MessengerIOException;
import com.github.messenger4j.receive.events.PostbackEvent;
import com.github.messenger4j.send.MessengerSendClient;
import com.timeout.chatbot.domain.payload.PostbackPayload;
import com.timeout.chatbot.graffitti.domain.GraffittiCategory;
import com.timeout.chatbot.graffitti.domain.GraffittiType;
import com.timeout.chatbot.handler.intent.IntentService;
import com.timeout.chatbot.services.BlockService;
import com.timeout.chatbot.services.SessionService;
import com.timeout.chatbot.session.Session;
import com.timeout.chatbot.session.bag.SessionStateItemBag;
import com.timeout.chatbot.session.state.SessionState;
import org.json.JSONObject;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class PostbackEventHandlerAsyncImpl {

    private final SessionService sessionService;
    private final IntentService intentService;
    private final BlockService blockService;
    private final MessengerSendClient msc;

    public PostbackEventHandlerAsyncImpl(
        SessionService sessionService,
        IntentService intentService,
        BlockService blockService,
        MessengerSendClient msc
    ) {
        this.sessionService = sessionService;
        this.intentService = intentService;
        this.blockService = blockService;
        this.msc = msc;
    }

    @Async
    public void handle(
        PostbackEvent event
    ) {
        final Session session =
            sessionService.getSession(
                event.getRecipient().getId(),
                event.getSender().getId()
            );

        final JSONObject payload = new JSONObject(event.getPayload());

        try {
            handleInternal(
                session,
                payload
            );

            sessionService.persistSession(session);

        } catch (InterruptedException | IOException | MessengerApiException | MessengerIOException e) {
            e.printStackTrace();
            blockService.getError().send(session.user.messengerId);
        }
    }

    private void handleInternal(
        Session session,
        JSONObject payload
    ) throws MessengerApiException, MessengerIOException, IOException, InterruptedException {

        final PostbackPayload postbackPayload = PostbackPayload.valueOf(payload.getString("type"));
        switch (postbackPayload) {

            case get_started:
                handleGetStarted(session);
                break;

            case start_over:
                handleStartOver(session);
                break;

            case discover_restaurants:
                handleDiscoverRestaurants(session);
                break;

            case discover_hotels:
                handleDiscoverHotels(session);
                break;

            case item_see:
                handleItemSee(session, payload);
                break;

            default:
                blockService.getError().send(session.user.messengerId);
        }
    }

    private void handleGetStarted(
        Session session
    ) throws MessengerApiException, MessengerIOException {

        blockService.getWelcomeFirstTimeBlock().send(
            session.user.messengerId,
            session.fbUserProfile
        );

        blockService.getDiscoverBlock().send(
            session.user.messengerId
        );

        session.state = SessionState.DISCOVER;
    }

    private void handleStartOver(
        Session session
    ) throws MessengerApiException, MessengerIOException {

        intentService.getIntentStartOverHandler().handle(session);
    }

    private void handleDiscoverRestaurants(
        Session session
    ) throws InterruptedException, MessengerApiException, MessengerIOException, IOException {

        intentService.getIntentFindVenuesHandler().handle(
            session,
            GraffittiCategory.RESTAURANTS,
            null,
            null,
            null
        );
    }

    private void handleDiscoverHotels(
        Session session
    ) throws InterruptedException, MessengerApiException, MessengerIOException, IOException {

        intentService.getIntentFindVenuesHandler().handle(
            session,
            GraffittiCategory.HOTELS,
            null,
            null,
            null
        );
    }

    private void handleItemSee(
        Session session,
        JSONObject payload
    ) throws InterruptedException, MessengerApiException, MessengerIOException, IOException {

        final GraffittiType graffittiType = GraffittiType.fromValue(payload.getString("item_type"));
        switch (graffittiType) {

            case VENUE:
                session.bagItem = new SessionStateItemBag();
                session.bagItem.graffittiType = graffittiType;
                session.bagItem.itemId = payload.getString("item_id");
                session.state = SessionState.ITEM;
                intentService.getIntentSeeItem().handle(session);
                break;
        }
    }
}
