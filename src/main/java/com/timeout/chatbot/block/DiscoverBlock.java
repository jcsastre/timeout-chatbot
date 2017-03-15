package com.timeout.chatbot.block;

import com.github.messenger4j.exceptions.MessengerApiException;
import com.github.messenger4j.exceptions.MessengerIOException;
import com.github.messenger4j.send.MessengerSendClient;
import com.github.messenger4j.send.buttons.Button;
import com.github.messenger4j.send.templates.GenericTemplate;
import com.timeout.chatbot.block.quickreply.QuickReplyBuilderForCurrentSessionState;
import com.timeout.chatbot.domain.entities.Category;
import com.timeout.chatbot.domain.payload.PostbackPayload;
import com.timeout.chatbot.graffitti.response.search.page.GraffittiSearchResponse;
import com.timeout.chatbot.graffitti.response.search.page.PageItem;
import com.timeout.chatbot.graffitti.urlbuilder.SearchUrlBuilder;
import com.timeout.chatbot.messenger4j.SenderActionsHelper;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Component
public class DiscoverBlock {

    private final SearchUrlBuilder searchUrlBuilder;
    private final MessengerSendClient msc;
    private final RestTemplate restTemplate;
    private final QuickReplyBuilderForCurrentSessionState quickReplyBuilderForCurrentSessionState;
    private final SenderActionsHelper senderActionsHelper;

    @Autowired
    public DiscoverBlock(
        SearchUrlBuilder searchUrlBuilder,
        MessengerSendClient msc,
        RestTemplate restTemplate,
        QuickReplyBuilderForCurrentSessionState quickReplyBuilderForCurrentSessionState,
        SenderActionsHelper senderActionsHelper
    ) {
        this.searchUrlBuilder = searchUrlBuilder;
        this.msc = msc;
        this.restTemplate = restTemplate;
        this.quickReplyBuilderForCurrentSessionState = quickReplyBuilderForCurrentSessionState;
        this.senderActionsHelper = senderActionsHelper;
    }

    public void send(
        String userMessengerId
    ) throws MessengerApiException, MessengerIOException {

        senderActionsHelper.typingOnAndWait(userMessengerId, 1000);
        msc.sendTextMessage(
            userMessengerId,
            "What are you looking for?"
        );

        msc.sendTemplate(
            userMessengerId,
            buildGenericTemplate()
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
                    pageItem,
                    buildJsonPayloadAsString(category)
                );
            }
        }

        return listBuilder.done().build();
    }

    private void addPageItemToListBuilder(
        GenericTemplate.Element.ListBuilder listBuilder,
        String nodeName,
        PageItem pageItem,
        String payloadAsJsonString
    ) {
        String titleForButton = nodeName;
        if (
            !titleForButton.equalsIgnoreCase("Restaurants") &&
            !titleForButton.equalsIgnoreCase("Hotels")
        ) {
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
                        payloadAsJsonString
                    ).toList()
                    .build()
            )
            .toList();
    }

    private String buildJsonPayloadAsString(
        Category category
    ) {
        switch (category) {

            case RESTAURANTS:
                return
                    new JSONObject()
                        .put("type", PostbackPayload.discover_restaurants)
                        .toString();

            case HOTELS:
                return
                    new JSONObject()
                        .put("type", PostbackPayload.discover_hotels)
                        .toString();

            default:
                return null;
        }
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
