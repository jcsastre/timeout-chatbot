package com.timeout.chatbot.domain.nlu;

import com.google.gson.JsonElement;
import com.timeout.chatbot.domain.nlu.intent.NluIntentType;

import java.util.HashMap;

public class NluResult {

    private final NluIntentType nluIntentType;

    private final HashMap<String, JsonElement> parameters;

    public NluResult(
        NluIntentType nluIntentType,
        HashMap<String, JsonElement> parameters
    ) {
        this.nluIntentType = nluIntentType;
        this.parameters = parameters;
    }

    public NluResult(
        NluIntentType nluIntentType
    ) {
        this.nluIntentType = nluIntentType;
        this.parameters = null;
    }

    public NluIntentType getNluIntentType() {
        return nluIntentType;
    }

    public HashMap<String, JsonElement> getParameters() {
        return parameters;
    }
}
