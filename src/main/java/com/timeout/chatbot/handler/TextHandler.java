package com.timeout.chatbot.handler;

import com.github.messenger4j.exceptions.MessengerApiException;
import com.github.messenger4j.exceptions.MessengerIOException;
import com.github.messenger4j.send.MessengerSendClient;
import com.timeout.chatbot.domain.What;
import com.timeout.chatbot.domain.nlu.NluException;
import com.timeout.chatbot.domain.nlu.NluResult;
import com.timeout.chatbot.domain.nlu.intent.NluIntentType;
import com.timeout.chatbot.graffitti.domain.GraffittiType;
import com.timeout.chatbot.handler.intent.IntentService;
import com.timeout.chatbot.services.GraffittiService;
import com.timeout.chatbot.services.NluService;
import com.timeout.chatbot.session.Session;
import com.timeout.chatbot.session.context.SessionState;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class TextHandler {

    private final IntentService intentService;
    private final NluService nluService;
    private final MessengerSendClient messengerSendClient;
    private final GraffittiService graffittiService;

    @Autowired
    public TextHandler(
        IntentService intentService,
        NluService nluService,
        MessengerSendClient messengerSendClient,
        GraffittiService graffittiService
    ) {
        this.intentService = intentService;
        this.nluService = nluService;
        this.messengerSendClient = messengerSendClient;
        this.graffittiService = graffittiService;
    }

    public void handle(
        String text,
        Session session
    ) throws NluException, MessengerApiException, MessengerIOException {

        NluResult nluResult = handleInternal(text);

        if (nluResult == null) {
            nluResult = nluService.processText(text);
        }

        if (nluResult != null) {
            processNluResult(
                session,
                nluResult
            );
        } else {
            messengerSendClient.sendTextMessage(
                session.getUser().getMessengerId(),
                "Sorry, I don't understand"
            );
        }
    }

    private NluResult handleInternal(
        String text
    ) {
        if (text.equalsIgnoreCase("restaurants")) {
            return
                new NluResult(
                    NluIntentType.FIND_RESTAURANTS
                );
        }

        return null;
    }

    private void processNluResult(
        Session session,
        NluResult nluResult
    ) throws MessengerApiException, MessengerIOException {
        switch (nluResult.getNluIntentType()) {

            case GREETINGS:
                intentService.handleGreetings(session);
                break;

            case SUGGESTIONS:
                intentService.handleSuggestions(session);
                break;

            case DISCOVER:
                intentService.handleDiscover(session);
                break;

            case FIND_THINGSTODO:
                intentService.handleFindThingsToDo(session);
                break;

            case FIND_RESTAURANTS:
                session.setSessionState(SessionState.LOOKING);
                session.getSessionStateLookingBag().setGraffittiType(GraffittiType.VENUE);
                session.getSessionStateLookingBag().setWhat(What.RESTAURANT);
                session.getSessionStateLookingBag().setGraffittiWhatCategoryNode(
                    graffittiService.findWhatCategoryNodeByConceptName("restaurants (category)")
                );
                session.getSessionStateLookingBag().setGraffittiPageNumber(1);
                intentService.handleFindRestaurants(session, nluResult.getParameters());
                break;

//            case FIND_RESTAURANTS_NEARBY:
//                session.setSessionState(SessionState.LOOKING);
//                intentFindRestaurantsHandler.handleNearby(session);
//                break;

//            case FIND_BARSANDPUBS:
//                onIntentFindBars();
//                break;
//
//            case FIND_BARSANDPUBS_NEARBY:
//                onIntentFindBarsNearby();
//                break;

//            case SET_LOCATION:
//                //TODO
//                break;

            case UNKOWN:
                //TODO: onIntentUnknown();
                break;

            default:
                messengerSendClient.sendTextMessage(
                    session.getUser().getMessengerId(),
                    "Sorry, I don't understand you."
                );
                break;
        }
    }
}
