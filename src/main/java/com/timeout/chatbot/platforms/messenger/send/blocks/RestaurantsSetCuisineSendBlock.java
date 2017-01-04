package com.timeout.chatbot.platforms.messenger.send.blocks;

import com.github.messenger4j.send.QuickReply;
import com.timeout.chatbot.messenger4j.send.MessengerSendClientWrapper;
import com.timeout.chatbot.services.GraffittiService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class RestaurantsSetCuisineSendBlock {
    private final MessengerSendClientWrapper messengerSendClientWrapper;
    private final GraffittiService graffittiService;

    @Autowired
    public RestaurantsSetCuisineSendBlock(
        MessengerSendClientWrapper messengerSendClientWrapper,
        GraffittiService graffittiService
    ) {
        this.messengerSendClientWrapper = messengerSendClientWrapper;
        this.graffittiService = graffittiService;
    }

    public void send(
        String recipientId
    ) {
        String msg =
            "What kind of cuisine are you looking for? \uD83D\uDE0B";

        final QuickReply.ListBuilder listBuilder = QuickReply.newListBuilder();

        //TODO:
    }
}
