package com.timeout.chatbot.handler.intent;

import com.github.messenger4j.exceptions.MessengerApiException;
import com.github.messenger4j.exceptions.MessengerIOException;
import com.timeout.chatbot.session.Session;

public abstract class IntentHandler {

    public void handle(Session session) throws MessengerApiException, MessengerIOException {

        switch (session.state) {

            case UNDEFINED:
                handleUndefined(session);
                break;

            case SEARCH_SUGGESTIONS:
                handleSearchSuggestions(session);
                break;

            case DISCOVER:
                handleDiscover(session);
                break;

            case MOST_LOVED:
                handleMostLoved(session);
                break;

            case SEARCHING:
                handleSearching(session);
                break;

            case ITEM:
                handleItem(session);
                break;

            case SUBMITTING_REVIEW:
                handleSubmittingReview(session);
                break;

            case BOOKING:
                handleBooking(session);
                break;

            default:
                handleDefault(session);
        }
    }

    public abstract void handleUndefined(Session session);
    public abstract void handleSearchSuggestions(Session session);
    public abstract void handleDiscover(Session session);
    public abstract void handleMostLoved(Session session);
    public abstract void handleSearching(Session session);
    public abstract void handleItem(Session session);
    public abstract void handleSubmittingReview(Session session);
    public abstract void handleBooking(Session session);
    public abstract void handleDefault(Session session);

}
