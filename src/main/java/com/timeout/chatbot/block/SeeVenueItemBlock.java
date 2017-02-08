package com.timeout.chatbot.block;

import com.github.messenger4j.exceptions.MessengerApiException;
import com.github.messenger4j.exceptions.MessengerIOException;
import com.github.messenger4j.send.MessengerSendClient;
import com.github.messenger4j.send.NotificationType;
import com.github.messenger4j.send.Recipient;
import com.github.messenger4j.send.templates.GenericTemplate;
import com.timeout.chatbot.block.quickreply.QuickReplyBuilderHelper;
import com.timeout.chatbot.block.template.generic.element.GenericTemplateElementVenueHelper;
import com.timeout.chatbot.graffitti.response.venue.GraffittiVenueResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class SeeVenueItemBlock {

    private final MessengerSendClient messengerSendClient;
    private final GenericTemplateElementVenueHelper genericTemplateElementVenueHelper;
    private final QuickReplyBuilderHelper quickReplyBuilderHelper;

    @Autowired
    public SeeVenueItemBlock(
        MessengerSendClient messengerSendClient,
        GenericTemplateElementVenueHelper genericTemplateElementVenueHelper,
        QuickReplyBuilderHelper quickReplyBuilderHelper
    ) {
        this.messengerSendClient = messengerSendClient;
        this.genericTemplateElementVenueHelper = genericTemplateElementVenueHelper;
        this.quickReplyBuilderHelper = quickReplyBuilderHelper;
    }

    public void send(
        String userId,
        GraffittiVenueResponse graffittiVenueResponse
    ) throws MessengerApiException, MessengerIOException {

        final GenericTemplate.Builder builder = GenericTemplate.newBuilder();
        final GenericTemplate.Element.ListBuilder listBuilder = builder.addElements();

        genericTemplateElementVenueHelper.addSingleElementInList(listBuilder, graffittiVenueResponse);

        messengerSendClient.sendTemplate(
            Recipient.newBuilder().recipientId(userId).build(),
            NotificationType.REGULAR,
            builder.build(),
            quickReplyBuilderHelper.buildForSeeVenueItem(graffittiVenueResponse)
        );
    }
}
