package com.timeout.chatbot.block;

import com.github.messenger4j.send.QuickReply;
import com.timeout.chatbot.messenger4j.send.MessengerSendClientWrapper;
import com.timeout.chatbot.services.GraffittiService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class RestaurantsSetCuisineBlock {
    private final MessengerSendClientWrapper messengerSendClientWrapper;
    private final GraffittiService graffittiService;

    @Autowired
    public RestaurantsSetCuisineBlock(
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
            "GraffittiFacetV4Where kind of cuisine are you looking for? \uD83D\uDE0B";

        final QuickReply.ListBuilder listBuilder = QuickReply.newListBuilder();

        //TODO:
    }
}
