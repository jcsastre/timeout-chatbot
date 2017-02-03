package com.timeout.chatbot.block;

import com.github.messenger4j.exceptions.MessengerApiException;
import com.github.messenger4j.exceptions.MessengerIOException;
import com.github.messenger4j.send.MessengerSendClient;
import com.github.messenger4j.send.NotificationType;
import com.github.messenger4j.send.QuickReply;
import com.github.messenger4j.send.Recipient;
import com.github.messenger4j.send.buttons.Button;
import com.github.messenger4j.send.templates.GenericTemplate;
import com.timeout.chatbot.block.types.BlockTypeFilmHelper;
import com.timeout.chatbot.block.types.BlockTypeVenueHelper;
import com.timeout.chatbot.block.types.EventListButtonsBuilder;
import com.timeout.chatbot.domain.payload.PayloadType;
import com.timeout.chatbot.graffitti.domain.GraffittiType;
import com.timeout.chatbot.graffitti.response.films.GraffitiFilmResponse;
import com.timeout.chatbot.graffitti.response.search.page.PageItem;
import com.timeout.chatbot.graffitti.response.search.page.SearchResponse;
import com.timeout.chatbot.graffitti.urlbuilder.SearchUrlBuilder;
import com.timeout.chatbot.messenger4j.send.MessengerSendClientWrapper;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Component
public class MostLovedBlock {

    private final MessengerSendClientWrapper messengerSendClientWrapper;
    private final RestTemplate restTemplate;
    private final SearchUrlBuilder searchUrlBuilder;
    private final BlockTypeVenueHelper blockTypeVenueHelper;
    private final EventListButtonsBuilder eventListButtonsBuilder;
    private final BlockTypeFilmHelper blockTypeFilmHelper;
    private final MessengerSendClient messengerSendClient;

    @Autowired
    public MostLovedBlock(
        MessengerSendClientWrapper messengerSendClientWrapper,
        RestTemplate restTemplate,
        SearchUrlBuilder searchUrlBuilder,
        BlockTypeVenueHelper blockTypeVenueHelper,
        EventListButtonsBuilder eventListButtonsBuilder,
        BlockTypeFilmHelper blockTypeFilmHelper,
        MessengerSendClient messengerSendClient
    ) {
        this.messengerSendClientWrapper = messengerSendClientWrapper;
        this.restTemplate = restTemplate;
        this.searchUrlBuilder = searchUrlBuilder;
        this.blockTypeVenueHelper = blockTypeVenueHelper;
        this.eventListButtonsBuilder = eventListButtonsBuilder;
        this.blockTypeFilmHelper = blockTypeFilmHelper;
        this.messengerSendClient = messengerSendClient;
    }

    public void send(
        String userId
    ) throws MessengerApiException, MessengerIOException {
        messengerSendClient.sendTemplate(
            Recipient.newBuilder().recipientId(userId).build(),
            NotificationType.REGULAR,
            buildGenericTemplate(),
            buildQuickReplies()
        );
//        messengerSendClientWrapper.sendTemplate(
//            userId,
//            buildGenericTemplate()
//        );
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
            addElement(listBuilder, pageItem);
        }
    }

    private void addElement(
        GenericTemplate.Element.ListBuilder listBuilder,
        PageItem pageItem
    ) {
        final GenericTemplate.Element.Builder builder = listBuilder.addElement(pageItem.getName());
//        final GenericTemplate.Element.Builder builder = listBuilder.addElement("title");

//        builder.itemUrl(pageItem.getToWebsite());

        final String subtitle = buildSubtitle(pageItem);
        if (subtitle != null && !subtitle.isEmpty()) {
            builder.subtitle(subtitle);
        }

        final String imageUrl = buildImageUrl(pageItem);
        if (imageUrl != null) {
            builder.imageUrl(imageUrl);
        }

        final List<Button> buttons = buildButtons(pageItem);
        if (buttons != null) {
            builder.buttons(buttons);
        }

        builder.toList().done();
    }

    private String buildSubtitle(
        PageItem pageItem
    ) {
        final GraffittiType type = GraffittiType.fromString(pageItem.getType());

        if (type == GraffittiType.VENUE) {
            return blockTypeVenueHelper.buildSubtitleForGenericTemplateElement(pageItem);
        } else if (type == GraffittiType.EVENT) {
            //TODO
            return null;
        } else if (type == GraffittiType.FILM) {
            return blockTypeFilmHelper.buildSubtitleForGenericTemplateElement(pageItem);
        } else {
            //TODO
            return null;
        }
    }

    private String buildImageUrl(
        PageItem pageItem
    ) {
        final GraffittiType type = GraffittiType.fromString(pageItem.getType());

        if (type == GraffittiType.VENUE) {
            return blockTypeFilmHelper.buildImageUrlForGenericTemplateElement(pageItem);
        } else if (type == GraffittiType.EVENT) {
            return blockTypeFilmHelper.buildImageUrlForGenericTemplateElement(pageItem);
        } else if (type == GraffittiType.FILM) {
            return blockTypeFilmHelper.buildImageUrlForGenericTemplateElement(pageItem);
        } else {
            return null;
        }
    }

    private List<Button> buildButtons(
        PageItem pageItem
    ) {
        final GraffittiType type = GraffittiType.fromString(pageItem.getType());

        if (type == GraffittiType.VENUE) {
            return blockTypeVenueHelper.buildButtonsList(pageItem);
        } else if (type == GraffittiType.EVENT) {
            return eventListButtonsBuilder.build(pageItem);
        } else if (type == GraffittiType.FILM) {
            final String url = pageItem.getUrl();
            final GraffitiFilmResponse graffitiFilmResponse =
                restTemplate.getForObject(
                    url,
                    GraffitiFilmResponse.class
                );
            return blockTypeFilmHelper.buildButtonsList(graffitiFilmResponse);
        } else {
            return null;
        }
    }

    private List<QuickReply> buildQuickReplies(
    ) {
        final QuickReply.ListBuilder builder = QuickReply.newListBuilder();

        builder.addTextQuickReply(
            "See more",
            new JSONObject()
                .put("type", PayloadType.see_more)
                .toString()
        ).toList();

        builder.addLocationQuickReply().toList();

        return builder.build();
    }
}
