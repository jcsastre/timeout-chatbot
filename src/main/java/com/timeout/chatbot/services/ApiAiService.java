package com.timeout.chatbot.services;

import ai.api.AIDataService;
import ai.api.AIServiceException;
import ai.api.model.AIRequest;
import ai.api.model.AIResponse;
import ai.api.model.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ApiAiService {

    private final AIDataService aiDataService;

    @Autowired
    public ApiAiService(
        AIDataService aiDataService
    ) {
        this.aiDataService = aiDataService;
    }

    public Result processText(String text) throws AIServiceException {
        AIRequest aiRequest = new AIRequest(text);
        AIResponse aiResponse = this.aiDataService.request(aiRequest);

        if (aiResponse.getStatus().getCode() == 200) {
            return aiResponse.getResult();

//                String response = aiResponse.getResult().getParameters().toApiaiAction();
//                response = aiResponse.getResult().toApiaiAction();
//                try {
//                    messengerSendClientWrapper.sendTextMessage(recipientId, "Eco: " + response);
//                    HashMap<String, JsonElement> parameters = aiResponse.getResult().getParameters();
//                    for (Map.Entry<String, JsonElement> entry : parameters.entrySet()) {
//                        messengerSendClientWrapper.sendTextMessage(recipientId, entry.getKey() + "/" + entry.getValue());
//                    }
//                } catch (MessengerApiException | MessengerIOException e) {
//                    e.printStackTrace();
//                }
        } else {
            System.err.println(aiResponse.getStatus().getErrorDetails());
            return null;
        }
    }
}
