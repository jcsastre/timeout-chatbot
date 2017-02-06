package com.timeout.chatbot.block;

import com.github.messenger4j.exceptions.MessengerApiException;
import com.github.messenger4j.exceptions.MessengerIOException;
import com.github.messenger4j.send.MessengerSendClient;
import com.github.messenger4j.send.NotificationType;
import com.github.messenger4j.send.Recipient;
import com.github.messenger4j.send.templates.GenericTemplate;
import com.timeout.chatbot.block.quickreply.QuickReplyBuilderHelper;
import com.timeout.chatbot.block.template.generic.element.GenericTemplateElementVenueHelper;
import com.timeout.chatbot.graffitti.response.venues.GraffittiVenueResponse;
import com.timeout.chatbot.graffitti.urlbuilder.VenuesUrlBuilder;
import com.timeout.chatbot.session.Session;
import com.timeout.chatbot.session.SessionStateItemBag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class SeeVenueItemBlock {
    private final MessengerSendClient messengerSendClient;
    private final VenuesUrlBuilder venuesUrlBuilder;
    private final RestTemplate restTemplate;
    private final GenericTemplateElementVenueHelper genericTemplateElementVenueHelper;
    private final QuickReplyBuilderHelper quickReplyBuilderHelper;

    @Autowired
    public SeeVenueItemBlock(
        MessengerSendClient messengerSendClient,
        VenuesUrlBuilder venuesUrlBuilder,
        RestTemplate restTemplate,
        GenericTemplateElementVenueHelper genericTemplateElementVenueHelper,
        QuickReplyBuilderHelper quickReplyBuilderHelper
    ) {
        this.messengerSendClient = messengerSendClient;
        this.venuesUrlBuilder = venuesUrlBuilder;
        this.restTemplate = restTemplate;
        this.genericTemplateElementVenueHelper = genericTemplateElementVenueHelper;
        this.quickReplyBuilderHelper = quickReplyBuilderHelper;
    }

    public void send(
        Session session
    ) throws MessengerApiException, MessengerIOException {

        final SessionStateItemBag itemBag = session.getSessionStateItemBag();

        final GraffittiVenueResponse graffittiVenueResponse =
            restTemplate.getForObject(
                venuesUrlBuilder.build(itemBag.getItemId()).toUri(),
                GraffittiVenueResponse.class
            );

        final GenericTemplate.Builder builder = GenericTemplate.newBuilder();
        final GenericTemplate.Element.ListBuilder listBuilder = builder.addElements();

        genericTemplateElementVenueHelper.addElementLite(listBuilder, graffittiVenueResponse);

        messengerSendClient.sendTemplate(
            Recipient.newBuilder().recipientId(session.getUser().getMessengerId()).build(),
            NotificationType.REGULAR,
            builder.build(),
            quickReplyBuilderHelper.buildForSeeVenueItem()
        );
    }
}
