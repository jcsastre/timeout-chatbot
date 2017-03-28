package com.timeout.chatbot.handler.states;

import com.github.messenger4j.exceptions.MessengerApiException;
import com.github.messenger4j.exceptions.MessengerIOException;
import com.github.messenger4j.send.MessengerSendClient;
import com.timeout.chatbot.block.cloudinary.CloudinaryUrlBuilder;
import com.timeout.chatbot.block.quickreply.QuickReplyBuilderForCurrentSessionState;
import com.timeout.chatbot.domain.nlu.NluException;
import com.timeout.chatbot.domain.payload.PayloadType;
import com.timeout.chatbot.handler.states.booking.BookingStatePayloadHandler;
import com.timeout.chatbot.handler.states.item.ItemStatePayloadHandler;
import com.timeout.chatbot.handler.states.submittingreview.SubmittingReviewStatePayloadHandler;
import com.timeout.chatbot.messenger4j.SenderActionsHelper;
import com.timeout.chatbot.services.BlockService;
import com.timeout.chatbot.services.SessionService;
import com.timeout.chatbot.session.Session;
import com.timeout.chatbot.session.state.SessionState;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class DefaultPayloadHandler {

    private static final Logger logger = LoggerFactory.getLogger(DefaultPayloadHandler.class);

    private final BlockService blockService;
    private final MessengerSendClient msc;
    private final DefaultTextHandler defaultTextHandler;
    private final QuickReplyBuilderForCurrentSessionState quickReplyBuilderForCurrentSessionState;
    private final SubmittingReviewStatePayloadHandler submittingReviewStatePayloadHandler;
    private final BookingStatePayloadHandler bookingStatePayloadHandler;
    private final ItemStatePayloadHandler itemStatePayloadHandler;
    private final SenderActionsHelper senderActionsHelper;
    private final CloudinaryUrlBuilder cloudinaryUrlBuilder;
    private final SessionService sessionService;

    @Autowired
    public DefaultPayloadHandler(
        BlockService blockService,
        MessengerSendClient msc,
        DefaultTextHandler defaultTextHandler,
        QuickReplyBuilderForCurrentSessionState quickReplyBuilderForCurrentSessionState,
        SubmittingReviewStatePayloadHandler submittingReviewStatePayloadHandler,
        BookingStatePayloadHandler bookingStatePayloadHandler,
        ItemStatePayloadHandler itemStatePayloadHandler,
        SenderActionsHelper senderActionsHelper,
        CloudinaryUrlBuilder cloudinaryUrlBuilder,
        SessionService sessionService
    ) {
        this.blockService = blockService;
        this.msc = msc;
        this.defaultTextHandler = defaultTextHandler;
        this.quickReplyBuilderForCurrentSessionState = quickReplyBuilderForCurrentSessionState;
        this.submittingReviewStatePayloadHandler = submittingReviewStatePayloadHandler;
        this.bookingStatePayloadHandler = bookingStatePayloadHandler;
        this.itemStatePayloadHandler = itemStatePayloadHandler;
        this.senderActionsHelper = senderActionsHelper;
        this.cloudinaryUrlBuilder = cloudinaryUrlBuilder;
        this.sessionService = sessionService;
    }

    public void handle(
        JSONObject payloadAsJson,
        PayloadType payloadType,
        Session session
    ) throws NluException, MessengerIOException, MessengerApiException, IOException, InterruptedException {

        final SessionState sessionState = session.state;

        try {
            switch (sessionState) {

                case ITEM:
                    itemStatePayloadHandler.handle(session, payloadAsJson);
                    break;

                case SUBMITTING_REVIEW:
                    submittingReviewStatePayloadHandler.handle(session, payloadAsJson);
                    break;

                case BOOKING:
                    bookingStatePayloadHandler.handle(session, payloadAsJson);
                    break;

                default:
                    handleInternal(session, payloadAsJson);
            }
        } catch (Exception e) {
            e.printStackTrace();
//            blockService.sendErrorBlock(session.user);
        }
    }

    private void handleInternal(
        Session session,
        JSONObject payload
    ) throws NluException, MessengerIOException, MessengerApiException, IOException, InterruptedException {

        final PayloadType payloadType = PayloadType.valueOf(payload.getString("type"));

        switch (payloadType) {

//            case _GetStarted:
//                intentService.handleGetStarted(session);
//                break;
//
//            case _Utterance:
//                final String utterance = payload.getString("utterance");
//                defaultTextHandler.perform(utterance, session);
//                break;

//            case help:
//                intentService.handleHelp(session);
//                break;

//            case _SearchSuggestions:
//                intentService.handleSuggestions(session);
////                session.setSessionState(SessionState.SEARCH_SUGGESTIONS);
////                blockService.sendSuggestionsBlock(session);
//                break;

//            case _MostLoved:
//                session.state = SessionState.MOST_LOVED;
//                blockService.sendMostLovedBlock(session);
//                break;

//            case _WhatsNew:
//                session.setSessionState(SessionState.WHATS_NEW);
//                blockService.sendWhatsNewBlock(session);
//                break;

            case set_geolocation:
                blockService.sendGeolocationAskBlock(session.user.messengerId);
                break;

            case venues_more_info:
                blockService.sendVenueSummaryBlock(
                    session.user.messengerId,
                    payload.getString("venue_id")
                );
                break;

//            case no_see_at_timeout:
//                msc.sendTextMessage(
//                    session.user.messengerId,
//                    "Ok, back to the list of restaurants"
//                );
//                intentService.handleFindRestaurants(session);
//                break;

            case films_find_cinemas:
                msc.sendTextMessage(
                    session.user.messengerId,
                    "Sorry, 'Find a cinema' is not implemented yet"
                );
                break;

//            case _TemporalyDisabled:
//                msc.sendTextMessage(
//                    session.user.messengerId,
//                    "Sorry, my creator has temporarily disabled the 'Search suggestions' :(",
//                    quickReplyBuilderForCurrentSessionState.build(session)
//                );
//                break;

            default:
                msc.sendTextMessage(
                    session.user.messengerId,
                    "Sorry, I don't understand you."
                );
                break;
        }
    }
}
