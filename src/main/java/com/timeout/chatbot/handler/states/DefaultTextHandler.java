package com.timeout.chatbot.handler.states;

import com.github.messenger4j.exceptions.MessengerApiException;
import com.github.messenger4j.exceptions.MessengerIOException;
import com.github.messenger4j.send.MessengerSendClient;
import com.timeout.chatbot.block.quickreply.QuickReplyBuilderForCurrentSessionState;
import com.timeout.chatbot.domain.entities.Category;
import com.timeout.chatbot.domain.nlu.NluException;
import com.timeout.chatbot.domain.nlu.NluResult;
import com.timeout.chatbot.domain.nlu.intent.NluIntentType;
import com.timeout.chatbot.handler.intent.IntentService;
import com.timeout.chatbot.handler.states.booking.BookingStateTextHandler;
import com.timeout.chatbot.handler.states.submittingreview.SubmittingReviewStateTextHandler;
import com.timeout.chatbot.services.GraffittiService;
import com.timeout.chatbot.services.NluService;
import com.timeout.chatbot.session.Session;
import com.timeout.chatbot.session.bag.SessionStateSearchingBag;
import com.timeout.chatbot.session.state.SessionState;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class DefaultTextHandler {

    private final IntentService intentService;
    private final NluService nluService;
    private final MessengerSendClient msc;
    private final GraffittiService graffittiService;
    private final QuickReplyBuilderForCurrentSessionState quickReplyBuilderForCurrentSessionState;
    private final SubmittingReviewStateTextHandler submittingReviewStateTextHandler;
    private final BookingStateTextHandler bookingStateTextHandler;

    @Autowired
    public DefaultTextHandler(
        IntentService intentService,
        NluService nluService,
        MessengerSendClient msc,
        GraffittiService graffittiService,
        QuickReplyBuilderForCurrentSessionState quickReplyBuilderForCurrentSessionState,
        SubmittingReviewStateTextHandler submittingReviewStateTextHandler,
        BookingStateTextHandler bookingStateTextHandler
    ) {
        this.intentService = intentService;
        this.nluService = nluService;
        this.msc = msc;
        this.graffittiService = graffittiService;
        this.quickReplyBuilderForCurrentSessionState = quickReplyBuilderForCurrentSessionState;
        this.submittingReviewStateTextHandler = submittingReviewStateTextHandler;
        this.bookingStateTextHandler = bookingStateTextHandler;
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

            case BOOKING:
                bookingStateTextHandler.handle(text, session);
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
        if (text.equalsIgnoreCase("get started")) {
            return
                new NluResult(
                    NluIntentType.GET_STARTED
                );
        } else if (text.equalsIgnoreCase("Forget me")) {
            return
                new NluResult(
                    NluIntentType.FORGET_ME
                );
        } else if (text.equalsIgnoreCase("Things to do")) {
            return
                new NluResult(
                    NluIntentType.FIND_THINGSTODO
                );
        } else if (text.equalsIgnoreCase("Restaurants")) {
            return
                new NluResult(
                    NluIntentType.FIND_RESTAURANTS
                );
        } else if (text.equalsIgnoreCase("Hotels")) {
            return
                new NluResult(
                    NluIntentType.FIND_HOTELS
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

            case GET_STARTED:
                intentService.handleGetStarted(session);
                break;

            case FORGET_ME:
                intentService.handleForgetMe(session);
                break;

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
                handleFindRestaurants(session, nluResult);
                break;

            case FIND_HOTELS:
                handleFindHotels(session, nluResult);
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

    private void handleFindRestaurants(
        Session session,
        NluResult nluResult
    ) throws MessengerApiException, MessengerIOException, IOException, InterruptedException {

        session.setSessionState(SessionState.SEARCHING);

        final SessionStateSearchingBag searchingBag = session.getSessionStateSearchingBag();
        searchingBag.setCategory(Category.RESTAURANTS);
        searchingBag.setGraffittiPageNumber(1);

        intentService.handleFindRestaurants(session, nluResult.getParameters());
    }

    private void handleFindHotels(
        Session session,
        NluResult nluResult
    ) throws MessengerApiException, MessengerIOException, IOException, InterruptedException {

        session.setSessionState(SessionState.SEARCHING);

        final SessionStateSearchingBag searchingBag = session.getSessionStateSearchingBag();
        searchingBag.setCategory(Category.HOTELS);
        searchingBag.setGraffittiPageNumber(1);

        intentService.handleFindRestaurants(session, nluResult.getParameters());
    }
}
