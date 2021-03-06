package com.timeout.chatbot.session.bag;

import com.timeout.chatbot.session.state.SubmittingReviewState;

import java.io.Serializable;

public class SessionStateSubmittingReviewBag implements Serializable {

    public SubmittingReviewState state;
    public Integer rate;
    public String comment;

    public SessionStateSubmittingReviewBag() {
        this.state = SubmittingReviewState.RATING;
        this.rate = null;
        this.comment = null;
    }
}
