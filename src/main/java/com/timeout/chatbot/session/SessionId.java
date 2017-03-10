package com.timeout.chatbot.session;

public class SessionId {

    private final String value;

    public SessionId(
        String pageId,
        String userId
    ) {
        this.value = pageId + "-" + userId;
    }

    public String getValue() {
        return value;
    }
}
