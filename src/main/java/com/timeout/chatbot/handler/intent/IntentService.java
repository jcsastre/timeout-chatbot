package com.timeout.chatbot.handler.intent;

import com.google.gson.JsonElement;
import com.timeout.chatbot.session.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

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
        IntentSetSubcategoryHandler setSubcategoryHandler
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
    }

    public void handleHelp(Session session) {
        helpHandler.handle(session);
    }

    public void handleGreetings(Session session) {
        greetingsHandler.handle(session);
    }

    public void handleSuggestions(Session session) {
        suggestionsHandler.handle(session);
    }

    public void handleDiscover(Session session) {
        discoverHandler.handle(session);
    }

    public void handleWhatsnew(Session session) {
        whatsnewHandler.handle(session);
    }

    public void handleFindThingsToDo(Session session) { findThingsToDoHandler.handle(session); }

    public void handleFindRestaurants(
        Session session,
        HashMap<String, JsonElement> nluParameters
    ) {
        findRestaurantsHandler.handle(session, nluParameters);
    }

    public void handleFindRestaurants(
        Session session
    ) {
        findRestaurantsHandler.handle(session);
    }

    public void  handleSeemore(
        Session session
    ) {
        seemoreHandler.handle(session);
    }

    public void  handleSetSubcategory(
        Session session,
        String subcategoryId
    ) {
        setSubcategoryHandler.handle(session, subcategoryId);
    }
}
