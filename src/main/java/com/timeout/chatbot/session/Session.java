package com.timeout.chatbot.session;

import com.timeout.chatbot.domain.page.Page;
import com.timeout.chatbot.domain.user.User;
import com.timeout.chatbot.session.context.SessionState;

public class Session {

    private final Page page;
    private final User user;
    private SessionState sessionState;
    private SessionStateLookingBag sessionStateLookingBag;
    private SessionStateItemBag sessionStateItemBag;
    private long lastAccessTime;

    public Session(
        Page page,
        User user
    ) {
        this.page = page;
        this.user = user;

        reset();
    }

    public void reset() {
        this.sessionState = SessionState.UNDEFINED;
        this.sessionStateLookingBag = new SessionStateLookingBag();
        this.sessionStateItemBag = new SessionStateItemBag();
    }

    public long getLastAccessTime() {
        return lastAccessTime;
    }

    public void setLastAccessTime(long lastAccessTime) {
        this.lastAccessTime = lastAccessTime;
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

    public SessionStateItemBag getSessionStateItemBag() {
        return sessionStateItemBag;
    }

    @Override
    public String toString() {
        return String.format(
            "Session[page=%s, user=%s]",
            page, user
        );
    }
}
