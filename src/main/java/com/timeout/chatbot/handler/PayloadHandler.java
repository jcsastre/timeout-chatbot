package com.timeout.chatbot.handler;

import com.timeout.chatbot.domain.payload.PayloadType;
import com.timeout.chatbot.handler.intent.IntentService;
import com.timeout.chatbot.messenger4j.send.MessengerSendClientWrapper;
import com.timeout.chatbot.services.BlockService;
import com.timeout.chatbot.session.Session;
import com.timeout.chatbot.session.context.SessionState;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class PayloadHandler {

    private final IntentService intentService;
    private final BlockService blockService;
    private final MessengerSendClientWrapper messengerSendClientWrapper;

    @Autowired
    public PayloadHandler(
        IntentService intentService,
        BlockService blockService,
        MessengerSendClientWrapper messengerSendClientWrapper
    ) {
        this.intentService = intentService;
        this.blockService = blockService;
        this.messengerSendClientWrapper = messengerSendClientWrapper;
    }

    public void handle(
        String payloadAsString,
        Session session
    ) throws Exception {
        final JSONObject payload = new JSONObject(payloadAsString);
        final PayloadType payloadType = PayloadType.valueOf(payload.getString("type"));

        switch (payloadType) {

            case get_started:
                blockService.sendWelcomeFirstTimeBlock(session.getUser());
                session.setSessionState(SessionState.WELCOMED);
                break;

            case help:
                intentService.handleHelp(session);
                break;

            case suggestions:
                intentService.handleSuggestions(session);
                break;

            case discover:
                intentService.handleDiscover(session);
                break;

            case most_loved:
                blockService.sendMostLovedBlock(session.getUser());
                break;

            case start_over:
                session.reset();
                blockService.sendWelcomeBackBlock(session.getUser());
                session.setSessionState(SessionState.WELCOMED);
                break;

            case see_more:
                intentService.handleSeemore(session);
                break;

            case set_geolocation:
                blockService.sendGeolocationAskBlock(session.getUser().getMessengerId());
                break;

            case show_subcategories:
                blockService.sendSubcategoriesQuickrepliesBlock(session, 1);
                break;

            case set_subcategory:
                final String subcategoryId = payload.getString("id");
                intentService.handleSetSubcategory(session, subcategoryId);
                break;

            case venues_more_info:
                blockService.sendVenueSummaryBlock(
                    session.getUser().getMessengerId(),
                    payload.getString("venue_id")
                );
                break;

            case venues_book:
                messengerSendClientWrapper.sendTextMessage(
                    session.getUser().getMessengerId(),
                    "Sorry, booking is not implemented yet"
                );
                blockService.sendVenuesRemainingBlock(session);
                break;

            case no_see_at_timeout:
                messengerSendClientWrapper.sendTextMessage(
                    session.getUser().getMessengerId(),
                    "Ok, back to the list of restaurants"
                );
                intentService.handleFindRestaurants(session);
                break;

//            case utterance:
//                final String utterance = payload.getString("utterance");
//                //TODO:
//                //onUndefinedStateTextHandler.handle(utterance, session);
//                break;

            case films_find_cinemas:
                messengerSendClientWrapper.sendTextMessage(
                    session.getUser().getMessengerId(),
                    "Sorry, 'Find a cinema' is not implemented yet"
                );
                break;

            default:
                messengerSendClientWrapper.sendTextMessage(
                    session.getUser().getMessengerId(),
                    "Sorry, I don't understand you."
                );
                break;
        }
    }
}
