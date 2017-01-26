package com.timeout.chatbot.handler.intent;

import com.timeout.chatbot.session.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class IntentService {

    private final IntentHelpHandler helpHandler;
    private final IntentGreetingsHandler greetingsHandler;
    private final IntentSuggestionsHandler suggestionsHandler;
    private final IntentDiscoverHandler discoverHandler;
    private final IntentWhatsnewHandler whatsnewHandler;
    private final IntentFindThingsToDoHandler findThingsToDoHandler;

    @Autowired
    public IntentService(IntentHelpHandler helpHandler, IntentGreetingsHandler greetingsHandler, IntentSuggestionsHandler suggestionsHandler, IntentDiscoverHandler discoverHandler, IntentWhatsnewHandler whatsnewHandler, IntentFindThingsToDoHandler findThingsToDoHandler) {
        this.helpHandler = helpHandler;
        this.greetingsHandler = greetingsHandler;
        this.suggestionsHandler = suggestionsHandler;
        this.discoverHandler = discoverHandler;
        this.whatsnewHandler = whatsnewHandler;
        this.findThingsToDoHandler = findThingsToDoHandler;
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
}
