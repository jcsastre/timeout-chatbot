package com.timeout.chatbot.handler.intent;

import com.github.messenger4j.exceptions.MessengerApiException;
import com.github.messenger4j.exceptions.MessengerIOException;
import com.google.gson.JsonElement;
import com.timeout.chatbot.session.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.HashMap;

@Component
public class IntentService {

    private final IntentHelpHandler helpHandler;
    private final IntentGreetingsHandler greetingsHandler;
    private final IntentSuggestionsHandler suggestionsHandler;
    private final IntentDiscoverHandler discoverHandler;
    private final IntentWhatsnewHandler whatsnewHandler;
    private final IntentSeemoreHandler seemoreHandler;
    private final IntentFindThingsToDoHandler findThingsToDoHandler;
    private final IntentFindRestaurantsHandler findRestaurantsHandler;
    private final IntentSetSubcategoryHandler setSubcategoryHandler;
    private final IntentCancelHandler cancelHandler;
    private final IntentSeeItem seeItem;
    private final IntentGetasummaryHandler getasummaryHandler;
    private final IntentBackHandler backHandler;
    private final IntentPhotosHandler photosHandler;

    @Autowired
    public IntentService(
        IntentHelpHandler helpHandler,
        IntentGreetingsHandler greetingsHandler,
        IntentSuggestionsHandler suggestionsHandler,
        IntentDiscoverHandler discoverHandler,
        IntentWhatsnewHandler whatsnewHandler,
        IntentSeemoreHandler seemoreHandler,
        IntentFindThingsToDoHandler findThingsToDoHandler,
        IntentFindRestaurantsHandler findRestaurantsHandler,
        IntentSetSubcategoryHandler setSubcategoryHandler,
        IntentCancelHandler cancelHandler,
        IntentSeeItem seeItem,
        IntentGetasummaryHandler getasummaryHandler,
        IntentBackHandler backHandler,
        IntentPhotosHandler photosHandler
    ) {
        this.helpHandler = helpHandler;
        this.greetingsHandler = greetingsHandler;
        this.suggestionsHandler = suggestionsHandler;
        this.discoverHandler = discoverHandler;
        this.whatsnewHandler = whatsnewHandler;
        this.seemoreHandler = seemoreHandler;
        this.findThingsToDoHandler = findThingsToDoHandler;
        this.findRestaurantsHandler = findRestaurantsHandler;
        this.setSubcategoryHandler = setSubcategoryHandler;
        this.cancelHandler = cancelHandler;
        this.seeItem = seeItem;
        this.getasummaryHandler = getasummaryHandler;
        this.backHandler = backHandler;
        this.photosHandler = photosHandler;
    }

    public void handleHelp(Session session) {
        helpHandler.handle(session);
    }

    public void handleGreetings(Session session) throws MessengerApiException, MessengerIOException {
        greetingsHandler.handle(session);
    }

    public void handleSuggestions(Session session) throws MessengerApiException, MessengerIOException {
        suggestionsHandler.handle(session);
    }

    public void handleDiscover(Session session) throws MessengerApiException, MessengerIOException {
        discoverHandler.handle(session);
    }

    public void handleWhatsnew(Session session) {
        whatsnewHandler.handle(session);
    }

    public void handleFindThingsToDo(Session session) throws MessengerApiException, MessengerIOException { findThingsToDoHandler.handle(session); }

    public void handleFindRestaurants(
        Session session,
        HashMap<String, JsonElement> nluParameters
    ) throws MessengerApiException, MessengerIOException, IOException, InterruptedException {
        findRestaurantsHandler.handle(session, nluParameters);
    }

    public void handleFindRestaurants(
        Session session
    ) throws MessengerApiException, MessengerIOException, IOException, InterruptedException {
        findRestaurantsHandler.handle(session);
    }

    public void handleSeemore(
        Session session
    ) throws MessengerApiException, MessengerIOException, IOException, InterruptedException {
        seemoreHandler.handle(session);
    }

    public void handleBack(
        Session session
    ) throws MessengerApiException, MessengerIOException, IOException, InterruptedException {
        backHandler.handle(session);
    }

    public void handleSetSubcategory(
        Session session,
        String subcategoryId
    ) throws MessengerApiException, MessengerIOException, IOException, InterruptedException {
        setSubcategoryHandler.handle(session, subcategoryId);
    }

    public void handleCancel(
        Session session
    ) {
        cancelHandler.handle(session);
    }

    public void handleSeeItem(
        Session session
    ) throws MessengerApiException, MessengerIOException, IOException, InterruptedException {
        seeItem.handle(session);
    }

    public void handleGetasummary(
        Session session
    ) throws MessengerApiException, MessengerIOException {
        getasummaryHandler.handle(session);
    }

    public void handlePhotos(
        Session session
    ) throws MessengerApiException, MessengerIOException {
        photosHandler.handle(session);
    }
}
