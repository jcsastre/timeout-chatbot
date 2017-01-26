package com.timeout.chatbot.domain.nlu;

import com.google.gson.JsonElement;

import java.util.HashMap;

public class NluResult {

    private final NluIntent nluIntent;

    private final HashMap<String, JsonElement> parameters;

    public NluResult(
        NluIntent nluIntent,
        HashMap<String, JsonElement> parameters
    ) {
        this.nluIntent = nluIntent;
        this.parameters = parameters;
    }

    public NluIntent getNluIntent() {
        return nluIntent;
    }

    public HashMap<String, JsonElement> getParameters() {
        return parameters;
    }
}
