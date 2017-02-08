package com.timeout.chatbot.block;

import com.github.messenger4j.exceptions.MessengerApiException;
import com.github.messenger4j.exceptions.MessengerIOException;
import com.github.messenger4j.send.MessengerSendClient;
import com.github.messenger4j.send.NotificationType;
import com.github.messenger4j.send.Recipient;
import com.github.messenger4j.send.templates.GenericTemplate;
import com.timeout.chatbot.block.quickreply.QuickReplyBuilderForCurrentSessionState;
import com.timeout.chatbot.block.template.generic.element.GenericTemplateElementFilmHelper;
import com.timeout.chatbot.block.template.generic.element.GenericTemplateElementVenueHelper;
import com.timeout.chatbot.graffitti.domain.GraffittiType;
import com.timeout.chatbot.graffitti.response.search.page.PageItem;
import com.timeout.chatbot.graffitti.response.search.page.SearchResponse;
import com.timeout.chatbot.graffitti.urlbuilder.SearchUrlBuilder;
import com.timeout.chatbot.services.GraffittiService;
import com.timeout.chatbot.session.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Component
public class WhatsNewBlock {

    private final RestTemplate restTemplate;
    private final SearchUrlBuilder searchUrlBuilder;
    private final GenericTemplateElementVenueHelper genericTemplateElementVenueHelper;
    private final GenericTemplateElementFilmHelper genericTemplateElementFilmHelper;
    private final MessengerSendClient messengerSendClient;
    private final GraffittiService graffittiService;
    private final QuickReplyBuilderForCurrentSessionState quickReplyBuilderForCurrentSessionState;

    @Autowired
    public WhatsNewBlock(
        RestTemplate restTemplate,
        SearchUrlBuilder searchUrlBuilder,
        GenericTemplateElementVenueHelper genericTemplateElementVenueHelper,
        GenericTemplateElementFilmHelper genericTemplateElementFilmHelper,
        MessengerSendClient messengerSendClient,
        GraffittiService graffittiService,
        QuickReplyBuilderForCurrentSessionState quickReplyBuilderForCurrentSessionState
    ) {
        this.restTemplate = restTemplate;
        this.searchUrlBuilder = searchUrlBuilder;
        this.genericTemplateElementVenueHelper = genericTemplateElementVenueHelper;
        this.genericTemplateElementFilmHelper = genericTemplateElementFilmHelper;
        this.messengerSendClient = messengerSendClient;
        this.graffittiService = graffittiService;
        this.quickReplyBuilderForCurrentSessionState = quickReplyBuilderForCurrentSessionState;
    }

    public void send(
        Session session
    ) throws MessengerApiException, MessengerIOException {

        messengerSendClient.sendTemplate(
            Recipient.newBuilder().recipientId(session.getUser().getMessengerId()).build(),
            NotificationType.REGULAR,
            buildGenericTemplate(),
            quickReplyBuilderForCurrentSessionState.build(session)
        );
    }

    private GenericTemplate buildGenericTemplate() {

        final GenericTemplate.Builder builder = GenericTemplate.newBuilder();
        final GenericTemplate.Element.ListBuilder listBuilder = builder.addElements();

        addElements(listBuilder);

        return builder.build();
    }

    private void addElements(
        GenericTemplate.Element.ListBuilder listBuilder
    ) {
        final SearchResponse searchResponse =
            restTemplate.getForObject(
                searchUrlBuilder.buildForMostLovedBlock().toUri(),
                SearchResponse.class
            );

        final List<PageItem> pageItems = searchResponse.getPageItems();
        for (PageItem pageItem : pageItems) {
            final GraffittiType type = GraffittiType.fromString(pageItem.getType());
            if (type == GraffittiType.VENUE) {
                genericTemplateElementVenueHelper.addNotSingleElementInList(listBuilder, pageItem);
            } else if (type == GraffittiType.EVENT) {
                //TODO
            } else if (type == GraffittiType.FILM) {
                genericTemplateElementFilmHelper.addElement(listBuilder, pageItem);
            } else {
                //TODO
            }
        }
    }
}
