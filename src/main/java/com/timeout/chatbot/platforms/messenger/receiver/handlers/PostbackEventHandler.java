package com.timeout.chatbot.platforms.messenger.receiver.handlers;

import com.github.messenger4j.receive.events.PostbackEvent;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.timeout.chatbot.domain.session.Session;
import com.timeout.chatbot.domain.session.SessionPool;
import com.timeout.chatbot.platforms.messenger.postback.PostbackPayloadType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class PostbackEventHandler implements com.github.messenger4j.receive.handlers.PostbackEventHandler {

    private final SessionPool sessionPool;

    private Gson gson = new Gson();

    @Autowired
    public PostbackEventHandler(SessionPool sessionPool) {
        this.sessionPool = sessionPool;
    }

    @Override
    public void handle(PostbackEvent event) {

        final Session session =
            sessionPool.getSession(
                event.getSender().getId(),
                event.getSender().getId()
            );

        JsonObject jsonPayload = gson.fromJson(event.getPayload(), JsonObject.class);
        final PostbackPayloadType postbackPayloadType =
            PostbackPayloadType.valueOf(jsonPayload.get("type").getAsString());

        if (postbackPayloadType == PostbackPayloadType.FIND_RESTAURANTS) {
            session.applyUtterance("restaurants");
        } else if (postbackPayloadType == PostbackPayloadType.FIND_CAMPINGS) {
            session.applyUtterance("campings");
        } else if (postbackPayloadType == PostbackPayloadType.FIND_OFFERS) {
            session.applyUtterance("offers");
        } else if (postbackPayloadType == PostbackPayloadType.CAMPING_MORE_INFO) {
            System.out.print("PostbackPayloadType.CAMPING_MORE_INFO");
            //TODO:
        }
    }
}
