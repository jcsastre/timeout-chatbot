package com.timeout.chatbot.block;

import com.github.messenger4j.exceptions.MessengerApiException;
import com.github.messenger4j.exceptions.MessengerIOException;
import com.github.messenger4j.send.MessengerSendClient;
import com.github.messenger4j.send.NotificationType;
import com.github.messenger4j.send.Recipient;
import com.timeout.chatbot.block.quickreply.QuickReplyBuilderHelper;
import com.timeout.chatbot.block.template.generic.element.GenericTemplateWithSingleElementVenueBuilder;
import com.timeout.chatbot.domain.Venue;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class SeeVenueItemBlock {

    private final MessengerSendClient messengerSendClient;
    private final GenericTemplateWithSingleElementVenueBuilder genericTemplateWithSingleElementVenueBuilder;
    private final QuickReplyBuilderHelper quickReplyBuilderHelper;

    @Autowired
    public SeeVenueItemBlock(
        MessengerSendClient messengerSendClient,
        GenericTemplateWithSingleElementVenueBuilder genericTemplateWithSingleElementVenueBuilder,
        QuickReplyBuilderHelper quickReplyBuilderHelper
    ) {
        this.messengerSendClient = messengerSendClient;
        this.genericTemplateWithSingleElementVenueBuilder = genericTemplateWithSingleElementVenueBuilder;
        this.quickReplyBuilderHelper = quickReplyBuilderHelper;
    }

    public void send(
        String userId,
        Venue venue
    ) throws MessengerApiException, MessengerIOException, IOException, InterruptedException {


        messengerSendClient.sendTemplate(
            Recipient.newBuilder().recipientId(userId).build(),
            NotificationType.REGULAR,
            genericTemplateWithSingleElementVenueBuilder.build(venue),
            quickReplyBuilderHelper.buildForSeeVenueItem(venue)
        );
    }
}
