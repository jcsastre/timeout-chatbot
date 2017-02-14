package com.timeout.chatbot.handler.states;

import com.github.messenger4j.exceptions.MessengerApiException;
import com.github.messenger4j.exceptions.MessengerIOException;
import com.github.messenger4j.send.MessengerSendClient;
import com.timeout.chatbot.block.quickreply.QuickReplyBuilderForCurrentSessionState;
import com.timeout.chatbot.domain.What;
import com.timeout.chatbot.domain.nlu.NluException;
import com.timeout.chatbot.domain.nlu.NluResult;
import com.timeout.chatbot.domain.nlu.intent.NluIntentType;
import com.timeout.chatbot.graffitti.domain.GraffittiType;
import com.timeout.chatbot.handler.intent.IntentService;
import com.timeout.chatbot.handler.states.submittingreview.SubmittingReviewStateTextHandler;
import com.timeout.chatbot.services.BlockService;
import com.timeout.chatbot.services.GraffittiService;
import com.timeout.chatbot.services.NluService;
import com.timeout.chatbot.session.Session;
import com.timeout.chatbot.session.state.SessionState;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class TextHandler {

    private final IntentService intentService;
    private final NluService nluService;
    private final MessengerSendClient msc;
    private final GraffittiService graffittiService;
    private final QuickReplyBuilderForCurrentSessionState quickReplyBuilderForCurrentSessionState;
    private final BlockService blockService;
    private final SubmittingReviewStateTextHandler submittingReviewStateTextHandler;

    @Autowired
    public TextHandler(
        IntentService intentService,
        NluService nluService,
        MessengerSendClient msc,
        GraffittiService graffittiService,
        QuickReplyBuilderForCurrentSessionState quickReplyBuilderForCurrentSessionState,
        BlockService blockService,
        SubmittingReviewStateTextHandler submittingReviewStateTextHandler
    ) {
        this.intentService = intentService;
        this.nluService = nluService;
        this.msc = msc;
        this.graffittiService = graffittiService;
        this.quickReplyBuilderForCurrentSessionState = quickReplyBuilderForCurrentSessionState;
        this.blockService = blockService;
        this.submittingReviewStateTextHandler = submittingReviewStateTextHandler;
    }

    public void handle(
        String text,
        Session session
    ) throws NluException, MessengerApiException, MessengerIOException, IOException, InterruptedException {

        final SessionState sessionState = session.getSessionState();

        switch (sessionState) {

            case SUBMITTING_REVIEW:
                submittingReviewStateTextHandler.handle(text, session);
                break;

            default:
                handleDefault(text, session);
        }
    }

    private void handleDefault(
        String text,
        Session session
    ) throws NluException, MessengerApiException, MessengerIOException, IOException, InterruptedException {

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
            msc.sendTextMessage(
                session.getUser().getMessengerId(),
                "Sorry, I don't understand"
            );
        }
    }

    private NluResult handleInternal(
        String text
    ) {
        if (text.equalsIgnoreCase("Things to do")) {
            return
                new NluResult(
                    NluIntentType.FIND_THINGSTODO
                );
        } else if (text.equalsIgnoreCase("Restaurants")) {
            return
                new NluResult(
                    NluIntentType.FIND_RESTAURANTS
                );
        } else if (text.equalsIgnoreCase("Bars and pubs")) {
            return
                new NluResult(
                    NluIntentType.FIND_BARSANDPUBS
                );
        } else if (text.equalsIgnoreCase("Art")) {
            return
                new NluResult(
                    NluIntentType.FIND_ART
                );
        } else if (text.equalsIgnoreCase("Theatre")) {
            return
                new NluResult(
                    NluIntentType.FIND_THEATRE
                );
        } else if (text.equalsIgnoreCase("Music")) {
            return
                new NluResult(
                    NluIntentType.FIND_MUSIC
                );
        } else if (text.equalsIgnoreCase("Nightlife")) {
            return
                new NluResult(
                    NluIntentType.FIND_NIGHTLIFE
                );
        } else if (text.equalsIgnoreCase("Film")) {
            return
                new NluResult(
                    NluIntentType.FINDS_FILMS
                );
        }

        return null;
    }

    private void processNluResult(
        Session session,
        NluResult nluResult
    ) throws MessengerApiException, MessengerIOException, IOException, InterruptedException {
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
                session.setSessionState(SessionState.SEARCHING);
                session.getSessionStateLookingBag().setGraffittiType(GraffittiType.VENUE);
                session.getSessionStateLookingBag().setWhat(What.RESTAURANT);
                session.getSessionStateLookingBag().setGraffittiWhatCategoryNode(
                    graffittiService.findWhatCategoryNodeByConceptName("restaurants (category)")
                );
                session.getSessionStateLookingBag().setGraffittiPageNumber(1);
                intentService.handleFindRestaurants(session, nluResult.getParameters());
                break;

//            case FIND_RESTAURANTS_NEARBY:
//                session.setSessionState(SessionState.SEARCHING);
//                intentFindRestaurantsHandler.handleNearby(session);
//                break;

            case FIND_BARSANDPUBS:
                msc.sendTextMessage(
                    session.getUser().getMessengerId(),
                    "Sorry, 'Bars & Pubs' is not implemented yet",
                    quickReplyBuilderForCurrentSessionState.build(session)
                );
//                onIntentFindBars();
                break;

            case FIND_ART:
                msc.sendTextMessage(
                    session.getUser().getMessengerId(),
                    "Sorry, 'Art' is not implemented yet",
                    quickReplyBuilderForCurrentSessionState.build(session)
                );
                break;

            case FIND_THEATRE:
                msc.sendTextMessage(
                    session.getUser().getMessengerId(),
                    "Sorry, 'Theatre' is not implemented yet",
                    quickReplyBuilderForCurrentSessionState.build(session)
                );
                break;

            case FIND_MUSIC:
                msc.sendTextMessage(
                    session.getUser().getMessengerId(),
                    "Sorry, 'Music' is not implemented yet",
                    quickReplyBuilderForCurrentSessionState.build(session)
                );
                break;

            case FIND_NIGHTLIFE:
                msc.sendTextMessage(
                    session.getUser().getMessengerId(),
                    "Sorry, 'Nightlife' is not implemented yet",
                    quickReplyBuilderForCurrentSessionState.build(session)
                );
                break;

            case FINDS_FILMS:
                msc.sendTextMessage(
                    session.getUser().getMessengerId(),
                    "Sorry, 'Films' is not implemented yet",
                    quickReplyBuilderForCurrentSessionState.build(session)
                );
                break;

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
                msc.sendTextMessage(
                    session.getUser().getMessengerId(),
                    "Sorry, I don't understand you."
                );
                break;
        }
    }
}
