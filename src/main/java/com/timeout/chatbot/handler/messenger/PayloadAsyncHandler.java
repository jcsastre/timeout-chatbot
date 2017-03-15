package com.timeout.chatbot.handler.messenger;

import com.github.messenger4j.exceptions.MessengerApiException;
import com.github.messenger4j.exceptions.MessengerIOException;
import com.github.messenger4j.send.MessengerSendClient;
import com.timeout.chatbot.block.BlockError;
import com.timeout.chatbot.block.quickreply.QuickReplyBuilderForCurrentSessionState;
import com.timeout.chatbot.domain.nlu.NluException;
import com.timeout.chatbot.domain.payload.PayloadType;
import com.timeout.chatbot.graffitti.domain.GraffittiType;
import com.timeout.chatbot.handler.intent.IntentService;
import com.timeout.chatbot.handler.intent.IntentStartOverHandler;
import com.timeout.chatbot.handler.states.DefaultTextHandler;
import com.timeout.chatbot.services.SessionService;
import com.timeout.chatbot.session.Session;
import com.timeout.chatbot.session.bag.SessionStateItemBag;
import com.timeout.chatbot.session.state.SessionState;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class PayloadAsyncHandler {

    private final SessionService sessionService;
    private final IntentService intentService;
    private final BlockError blockError;
    private final IntentStartOverHandler intentStartOverHandler;
    private final DefaultTextHandler defaultTextHandler;
    private final MessengerSendClient msc;
    private final QuickReplyBuilderForCurrentSessionState quickReplyBuilderForCurrentSessionState;

    @Autowired
    public PayloadAsyncHandler(
        SessionService sessionService,
        IntentService intentService,
        BlockError blockError,
        IntentStartOverHandler intentStartOverHandler,
        DefaultTextHandler defaultTextHandler,
        MessengerSendClient msc,
        QuickReplyBuilderForCurrentSessionState quickReplyBuilderForCurrentSessionState
    ) {
        this.sessionService = sessionService;
        this.intentService = intentService;
        this.blockError = blockError;
        this.intentStartOverHandler = intentStartOverHandler;
        this.defaultTextHandler = defaultTextHandler;
        this.msc = msc;
        this.quickReplyBuilderForCurrentSessionState = quickReplyBuilderForCurrentSessionState;
    }

    @Async
    void handleAsync(
        String payloadAsString,
        String recipientId,
        String senderId
    ) {
        try {
            Session session = sessionService.getSession(recipientId, senderId);

            final JSONObject payloadAsJson = new JSONObject(payloadAsString);

            final PayloadType payloadType = PayloadType.valueOf(payloadAsJson.getString("type"));

            switch (payloadType) {

                case _StartOver:
                    intentStartOverHandler.handle(session);
                    break;

                case _GetStarted:
                    intentService.handleGetStarted(session);
                    break;

                case _Utterance:
                    final String utterance = payloadAsJson.getString("utterance");
                    defaultTextHandler.handle(utterance, session);
                    break;

                case _SearchSuggestions:
                    //TODO
                    break;

                case _Discover:
                    intentService.handleDiscover(session);
                    break;

                case _WhatsNew:
                    //TODO
                    break;

                case _MostLoved:
                    //TODO
                    break;

                case _Cancel:
                    //TODO
                    break;
//                case _Cancel:
//                    venuesRemainingBlock.send(session);
//                    break;

                case _GetASummary:
                    intentService.handleGetasummary(session);
                    break;

                case _TemporalyDisabled:
                    msc.sendTextMessage(
                        session.user.messengerId,
                        "Sorry, my creator has temporarily disabled the 'Search suggestions' :(",
                        quickReplyBuilderForCurrentSessionState.build(session)
                    );
                    break;

                case searching_ItemMoreOptions:
                    handleSearchingItemMoreOptions(session, payloadAsJson);
                    break;

                case searching_VenuesShowAreas:
                    handleVenuesShowAreas(session, payload);
                    break;

                case searching_ShowSubcategories:
                    handleShowSubcategories(session, payload);
                    break;

                case searching_VenuesSetNeighborhood:
                    handleVenuesSetNeighborhood(session, payload);
                    break;

                case searching_SetSubcategory:
                    handleSetSubcategory(session, payload);
                    break;

                case searching_WhereEverywhere:
                    handleWhereEverywhere(session);
                    break;

                case searching_SeeMore:
                    handleSeeMore(session);
                    break;
            }

//            if (payloadType == PayloadType._StartOver) {
//                intentStartOverHandler.handle(session);
//            } else {
//                defaultPayloadHandler.handle(
//                    payloadAsJson,
//                    payloadType,
//                    session
//                );
//            }

            sessionService.persistSession(session);
        } catch (MessengerApiException | MessengerIOException | NluException | InterruptedException | IOException e) {
            e.printStackTrace();
            blockError.send(senderId);
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
    }

    private void handleSearchingItemMoreOptions(
        Session session,
        JSONObject payload
    ) throws InterruptedException, MessengerApiException, MessengerIOException, IOException {

        intentService.handleSeeItem(
            session,
            GraffittiType.fromValue(payload.getString("item_type")),
            payload.getString("item_id")
        );
    }
}
