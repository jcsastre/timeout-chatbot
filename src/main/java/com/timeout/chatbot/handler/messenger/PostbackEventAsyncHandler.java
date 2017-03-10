package com.timeout.chatbot.handler.messenger;

import com.github.messenger4j.exceptions.MessengerApiException;
import com.github.messenger4j.exceptions.MessengerIOException;
import com.timeout.chatbot.block.BlockError;
import com.timeout.chatbot.domain.nlu.NluException;
import com.timeout.chatbot.domain.payload.PayloadType;
import com.timeout.chatbot.handler.intent.IntentStartOverHandler;
import com.timeout.chatbot.handler.states.DefaultPayloadHandler;
import com.timeout.chatbot.services.SessionService;
import com.timeout.chatbot.session.Session;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class PostbackEventAsyncHandler {

    private final SessionService sessionService;
    private final DefaultPayloadHandler defaultPayloadHandler;
    private final BlockError blockError;
    private final IntentStartOverHandler intentStartOverHandler;

    @Autowired
    public PostbackEventAsyncHandler(
        SessionService sessionService,
        DefaultPayloadHandler defaultPayloadHandler,
        BlockError blockError,
        IntentStartOverHandler intentStartOverHandler
    ) {
        this.sessionService = sessionService;
        this.defaultPayloadHandler = defaultPayloadHandler;
        this.blockError = blockError;
        this.intentStartOverHandler = intentStartOverHandler;
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
            if (payloadType == PayloadType.start_over) {
                intentStartOverHandler.handle(session);
            } else {
                defaultPayloadHandler.handle(
                    payloadAsJson,
                    payloadType,
                    session
                );
            }
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
//                    blockError.send(session.getUser());
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
}
