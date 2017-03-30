package com.timeout.chatbot.handler.messenger.quickreply;

import com.github.messenger4j.exceptions.MessengerApiException;
import com.github.messenger4j.exceptions.MessengerIOException;
import com.github.messenger4j.receive.events.QuickReplyMessageEvent;
import com.github.messenger4j.receive.handlers.QuickReplyMessageEventHandler;
import com.timeout.chatbot.block.BlockError;
import com.timeout.chatbot.services.SessionService;
import com.timeout.chatbot.session.Session;
import org.json.JSONObject;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class QuickReplyMessageEventHandlerImpl implements QuickReplyMessageEventHandler {

    private final SessionService sessionService;
    private final QuickReplyMessageEventHandlerSearching quickReplyMessageEventHandlerSearching;
    private final QuickReplyMessageEventHandlerItem quickReplyMessageEventHandlerItem;
    private final BlockError blockError;

    public QuickReplyMessageEventHandlerImpl(
        SessionService sessionService,
        QuickReplyMessageEventHandlerSearching quickReplyMessageEventHandlerSearching,
        QuickReplyMessageEventHandlerItem quickReplyMessageEventHandlerItem,
        BlockError blockError
    ) {
        this.sessionService = sessionService;
        this.quickReplyMessageEventHandlerSearching = quickReplyMessageEventHandlerSearching;
        this.quickReplyMessageEventHandlerItem = quickReplyMessageEventHandlerItem;
        this.blockError = blockError;
    }

    @Override
    public void handle(
        QuickReplyMessageEvent event
    ) {
        handleAsync(event);
    }

    @Async
    private void handleAsync(
        QuickReplyMessageEvent event
    ) {
        final Session session =
            sessionService.getSession(
                event.getRecipient().getId(),
                event.getSender().getId()
            );

        final JSONObject payload = new JSONObject(event.getQuickReply().getPayload());

        try {

            final String payloadTypeAsString = payload.getString("type");
            if (payloadTypeAsString.startsWith("searching_")) {
                quickReplyMessageEventHandlerSearching.handle(
                    payload,
                    session
                );
            } else if (payloadTypeAsString.startsWith("item_")) {
                quickReplyMessageEventHandlerItem.handle(
                    payload,
                    session
                );
            } else if (payloadTypeAsString.startsWith("booking_")) {
                //TODO
            } else {

            }

            sessionService.persistSession(session);

        } catch (InterruptedException | IOException | MessengerApiException | MessengerIOException e) {
            e.printStackTrace();
            blockError.send(session.user.messengerId);
        }
    }
}
