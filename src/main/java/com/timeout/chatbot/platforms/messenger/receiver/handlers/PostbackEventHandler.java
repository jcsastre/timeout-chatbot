package com.timeout.chatbot.platforms.messenger.receiver.handlers;

import com.github.messenger4j.receive.events.PostbackEvent;
import com.google.gson.Gson;
import com.timeout.chatbot.domain.session.Session;
import com.timeout.chatbot.domain.session.SessionPool;
import org.json.JSONException;
import org.json.JSONObject;
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

        try {
            final String type = new JSONObject(event.getPayload()).getString("type");

            if (type.equals("restaurants")) {
                session.applyUtterance("restaurants");
            }
        } catch(JSONException exception) {
            //TODO
        }
    }
}
