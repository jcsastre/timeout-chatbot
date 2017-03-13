package com.timeout.chatbot.block;

import com.github.messenger4j.exceptions.MessengerApiException;
import com.github.messenger4j.exceptions.MessengerIOException;
import com.github.messenger4j.send.MessengerSendClient;
import com.github.messenger4j.send.NotificationType;
import com.github.messenger4j.send.Recipient;
import com.github.messenger4j.send.templates.GenericTemplate;
import com.timeout.chatbot.block.quickreply.QuickReplyBuilderForCurrentSessionState;
import com.timeout.chatbot.block.template.generic.element.GenericTemplateElementEventHelper;
import com.timeout.chatbot.block.template.generic.element.GenericTemplateElementFilmHelper;
import com.timeout.chatbot.block.template.generic.element.GenericTemplateElementHelper;
import com.timeout.chatbot.block.template.generic.element.GenericTemplateWithSingleElementVenueBuilder;
import com.timeout.chatbot.graffitti.response.search.page.PageItem;
import com.timeout.chatbot.graffitti.response.search.page.GraffittiSearchResponse;
import com.timeout.chatbot.graffitti.urlbuilder.SearchUrlBuilder;
import com.timeout.chatbot.session.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.net.URI;
import java.util.List;

@Component
public class MostLovedBlock {

    private final RestTemplate restTemplate;
    private final SearchUrlBuilder searchUrlBuilder;
    private final GenericTemplateElementHelper genericTemplateElementHelper;
    private final GenericTemplateWithSingleElementVenueBuilder genericTemplateWithSingleElementVenueBuilder;
    private final GenericTemplateElementFilmHelper genericTemplateElementFilmHelper;
    private final GenericTemplateElementEventHelper genericTemplateElementEventHelper;
    private final MessengerSendClient messengerSendClient;
    private final QuickReplyBuilderForCurrentSessionState quickReplyBuilderForCurrentSessionState;

    @Autowired
    public MostLovedBlock(
        RestTemplate restTemplate,
        SearchUrlBuilder searchUrlBuilder,
        GenericTemplateElementHelper genericTemplateElementHelper, GenericTemplateWithSingleElementVenueBuilder genericTemplateWithSingleElementVenueBuilder,
        GenericTemplateElementFilmHelper genericTemplateElementFilmHelper,
        GenericTemplateElementEventHelper genericTemplateElementEventHelper, MessengerSendClient messengerSendClient,
        QuickReplyBuilderForCurrentSessionState quickReplyBuilderForCurrentSessionState
    ) {
        this.restTemplate = restTemplate;
        this.searchUrlBuilder = searchUrlBuilder;
        this.genericTemplateElementHelper = genericTemplateElementHelper;
        this.genericTemplateWithSingleElementVenueBuilder = genericTemplateWithSingleElementVenueBuilder;
        this.genericTemplateElementFilmHelper = genericTemplateElementFilmHelper;
        this.genericTemplateElementEventHelper = genericTemplateElementEventHelper;
        this.messengerSendClient = messengerSendClient;
        this.quickReplyBuilderForCurrentSessionState = quickReplyBuilderForCurrentSessionState;
    }

    public void send(
        Session session
    ) throws MessengerApiException, MessengerIOException, IOException, InterruptedException {

        messengerSendClient.sendTemplate(
            Recipient.newBuilder().recipientId(session.user.messengerId).build(),
            NotificationType.REGULAR,
            buildGenericTemplate(),
            quickReplyBuilderForCurrentSessionState.build(session)
        );
    }

    private GenericTemplate buildGenericTemplate() throws IOException, InterruptedException {

        final GenericTemplate.Builder builder = GenericTemplate.newBuilder();
        final GenericTemplate.Element.ListBuilder listBuilder = builder.addElements();

        addElements(listBuilder);

        return builder.build();
    }

    private void addElements(
        GenericTemplate.Element.ListBuilder listBuilder
    ) throws IOException, InterruptedException {
        final URI uri = searchUrlBuilder.buildForMostLovedBlock().toUri();
        System.out.println(uri);

        final GraffittiSearchResponse graffittiSearchResponse =
            restTemplate.getForObject(
                uri,
                GraffittiSearchResponse.class
            );

        final List<PageItem> pageItems = graffittiSearchResponse.getPageItems();
        for (PageItem pageItem : pageItems) {
            genericTemplateElementHelper.addNotSingleElementInList(listBuilder, pageItem);
//            final GraffittiType graffittiType = GraffittiType.fromString(pageItem.getType());
//            switch (graffittiType) {
//
//                case VENUE:
//                    genericTemplateElementVenueHelper.addNotSingleElementInList(listBuilder, pageItem);
//                    break;
//
//                case EVENT:
//                    genericTemplateElementEventHelper.addNotSingleElementInList(listBuilder, pageItem);
//                    break;
//
//                case FILM:
//                    genericTemplateElementFilmHelper.addElement(listBuilder, pageItem);
//                    break;
//
//                case PAGE:
//                    //TODO
//                    break;
//            }
        }
    }
}
