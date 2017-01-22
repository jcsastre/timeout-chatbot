package com.timeout.chatbot.session.context;

public interface SessionContext {

    public void applyUtterance(String utterance);

    public void applyPayload(String payloadAsJsonString);
}
