package com.timeout.chatbot.handler.states.searching;

import com.github.messenger4j.exceptions.MessengerApiException;
import com.github.messenger4j.exceptions.MessengerIOException;
import com.timeout.chatbot.block.AreasQuickrepliesBlock;
import com.timeout.chatbot.block.BlockError;
import com.timeout.chatbot.block.VenuesRemainingBlock;
import com.timeout.chatbot.domain.Neighborhood;
import com.timeout.chatbot.domain.nlu.NluException;
import com.timeout.chatbot.domain.payload.PayloadType;
import com.timeout.chatbot.handler.intent.IntentFindRestaurantsHandler;
import com.timeout.chatbot.services.GraffittiService;
import com.timeout.chatbot.session.Session;
import com.timeout.chatbot.session.bag.SessionStateSearchingBag;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class SearchingStatePayloadHandler {

    private final GraffittiService graffittiService;
    private final AreasQuickrepliesBlock areasQuickrepliesBlock;
    private final VenuesRemainingBlock venuesRemainingBlock;
    private final IntentFindRestaurantsHandler intentFindRestaurantsHandler;
    private final BlockError blockError;

    @Autowired
    public SearchingStatePayloadHandler(
        GraffittiService graffittiService,
        AreasQuickrepliesBlock areasQuickrepliesBlock,
        VenuesRemainingBlock venuesRemainingBlock,
        IntentFindRestaurantsHandler intentFindRestaurantsHandler,
        BlockError blockError
    ) {
        this.graffittiService = graffittiService;
        this.areasQuickrepliesBlock = areasQuickrepliesBlock;
        this.venuesRemainingBlock = venuesRemainingBlock;
        this.intentFindRestaurantsHandler = intentFindRestaurantsHandler;
        this.blockError = blockError;
    }

    public void handle(
        Session session,
        JSONObject payload
    ) throws NluException, MessengerIOException, MessengerApiException, IOException, InterruptedException {

        final PayloadType payloadType = PayloadType.valueOf(payload.getString("type"));

        switch (payloadType) {

            case venues_show_areas:
                final Integer pageNumber = payload.getInt("pageNumber");
                areasQuickrepliesBlock.send(session, pageNumber);
                break;

            case cancel:
                venuesRemainingBlock.send(session);
                break;

            case venues_set_neighborhood:
                handleVenuesSetNeighborhood(session, payload);
                break;

            case where_everywhere:
                handleWhereEverywhere(session);
                break;

            default:
                blockError.send(session.getUser());
                break;
        }
    }

    private void handleWhereEverywhere(
        Session session
    ) throws InterruptedException, MessengerApiException, MessengerIOException, IOException {

        final SessionStateSearchingBag bag = session.getSessionStateSearchingBag();
        bag.setNeighborhood(null);
        intentFindRestaurantsHandler.handle(session);
    }

    public void handleVenuesSetNeighborhood(
        Session session,
        JSONObject payload
    ) throws InterruptedException, MessengerApiException, MessengerIOException, IOException {

        final String neighborhoodId = payload.getString("neighborhood_id");
        final Neighborhood neighborhood = graffittiService.getNeighborhoodByGraffittiId(neighborhoodId);
        if (neighborhood!=null) {
            final SessionStateSearchingBag bag = session.getSessionStateSearchingBag();
            bag.setNeighborhood(neighborhood);
            intentFindRestaurantsHandler.handle(session);
        } else {
            blockError.send(session.getUser());
        }
    }
}
