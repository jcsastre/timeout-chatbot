package com.timeout.chatbot.handler.messenger;

import com.github.messenger4j.exceptions.MessengerApiException;
import com.github.messenger4j.exceptions.MessengerIOException;
import com.github.messenger4j.receive.events.QuickReplyMessageEvent;
import com.timeout.chatbot.action.FindVenuesAction;
import com.timeout.chatbot.domain.Neighborhood;
import com.timeout.chatbot.domain.payload.QuickreplyPayload;
import com.timeout.chatbot.services.BlockService;
import com.timeout.chatbot.services.GraffittiService;
import com.timeout.chatbot.services.SessionService;
import com.timeout.chatbot.session.Session;
import com.timeout.chatbot.session.state.SessionState;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class QuickReplyMessageEventHandlerAsyncImpl {

    private final SessionService sessionService;
    private final BlockService blockService;
    private final FindVenuesAction findVenuesAction;
    private final GraffittiService graffittiService;

    @Autowired
    public QuickReplyMessageEventHandlerAsyncImpl(
        SessionService sessionService,
        BlockService blockService,
        FindVenuesAction findVenuesAction,
        GraffittiService graffittiService
    ) {
        this.sessionService = sessionService;
        this.blockService = blockService;
        this.findVenuesAction = findVenuesAction;
        this.graffittiService = graffittiService;
    }

    @Async
    public void handle(
        QuickReplyMessageEvent event
    ) {
        final Session session =
            sessionService.getSession(
                event.getRecipient().getId(),
                event.getSender().getId()
            );

        final JSONObject payload = new JSONObject(event.getQuickReply().getPayload());

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
    ) throws InterruptedException, MessengerApiException, MessengerIOException, IOException {

        final QuickreplyPayload payloadType = QuickreplyPayload.valueOf(payload.getString("type"));
        switch (payloadType) {

            case searching_see_more:
                handleSearchingSeeMore(session, payload);
                break;

            case searching_show_areas:
                handleSearchingShowAreas(session, payload);
                break;

            case searching_show_subcategories:
                handleSearchingShowSubcategories(session, payload);
                break;

            case searching_set_area_neighborhood:
                handleSearchingSetAreaNeighborhood(session, payload);
                break;

            case searching_set_area_any:
                handleSearchingSetAreaAny(session, payload);
                break;

            case searching_set_subcategory:
                handleSearchingSetSubcategory(session, payload);
                break;

            case searching_set_subcategory_any:
                handleSearchingSetSubcategoryAny(session, payload);
                break;
        }
    }

    private void handleSearchingSeeMore(
        Session session,
        JSONObject payload
    ) throws InterruptedException, MessengerApiException, MessengerIOException, IOException {

        if (session.state == SessionState.SEARCHING) {
            session.bagSearching.pageNumber = session.bagSearching.pageNumber + 1;
            findVenuesAction.find(session);
        } else {
            //TODO: ha pasado mucho tiempo, y los resultados pueden ser distintos, que hacer?
        }
    }

    private void handleSearchingShowAreas(
        Session session,
        JSONObject payload
    ) throws MessengerApiException, MessengerIOException {

        if (session.state == SessionState.SEARCHING) {
            blockService.getAreas().send(
                session,
                payload.getInt("pageNumber")
            );
        } else {
            //TODO: ha pasado mucho tiempo, y los resultados pueden ser distintos, que hacer?
        }
    }

    private void handleSearchingShowSubcategories(
        Session session,
        JSONObject payload
    ) throws MessengerApiException, MessengerIOException {

        if (session.state == SessionState.SEARCHING) {
            blockService.getSubcategoriesQuickreplies().send(
                session,
                payload.getInt("pageNumber")
            );
        } else {
            //TODO: ha pasado mucho tiempo, y los resultados pueden ser distintos, que hacer?
        }
    }

    private void handleSearchingSetAreaNeighborhood(
        Session session,
        JSONObject payload
    ) throws InterruptedException, MessengerApiException, MessengerIOException, IOException {

        if (session.state == SessionState.SEARCHING) {
            final String neighborhoodId = payload.getString("neighborhood_id");
            final Neighborhood neighborhood = graffittiService.getNeighborhoodByGraffittiId(neighborhoodId);
            if (neighborhood!=null) {
                session.bagSearching.neighborhood = neighborhood;
                session.bagSearching.geolocation = null;
                session.bagSearching.pageNumber = 1;
                findVenuesAction.find(session);
            } else {
                blockService.getError().send(session.user.messengerId);
            }
        } else {
            //TODO: ha pasado mucho tiempo, y los resultados pueden ser distintos, que hacer?
        }
    }

    private void handleSearchingSetAreaAny(
        Session session,
        JSONObject payload
    ) throws InterruptedException, MessengerApiException, MessengerIOException, IOException {

        if (session.state == SessionState.SEARCHING) {
            session.bagSearching.neighborhood = null;
            session.bagSearching.geolocation = null;
            session.bagSearching.pageNumber = 1;
            findVenuesAction.find(session);
        } else {
            //TODO: ha pasado mucho tiempo, y los resultados pueden ser distintos, que hacer?
        }
    }

    private void handleSearchingSetSubcategory(
        Session session,
        JSONObject payload
    ) throws InterruptedException, MessengerApiException, MessengerIOException, IOException {

        if (session.state == SessionState.SEARCHING) {
            final String subcategoryId = payload.getString("subcategory_id");
            session.bagSearching.graffittiSubcategory = session.bagSearching.graffittiCategory.findSubcategoryByGraffittiId(subcategoryId);
            session.bagSearching.pageNumber = 1;
            findVenuesAction.find(session);
        } else {
            //TODO: ha pasado mucho tiempo, y los resultados pueden ser distintos, que hacer?
        }
    }

    private void handleSearchingSetSubcategoryAny(
        Session session,
        JSONObject payload
    ) throws InterruptedException, MessengerApiException, MessengerIOException, IOException {

        if (session.state == SessionState.SEARCHING) {
            session.bagSearching.graffittiSubcategory = null;
            session.bagSearching.pageNumber = 1;
            findVenuesAction.find(session);
        } else {
            //TODO: ha pasado mucho tiempo, y los resultados pueden ser distintos, que hacer?
        }
    }
}

//        this.sessionPool.getSession(
//            new PageUid(recipientId),
//            senderId
//        ).addCallback(
//            (session -> {
//                final Date currentTimestamp = session.getCurrentTimestamp();
//                session.setCurrentTimestamp(currentTimestamp);
//                try {
//                    defaultPayloadHandler.handle(payload, session);
//                } catch (Exception e) {
//                    e.printStackTrace();
//                    blockError.send(session.user);
//                }
//
//            }),
//            Throwable::printStackTrace
//        );

//        listenableFuture.addCallback(
//            new ListenableFutureCallback<Session>() {
//                @Override
//                public void onSuccess(Session session) {
//                }
//                @Override
//                public void onFailure(Throwable ex) {
//                }
//            }
//        );
