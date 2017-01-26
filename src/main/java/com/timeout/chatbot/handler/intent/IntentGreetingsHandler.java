package com.timeout.chatbot.handler.intent;

import com.timeout.chatbot.messenger4j.send.MessengerSendClientWrapper;
import com.timeout.chatbot.services.BlockService;
import com.timeout.chatbot.session.Session;
import com.timeout.chatbot.session.context.SessionState;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class IntentGreetingsHandler {

    private final RestTemplate restTemplate;
    private final BlockService blockService;
    private final MessengerSendClientWrapper messengerSendClientWrapper;

    @Autowired
    public IntentGreetingsHandler(
        RestTemplate restTemplate,
        BlockService blockService,
        MessengerSendClientWrapper messengerSendClientWrapper
    ) {
        this.restTemplate = restTemplate;
        this.blockService = blockService;
        this.messengerSendClientWrapper = messengerSendClientWrapper;
    }

    public void handle(
        Session session
    ) {
        switch (session.getSessionState()) {

            case UNDEFINED:
                blockService.sendWelcomeBackBlock(session.getUser());
                session.setSessionState(SessionState.WELCOMED);
                break;

            case WELCOMED:
                messengerSendClientWrapper.sendTextMessage(
                    session.getUser().getMessengerId(),
                    "Hi!"
                );
                break;

            case LOOKING:
                messengerSendClientWrapper.sendTextMessage(
                    session.getUser().getMessengerId(),
                    "Hi!"
                );
                break;

            case BOOKING:
                //TODO: greetings during booking
                break;

            default:
                break;
        }
    }
}
