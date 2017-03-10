package com.timeout.chatbot.session;

import com.timeout.chatbot.domain.FbUserProfile;
import com.timeout.chatbot.domain.page.Page;
import com.timeout.chatbot.domain.user.User;
import com.timeout.chatbot.session.bag.SessionStateBookingBag;
import com.timeout.chatbot.session.bag.SessionStateItemBag;
import com.timeout.chatbot.session.bag.SessionStateSearchingBag;
import com.timeout.chatbot.session.bag.SessionStateSubmittingReviewBag;
import com.timeout.chatbot.session.state.SessionState;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.TimeToLive;

import javax.persistence.Id;
import java.io.Serializable;
import java.util.Date;
import java.util.concurrent.TimeUnit;

@RedisHash("sessions")
public class Session implements Serializable {

    private static final Logger logger = LoggerFactory.getLogger(Session.class);

    @Id
    private String id;

    @TimeToLive(unit = TimeUnit.SECONDS)
    private Long timeToLiveAsSeconds;

    private Page page;
    private User user;

    private SessionState sessionState;
    private SessionStateSearchingBag sessionStateSearchingBag;
    private SessionStateItemBag sessionStateItemBag;
    private SessionStateBookingBag sessionStateBookingBag;
    private SessionStateSubmittingReviewBag sessionStateSubmittingReviewBag;
    private long lastAccessTime;
    private Date currentTimestamp;
    private FbUserProfile fbUserProfile;

    public Session() {
        logger.debug("Session()");
//        this.sessionState = SessionState.UNDEFINED;
//        this.sessionStateSearchingBag = new SessionStateSearchingBag();
//        this.sessionStateItemBag = new SessionStateItemBag();
//        this.sessionStateBookingBag = new SessionStateBookingBag();
//        this.sessionStateSubmittingReviewBag = new SessionStateSubmittingReviewBag();
    }

    public static String buildId(
        String recipientId,
        String senderId
    ) {
        return recipientId + "-" + senderId;
    }

    public void reset() {
        logger.debug("reset()");
        this.sessionState = SessionState.UNDEFINED;
        this.sessionStateSearchingBag = new SessionStateSearchingBag();
        this.sessionStateItemBag = new SessionStateItemBag();
        this.sessionStateBookingBag = new SessionStateBookingBag();
        this.sessionStateSubmittingReviewBag = new SessionStateSubmittingReviewBag();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        logger.debug("setId()");
        this.id = id;
    }

    public Page getPage() {
        return page;
    }

    public void setPage(Page page) {
        logger.debug("setPage("+page+")");
        this.page = page;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        logger.debug("setUser()");
        this.user = user;
    }

    public Long getTimeToLiveAsSeconds() {
        return timeToLiveAsSeconds;
    }

    public void setTimeToLiveAsSeconds(Long timeToLiveAsSeconds) {
        this.timeToLiveAsSeconds = timeToLiveAsSeconds;
    }

    public long getLastAccessTime() {
        return lastAccessTime;
    }

    public void setLastAccessTime(long lastAccessTime) {
        this.lastAccessTime = lastAccessTime;
    }


    public SessionState getSessionState() {
        return sessionState;
    }

    public void setSessionState(SessionState sessionState) {
        logger.debug("setSessionState("+ sessionState +")");
        this.sessionState = sessionState;
    }

    public void setSessionStateSearchingBag(SessionStateSearchingBag sessionStateSearchingBag) {
        this.sessionStateSearchingBag = sessionStateSearchingBag;
    }

    public SessionStateSearchingBag getSessionStateSearchingBag() {
        return sessionStateSearchingBag;
    }

    public void setSessionStateItemBag(SessionStateItemBag setSessionStateItemBag) {
        logger.debug("setSessionStateItemBag("+ setSessionStateItemBag +")");
        this.sessionStateItemBag = sessionStateItemBag;
    }

    public SessionStateItemBag getSessionStateItemBag() {
        return sessionStateItemBag;
    }

    public void setSessionStateBookingBag(SessionStateBookingBag sessionStateBookingBag) {
        this.sessionStateBookingBag = sessionStateBookingBag;
    }

    public SessionStateBookingBag getSessionStateBookingBag() {
        return sessionStateBookingBag;
    }

    public void setSessionStateSubmittingReviewBag(SessionStateSubmittingReviewBag sessionStateSubmittingReviewBag) {
        this.sessionStateSubmittingReviewBag = sessionStateSubmittingReviewBag;
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
            "Session[page=%s, state=%s, stateItemBag=%s]",
            page, sessionState, sessionStateItemBag
        );
    }
}
