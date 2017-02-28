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

        final NluResult nluResult = processInternal(text);

        if (nluResult != null) {

            return nluResult;
        } else {

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

    private NluResult processInternal(
        String text
    ) {
        if (text.equalsIgnoreCase("get started")) {
            return
                new NluResult(
                    NluIntentType.GET_STARTED
                );
        } else if (text.equalsIgnoreCase("Forget me")) {
            return
                new NluResult(
                    NluIntentType.FORGET_ME
                );
        } else if (text.equalsIgnoreCase("Things to do")) {
            return
                new NluResult(
                    NluIntentType.FIND_THINGSTODO
                );
        } else if (text.equalsIgnoreCase("Restaurants")) {
            return
                new NluResult(
                    NluIntentType.FIND_RESTAURANTS
                );
        } else if (text.equalsIgnoreCase("Hotels")) {
            return
                new NluResult(
                    NluIntentType.FIND_HOTELS
                );
        } else if (text.equalsIgnoreCase("Bars and pubs")) {
            return
                new NluResult(
                    NluIntentType.FIND_BARSANDPUBS
                );
        } else if (text.equalsIgnoreCase("Art")) {
            return
                new NluResult(
                    NluIntentType.FIND_ART
                );
        } else if (text.equalsIgnoreCase("Theatre")) {
            return
                new NluResult(
                    NluIntentType.FIND_THEATRE
                );
        } else if (text.equalsIgnoreCase("Music")) {
            return
                new NluResult(
                    NluIntentType.FIND_MUSIC
                );
        } else if (text.equalsIgnoreCase("Nightlife")) {
            return
                new NluResult(
                    NluIntentType.FIND_NIGHTLIFE
                );
        } else if (text.equalsIgnoreCase("Film")) {
            return
                new NluResult(
                    NluIntentType.FINDS_FILMS
                );
        }

        return null;
    }
}
