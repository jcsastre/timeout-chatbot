package com.timeout.chatbot.session.bag;

import com.timeout.chatbot.session.state.SubmittingReviewState;

public class SessionStateSubmittingReviewBag {

    private SubmittingReviewState submittingReviewState;
    private Integer rate;
    private String comment;

    public SubmittingReviewState getSubmittingReviewState() {
        return submittingReviewState;
    }

    public void setSubmittingReviewState(SubmittingReviewState submittingReviewState) {
        this.submittingReviewState = submittingReviewState;
    }

    public Integer getRate() {
        return rate;
    }

    public void setRate(Integer rate) {
        this.rate = rate;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }
}
