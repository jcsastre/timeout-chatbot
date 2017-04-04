package com.timeout.chatbot.session.state;

import java.io.Serializable;

public enum SubmittingReviewState implements Serializable {

    RATING,
    WRITING_COMMENT,
    ASKING_FOR_CONFIRMATION
}
