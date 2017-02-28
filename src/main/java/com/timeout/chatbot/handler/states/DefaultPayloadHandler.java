package com.timeout.chatbot.handler.states;

import com.github.messenger4j.exceptions.MessengerApiException;
import com.github.messenger4j.exceptions.MessengerIOException;
import com.github.messenger4j.send.MessengerSendClient;
import com.timeout.chatbot.block.cloudinary.CloudinaryUrlBuilder;
import com.timeout.chatbot.block.quickreply.QuickReplyBuilderForCurrentSessionState;
import com.timeout.chatbot.domain.nlu.NluException;
import com.timeout.chatbot.domain.payload.PayloadType;
import com.timeout.chatbot.handler.intent.IntentService;
import com.timeout.chatbot.handler.states.booking.BookingStatePayloadHandler;
import com.timeout.chatbot.handler.states.item.ItemStatePayloadHandler;
import com.timeout.chatbot.handler.states.searching.SearchingStatePayloadHandler;
import com.timeout.chatbot.handler.states.submittingreview.SubmittingReviewStatePayloadHandler;
import com.timeout.chatbot.messenger4j.SenderActionsHelper;
import com.timeout.chatbot.services.BlockService;
import com.timeout.chatbot.session.Session;
import com.timeout.chatbot.session.state.SessionState;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalTime;

@Component
public class DefaultPayloadHandler {

    private final IntentService intentService;
    private final BlockService blockService;
    private final MessengerSendClient msc;
    private final DefaultTextHandler defaultTextHandler;
    private final QuickReplyBuilderForCurrentSessionState quickReplyBuilderForCurrentSessionState;
    private final SubmittingReviewStatePayloadHandler submittingReviewStatePayloadHandler;
    private final BookingStatePayloadHandler bookingStatePayloadHandler;
    private final ItemStatePayloadHandler itemStatePayloadHandler;
    private final SearchingStatePayloadHandler searchingStatePayloadHandler;
    private final SenderActionsHelper senderActionsHelper;
    private final CloudinaryUrlBuilder cloudinaryUrlBuilder;

    @Autowired
    public DefaultPayloadHandler(
        IntentService intentService,
        BlockService blockService,
        MessengerSendClient msc,
        DefaultTextHandler defaultTextHandler,
        QuickReplyBuilderForCurrentSessionState quickReplyBuilderForCurrentSessionState,
        SubmittingReviewStatePayloadHandler submittingReviewStatePayloadHandler,
        BookingStatePayloadHandler bookingStatePayloadHandler,
        ItemStatePayloadHandler itemStatePayloadHandler,
        SearchingStatePayloadHandler searchingStatePayloadHandler,
        SenderActionsHelper senderActionsHelper,
        CloudinaryUrlBuilder cloudinaryUrlBuilder) {
        this.intentService = intentService;
        this.blockService = blockService;
        this.msc = msc;
        this.defaultTextHandler = defaultTextHandler;
        this.quickReplyBuilderForCurrentSessionState = quickReplyBuilderForCurrentSessionState;
        this.submittingReviewStatePayloadHandler = submittingReviewStatePayloadHandler;
        this.bookingStatePayloadHandler = bookingStatePayloadHandler;
        this.itemStatePayloadHandler = itemStatePayloadHandler;
        this.searchingStatePayloadHandler = searchingStatePayloadHandler;
        this.senderActionsHelper = senderActionsHelper;
        this.cloudinaryUrlBuilder = cloudinaryUrlBuilder;
    }

    public void handle(
        String payloadAsString,
        Session session
    ) throws NluException, MessengerIOException, MessengerApiException, IOException, InterruptedException {

        final JSONObject payload = new JSONObject(payloadAsString);
        final PayloadType payloadType = PayloadType.valueOf(payload.getString("type"));

        if (payloadType == PayloadType.start_over) {

            session.reset();

            msc.sendImageAttachment(
                session.getUser().getMessengerId(),
                cloudinaryUrlBuilder.buildBookReceiptUrl(
                    2,
                    LocalDate.now(),
                    LocalTime.now(),
                    "101611815",
                    "La Esquinica",
                    "Juan Carlos Sastre",
                    "6 Greek Street",
                    "London",
                    "W1D 4DE"
                )
            );

//            blockService.sendWelcomeBackBlock(session.getUser());
//            intentService.handleDiscover(session);

//            blockService.sendWelcomeFirstTimeBlock(session.getUser());
//            blockService.sendVersionInfoBlock(session.getUser().getMessengerId());

        } else if (payloadType == PayloadType.discover) {
            intentService.handleDiscover(session);
        } else {

            final SessionState sessionState = session.getSessionState();

            try {
                switch (sessionState) {

                    case ITEM:
                        itemStatePayloadHandler.handle(session, payload);
                        break;

                    case SUBMITTING_REVIEW:
                        submittingReviewStatePayloadHandler.handle(session, payload);
                        break;

                    case BOOKING:
                        bookingStatePayloadHandler.handle(session, payload);
                        break;

                    case SEARCHING:
                        searchingStatePayloadHandler.handle(session, payload);
                        break;

                    default:
                        handleInternal(session, payload);
                }
            } catch (Exception e) {
                e.printStackTrace();
                blockService.sendErrorBlock(session.getUser());
            }
        }
    }

    private void handleInternal(
        Session session,
        JSONObject payload
    ) throws NluException, MessengerIOException, MessengerApiException, IOException, InterruptedException {

        final PayloadType payloadType = PayloadType.valueOf(payload.getString("type"));

        switch (payloadType) {

            case get_started:
                intentService.handleGetStarted(session);
                break;

            case utterance:
                final String utterance = payload.getString("utterance");
                defaultTextHandler.handle(utterance, session);
                break;

//            case help:
//                intentService.handleHelp(session);
//                break;

//            case search_suggestions:
//                intentService.handleSuggestions(session);
////                session.setSessionState(SessionState.SEARCH_SUGGESTIONS);
////                blockService.sendSuggestionsBlock(session);
//                break;

            case most_loved:
                session.setSessionState(SessionState.MOST_LOVED);
                blockService.sendMostLovedBlock(session);
                break;

//            case whats_new:
//                session.setSessionState(SessionState.WHATS_NEW);
//                blockService.sendWhatsNewBlock(session);
//                break;

            case set_geolocation:
                blockService.sendGeolocationAskBlock(session.getUser().getMessengerId());
                break;

            case get_a_summary:
                intentService.handleGetasummary(session);
                break;

            case venues_more_info:
                blockService.sendVenueSummaryBlock(
                    session.getUser().getMessengerId(),
                    payload.getString("venue_id")
                );
                break;

            case no_see_at_timeout:
                msc.sendTextMessage(
                    session.getUser().getMessengerId(),
                    "Ok, item_Back to the list of restaurants"
                );
                intentService.handleFindRestaurants(session);
                break;

            case films_find_cinemas:
                msc.sendTextMessage(
                    session.getUser().getMessengerId(),
                    "Sorry, 'Find a cinema' is not implemented yet"
                );
                break;

            case temporaly_disabled:
                msc.sendTextMessage(
                    session.getUser().getMessengerId(),
                    "Sorry, my creator has temporarily disabled the 'Search suggestions' :(",
                    quickReplyBuilderForCurrentSessionState.build(session)
                );
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
