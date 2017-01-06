package com.timeout.chatbot.messenger4j.thread;

import org.springframework.beans.factory.annotation.Value;

public class GreetingText {

    private final String pageAccessToken;

    public GreetingText(
        @Value("${messenger.pageAccessToken}") String pageAccessToken
    ) {
        this.pageAccessToken = pageAccessToken;
    }

    public void post() {
        //TODO
    }

    public void delete() {

    }
}
