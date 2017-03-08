package com.timeout.chatbot.session;

import com.timeout.chatbot.domain.FbUserProfile;
import com.timeout.chatbot.domain.page.Page;
import com.timeout.chatbot.domain.user.User;
import com.timeout.chatbot.session.bag.SessionStateBookingBag;
import com.timeout.chatbot.session.bag.SessionStateItemBag;
import com.timeout.chatbot.session.bag.SessionStateSearchingBag;
import com.timeout.chatbot.session.bag.SessionStateSubmittingReviewBag;
import com.timeout.chatbot.session.state.SessionState;

import java.io.Serializable;
import java.util.Date;

public class Session implements Serializable {

    private final Page page;
    private final User user;
    private SessionState sessionState;
    private SessionStateSearchingBag sessionStateSearchingBag;
    private SessionStateItemBag sessionStateItemBag;
    private SessionStateBookingBag sessionStateBookingBag;
    private SessionStateSubmittingReviewBag sessionStateSubmittingReviewBag;
    private long lastAccessTime;
    private Date currentTimestamp;
    private FbUserProfile fbUserProfile;

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
        this.sessionStateSearchingBag = new SessionStateSearchingBag();
        this.sessionStateItemBag = new SessionStateItemBag();
        this.sessionStateBookingBag = new SessionStateBookingBag();
        this.sessionStateSubmittingReviewBag = new SessionStateSubmittingReviewBag();
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

    public SessionStateSearchingBag getSessionStateSearchingBag() {
        return sessionStateSearchingBag;
    }

    public SessionStateItemBag getSessionStateItemBag() {
        return sessionStateItemBag;
    }

    public SessionStateBookingBag getSessionStateBookingBag() {
        return sessionStateBookingBag;
    }

    public SessionStateSubmittingReviewBag getSessionStateSubmittingReviewBag() {
        return sessionStateSubmittingReviewBag;
    }

    public Date getCurrentTimestamp() {
        return currentTimestamp;
    }

    public void setCurrentTimestamp(Date currentTimestamp) {
        this.currentTimestamp = currentTimestamp;
    }

    public FbUserProfile getFbUserProfile() {
        return fbUserProfile;
    }

    public void setFbUserProfile(FbUserProfile fbUserProfile) {
        this.fbUserProfile = fbUserProfile;
    }

    @Override
    public String toString() {
        return String.format(
            "Session[page=%s, user=%s]",
            page, user
        );
    }
}
