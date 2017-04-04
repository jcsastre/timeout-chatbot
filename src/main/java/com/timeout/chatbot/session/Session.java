package com.timeout.chatbot.session;

import com.timeout.chatbot.domain.FbUserProfile;
import com.timeout.chatbot.domain.page.Page;
import com.timeout.chatbot.domain.user.User;
import com.timeout.chatbot.graffitti.domain.GraffittiCategory;
import com.timeout.chatbot.graffitti.domain.GraffittiType;
import com.timeout.chatbot.session.bag.SessionStateBookingBag;
import com.timeout.chatbot.session.bag.SessionStateItemBag;
import com.timeout.chatbot.session.bag.SessionStateSearchingBag;
import com.timeout.chatbot.session.bag.SessionStateSubmittingReviewBag;
import com.timeout.chatbot.session.state.SessionState;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.TimeToLive;

import javax.persistence.Id;
import java.io.Serializable;
import java.util.concurrent.TimeUnit;

@RedisHash("sessions")
public class Session implements Serializable {

    @Id
    public String id;

    @TimeToLive(unit = TimeUnit.SECONDS)
    public Long timeToLiveAsSeconds;

    public Page page;
    public User user;

    public SessionState state;

    public SessionStateSearchingBag bagSearching;
    public SessionStateItemBag bagItem;
    public SessionStateBookingBag bagBooking;
    public SessionStateSubmittingReviewBag bagSubmitting;

    public FbUserProfile fbUserProfile;

    public static String buildId(
        String recipientId,
        String senderId
    ) {
        return recipientId + "-" + senderId;
    }

    public void updateToSearchRestaurants() {
        state = SessionState.SEARCHING;

        bagSearching = new SessionStateSearchingBag();
        bagSearching.graffittiCategory = GraffittiCategory.RESTAURANTS;
        bagSearching.graffittiType = GraffittiType.VENUE;
    }

    public void updateToSearchHotels() {
        state = SessionState.SEARCHING;

        bagSearching = new SessionStateSearchingBag();
        bagSearching.graffittiCategory = GraffittiCategory.HOTELS;
        bagSearching.graffittiType = GraffittiType.VENUE;
    }

    @Override
    public String toString() {
        return String.format(
            "Session {state=%s, bagSearching=%s, bagItem=%s, PAGE=%s, user=%s}",
            state, bagSearching, bagItem, page, user
        );
    }
}
