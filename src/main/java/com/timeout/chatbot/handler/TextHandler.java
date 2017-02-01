package com.timeout.chatbot.handler;

import com.timeout.chatbot.block.ErrorBlock;
import com.timeout.chatbot.domain.What;
import com.timeout.chatbot.domain.nlu.NluResult;
import com.timeout.chatbot.graffitti.domain.GraffittiType;
import com.timeout.chatbot.handler.intent.IntentService;
import com.timeout.chatbot.messenger4j.send.MessengerSendClientWrapper;
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
    private final MessengerSendClientWrapper messengerSendClientWrapper;
    private final GraffittiService graffittiService;
    private final ErrorBlock errorBlock;

    @Autowired
    public TextHandler(
        IntentService intentService,
        NluService nluService,
        MessengerSendClientWrapper messengerSendClientWrapper,
        GraffittiService graffittiService,
        ErrorBlock errorBlock
    ) {
        this.intentService = intentService;
        this.nluService = nluService;
        this.messengerSendClientWrapper = messengerSendClientWrapper;
        this.graffittiService = graffittiService;
        this.errorBlock = errorBlock;
    }

    public void handle(
        String text,
        Session session
    ) {
        try {
            processNluResult(
                session,
                nluService.processText(text)
            );
        } catch (Exception e) {
            e.printStackTrace();
            errorBlock.send(session.getUser());
        }
    }

    private void processNluResult(
        Session session,
        NluResult nluResult
    ) {
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

            // find restaurants
            // find restaurants near me
            // find restaurants nearby
            // find restaurants candem

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
                messengerSendClientWrapper.sendTextMessage(
                    session.getUser().getMessengerId(),
                    "Sorry, I don't understand you."
                );
                break;
        }
    }
}
