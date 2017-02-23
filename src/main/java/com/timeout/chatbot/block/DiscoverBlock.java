package com.timeout.chatbot.block;

import com.github.messenger4j.exceptions.MessengerApiException;
import com.github.messenger4j.exceptions.MessengerIOException;
import com.github.messenger4j.send.MessengerSendClient;
import com.github.messenger4j.send.NotificationType;
import com.github.messenger4j.send.Recipient;
import com.github.messenger4j.send.buttons.Button;
import com.github.messenger4j.send.templates.GenericTemplate;
import com.timeout.chatbot.block.quickreply.QuickReplyBuilderForCurrentSessionState;
import com.timeout.chatbot.domain.entities.Category;
import com.timeout.chatbot.domain.payload.PayloadType;
import com.timeout.chatbot.graffitti.response.search.page.GraffittiSearchResponse;
import com.timeout.chatbot.graffitti.response.search.page.PageItem;
import com.timeout.chatbot.graffitti.urlbuilder.SearchUrlBuilder;
import com.timeout.chatbot.session.Session;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Component
public class DiscoverBlock {

    private final SearchUrlBuilder searchUrlBuilder;
    private final MessengerSendClient messengerSendClient;
    private final RestTemplate restTemplate;
    private final QuickReplyBuilderForCurrentSessionState quickReplyBuilderForCurrentSessionState;

    @Autowired
    public DiscoverBlock(
        SearchUrlBuilder searchUrlBuilder, MessengerSendClient messengerSendClient,
        RestTemplate restTemplate,
        QuickReplyBuilderForCurrentSessionState quickReplyBuilderForCurrentSessionState
    ) {
        this.searchUrlBuilder = searchUrlBuilder;
        this.messengerSendClient = messengerSendClient;
        this.restTemplate = restTemplate;
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

        final GenericTemplate.Element.ListBuilder listBuilder = GenericTemplate.newBuilder().addElements();

        for (Category category : Category.values()) {
            final PageItem pageItem = getPageItemWithImage(category);
            if (pageItem != null) {
                addPageItemToListBuilder(
                    listBuilder,
                    category.getNamePlural(),
                    pageItem
                );
            }

        }

        return listBuilder.done().build();
    }

    private void addPageItemToListBuilder(
        GenericTemplate.Element.ListBuilder listBuilder,
        String nodeName,
        PageItem pageItem
    ) {
        String titleForButton = nodeName;
        if (!titleForButton.equalsIgnoreCase("Restaurants")) {
            titleForButton = "\ud83d\udeab " + titleForButton;
        }
        if (titleForButton.length() > 20) {
            titleForButton = titleForButton.substring(0, 20);
        }
        listBuilder
            .addElement(nodeName)
            .imageUrl(pageItem.getImage_url())
            .buttons(
                Button.newListBuilder()
                    .addPostbackButton(
                        titleForButton,
                        new JSONObject()
                            .put("type", PayloadType.utterance)
                            .put("utterance", nodeName)
                            .toString()
                    ).toList()
                    .build()
            )
            .toList();
    }

    private PageItem getPageItemWithImage(
        Category category
    ) {
        final GraffittiSearchResponse graffittiSearchResponse =
            restTemplate.getForObject(
                searchUrlBuilder.buildForDiscoverBlock(category.getGraffittiId()).toUri(),
                GraffittiSearchResponse.class
            );

        final List<PageItem> pageItems = graffittiSearchResponse.getPageItems();
        for (PageItem pageItem : pageItems) {
            if (pageItem.getImage() != null) {
                return pageItem;
            }
        }

        return null;
    }
}
