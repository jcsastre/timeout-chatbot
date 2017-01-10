package com.timeout.chatbot.block;

import com.github.messenger4j.send.QuickReply;
import com.timeout.chatbot.messenger4j.send.MessengerSendClientWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class GeolocationAskBlock {

    private final MessengerSendClientWrapper messengerSendClientWrapper;

    @Autowired
    public GeolocationAskBlock(
        MessengerSendClientWrapper messengerSendClientWrapper
    ) {
        this.messengerSendClientWrapper = messengerSendClientWrapper;
    }

    public void send(
        String userId
    ) {
        messengerSendClientWrapper.sendTextMessage(
            userId,
            "I need your location please",
            QuickReply.newListBuilder()
                .addLocationQuickReply().toList()
                .build()
        );
    }
}
