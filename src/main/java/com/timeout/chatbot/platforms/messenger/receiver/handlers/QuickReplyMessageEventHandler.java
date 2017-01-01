package com.timeout.chatbot.platforms.messenger.receiver.handlers;

import com.github.messenger4j.receive.events.QuickReplyMessageEvent;
import com.timeout.chatbot.domain.session.Session;
import com.timeout.chatbot.domain.session.SessionPool;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class QuickReplyMessageEventHandler implements com.github.messenger4j.receive.handlers.QuickReplyMessageEventHandler {
    @Autowired
    private SessionPool sessionPool;

    @Override
    public void handle(QuickReplyMessageEvent event) {
        final Session session =
            sessionPool.getSession(
                event.getRecipient().getId(),
                event.getSender().getId()
            );

        final JSONObject payloadAsJson = new JSONObject(event.getQuickReply().getPayload());

        try {
            final String utterance = payloadAsJson.getString("utterance");
            session.applyUtterance(utterance);
        } catch(JSONException e) {
            session.sendTextMessage("Lo siento ha ocurrido un error.");
        }
    }
}
