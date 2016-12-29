package com.timeout.chatbot.services;

import ai.api.AIDataService;
import ai.api.AIServiceException;
import ai.api.model.AIRequest;
import ai.api.model.AIResponse;
import ai.api.model.Result;
import com.github.messenger4j.send.MessengerSendClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ApiAiService {

    private final AIDataService aiDataService;
    private final MessengerSendClient messengerSendClient;

    @Autowired
    public ApiAiService(
        AIDataService aiDataService,
        MessengerSendClient messengerSendClient
    ) {
        this.aiDataService = aiDataService;
        this.messengerSendClient = messengerSendClient;
    }

    public Result processText(String text) throws AIServiceException {
        AIRequest aiRequest = new AIRequest(text);
        AIResponse aiResponse = this.aiDataService.request(aiRequest);

        if (aiResponse.getStatus().getCode() == 200) {
            return aiResponse.getResult();

//                String response = aiResponse.getResult().getParameters().toString();
//                response = aiResponse.getResult().toString();
//                try {
//                    messengerSendClient.sendTextMessage(recipientId, "Eco: " + response);
//                    HashMap<String, JsonElement> parameters = aiResponse.getResult().getParameters();
//                    for (Map.Entry<String, JsonElement> entry : parameters.entrySet()) {
//                        messengerSendClient.sendTextMessage(recipientId, entry.getKey() + "/" + entry.getValue());
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
