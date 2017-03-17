package com.timeout.chatbot.handler.intent;

import com.github.messenger4j.exceptions.MessengerApiException;
import com.github.messenger4j.exceptions.MessengerIOException;
import com.timeout.chatbot.session.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class IntentService {

    private final IntentGetStartedHandler getStartedHandler;
    private final IntentStartOverHandler startOverHandler;
    private final IntentHelpHandler helpHandler;
    private final IntentGreetingsHandler greetingsHandler;
    private final IntentSuggestionsHandler suggestionsHandler;
    private final IntentDiscoverHandler discoverHandler;
    private final IntentWhatsnewHandler whatsnewHandler;
    private final IntentFindThingsToDoHandler findThingsToDoHandler;
    private final IntentFindVenuesHandler intentFindVenuesHandler;
    private final IntentSeeItem seeItem;
    private final IntentGetasummaryHandler getasummaryHandler;
    private final IntentForgetMeHandler forgetMeHandler;

    @Autowired
    public IntentService(
        IntentGetStartedHandler getStartedHandler,
        IntentStartOverHandler startOverHandler, IntentHelpHandler helpHandler,
        IntentGreetingsHandler greetingsHandler,
        IntentSuggestionsHandler suggestionsHandler,
        IntentDiscoverHandler discoverHandler,
        IntentWhatsnewHandler whatsnewHandler,
        IntentFindThingsToDoHandler findThingsToDoHandler,
        IntentFindVenuesHandler intentFindVenuesHandler,
        IntentSeeItem seeItem,
        IntentGetasummaryHandler getasummaryHandler,
        IntentForgetMeHandler forgetMeHandler
    ) {
        this.getStartedHandler = getStartedHandler;
        this.startOverHandler = startOverHandler;
        this.helpHandler = helpHandler;
        this.greetingsHandler = greetingsHandler;
        this.suggestionsHandler = suggestionsHandler;
        this.discoverHandler = discoverHandler;
        this.whatsnewHandler = whatsnewHandler;
        this.findThingsToDoHandler = findThingsToDoHandler;
        this.intentFindVenuesHandler = intentFindVenuesHandler;
        this.seeItem = seeItem;
        this.getasummaryHandler = getasummaryHandler;
        this.forgetMeHandler = forgetMeHandler;
    }

    public IntentGetStartedHandler getIntentGetStartedHandler() {
        return getStartedHandler;
    }

    public IntentStartOverHandler getIntentStartOverHandler() {
        return startOverHandler;
    }

    public IntentFindVenuesHandler getIntentFindVenuesHandler() { return intentFindVenuesHandler; }

    public IntentDiscoverHandler getIntentDiscoverHandler() { return discoverHandler; }

    public IntentSeeItem getIntentSeeItem() {return seeItem; }

    public void handleHelp(Session session) {
        helpHandler.handle(session);
    }

    public void handleGreetings(Session session) throws MessengerApiException, MessengerIOException {
        greetingsHandler.handle(session);
    }

    public void handleSuggestions(Session session) throws MessengerApiException, MessengerIOException {
        suggestionsHandler.handle(session);
    }

    public void handleWhatsnew(Session session) {
        whatsnewHandler.handle(session);
    }

    public void handleFindThingsToDo(Session session) throws MessengerApiException, MessengerIOException { findThingsToDoHandler.handle(session); }

//    public void handleFindRestaurants(
//        Session session,
//        HashMap<String, JsonElement> nluParameters
//    ) throws MessengerApiException, MessengerIOException, IOException, InterruptedException {
//        intentFindVenuesHandler.handle(session, nluParameters);
//    }
//
//    public void handleFindRestaurants(
//        Session session
//    ) throws MessengerApiException, MessengerIOException, IOException, InterruptedException {
//        intentFindVenuesHandler.handle(session);
//    }

    public void handleGetasummary(
        Session session
    ) throws MessengerApiException, MessengerIOException {
        getasummaryHandler.handle(session);
    }

    public void handleForgetMe(
        Session session
    ) throws MessengerApiException, MessengerIOException {

        forgetMeHandler.handle(session);
    }

    public void handleGetStarted(
        Session session
    ) throws MessengerApiException, MessengerIOException {

        getStartedHandler.handle(session);
    }
}
