package com.timeout.chatbot.session;

import com.timeout.chatbot.domain.page.Page;
import com.timeout.chatbot.domain.user.User;
import com.timeout.chatbot.messenger4j.send.MessengerSendClientWrapper;
import com.timeout.chatbot.session.context.SessionState;

public class Session {

    private final Page page;
    private final User user;
    private SessionState sessionState;
    private final SessionStateLookingBag sessionStateLookingBag;
    private long lastAccessTime;

    private final MessengerSendClientWrapper messengerSendClientWrapper;

    public Session(
        MessengerSendClientWrapper messengerSendClientWrapper,
        Page page,
        User user
    ) {
        this.messengerSendClientWrapper = messengerSendClientWrapper;
        this.page = page;
        this.user = user;

        this.sessionState = SessionState.UNDEFINED;
        this.sessionStateLookingBag = new SessionStateLookingBag();
    }

    public long getLastAccessTime() {
        return lastAccessTime;
    }

    public void setLastAccessTime(long lastAccessTime) {
        this.lastAccessTime = lastAccessTime;
    }

    public void sendTextMessage(String msg) {
        if (msg.length() > 320) {
            msg = msg.substring(0, 320);
        }

        messengerSendClientWrapper.sendTextMessage(
            user.getMessengerId(),
            msg
        );
    }

    public User getUser() {
        return user;
    }

    public Page getPage() {
        return page;
    }

    public SessionState getSessionState() {
        return sessionState;
    }

    public void setSessionState(SessionState sessionState) {
        this.sessionState = sessionState;
    }

    public SessionStateLookingBag getSessionStateLookingBag() {
        return sessionStateLookingBag;
    }

    @Override
    public String toString() {
        return String.format(
            "Session[page=%s, user=%s]",
            page, user
        );
    }
}
