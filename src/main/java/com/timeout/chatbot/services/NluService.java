package com.timeout.chatbot.services;

import ai.api.AIServiceException;
import ai.api.model.Result;
import com.google.gson.JsonElement;
import com.timeout.chatbot.domain.nlu.NluException;
import com.timeout.chatbot.domain.nlu.intent.NluIntentType;
import com.timeout.chatbot.domain.nlu.NluResult;
import com.timeout.chatbot.session.SessionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;

@Component
public class NluService {

    private final ApiAiService apiAiService;

    @Autowired
    public NluService(
        ApiAiService apiAiService
    ) {
        this.apiAiService = apiAiService;
    }

    public NluResult processText(
        String text
    ) throws NluException {

        final Result apiaiResult;
        try {
            apiaiResult = apiAiService.processText(text);
        } catch (AIServiceException e) {
            e.printStackTrace();
            throw new NluException();
        }

        final String apiaiAction = apiaiResult.getAction();
        final HashMap<String, JsonElement> apiaiParameters = apiaiResult.getParameters();
        SessionUtils.printConsole(apiaiAction, apiaiParameters);

        return
            new NluResult(
                NluIntentType.fromApiaiAction(apiaiAction),
                apiaiParameters
            );
    }
}
