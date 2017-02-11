package com.timeout.chatbot.block;

import com.github.messenger4j.exceptions.MessengerApiException;
import com.github.messenger4j.exceptions.MessengerIOException;
import com.github.messenger4j.send.MessengerSendClient;
import com.github.messenger4j.send.NotificationType;
import com.github.messenger4j.send.Recipient;
import com.github.messenger4j.send.templates.GenericTemplate;
import com.timeout.chatbot.block.quickreply.QuickReplyBuilderForCurrentSessionState;
import com.timeout.chatbot.block.template.generic.element.GenericTemplateElementHelper;
import com.timeout.chatbot.graffitti.response.search.page.GraffittiSearchResponse;
import com.timeout.chatbot.graffitti.response.search.page.PageItem;
import com.timeout.chatbot.graffitti.urlbuilder.SearchUrlBuilder;
import com.timeout.chatbot.session.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.util.List;

@Component
public class WhatsNewBlock {

    private final RestTemplate restTemplate;
    private final SearchUrlBuilder searchUrlBuilder;
    private final GenericTemplateElementHelper genericTemplateElementHelper;
    private final MessengerSendClient messengerSendClient;
    private final QuickReplyBuilderForCurrentSessionState quickReplyBuilderForCurrentSessionState;

    @Autowired
    public WhatsNewBlock(
        RestTemplate restTemplate,
        SearchUrlBuilder searchUrlBuilder,
        GenericTemplateElementHelper genericTemplateElementHelper,
        MessengerSendClient messengerSendClient,
        QuickReplyBuilderForCurrentSessionState quickReplyBuilderForCurrentSessionState
    ) {
        this.restTemplate = restTemplate;
        this.searchUrlBuilder = searchUrlBuilder;
        this.genericTemplateElementHelper = genericTemplateElementHelper;
        this.messengerSendClient = messengerSendClient;
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
        final URI uri = searchUrlBuilder.buildForWhatsNewBlock().toUri();
        System.out.println(uri);

        final GraffittiSearchResponse graffittiSearchResponse =
            restTemplate.getForObject(
                uri,
                GraffittiSearchResponse.class
            );

        final List<PageItem> pageItems = graffittiSearchResponse.getPageItems();
        for (PageItem pageItem : pageItems) {
            genericTemplateElementHelper.addNotSingleElementInList(listBuilder, pageItem);
        }
    }
}
