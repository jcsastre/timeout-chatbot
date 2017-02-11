package com.timeout.chatbot.block;

import com.github.messenger4j.exceptions.MessengerApiException;
import com.github.messenger4j.exceptions.MessengerIOException;
import com.github.messenger4j.send.MessengerSendClient;
import com.github.messenger4j.send.NotificationType;
import com.github.messenger4j.send.Recipient;
import com.github.messenger4j.send.buttons.Button;
import com.github.messenger4j.send.templates.GenericTemplate;
import com.timeout.chatbot.block.quickreply.QuickReplyBuilderForCurrentSessionState;
import com.timeout.chatbot.configuration.TimeoutConfiguration;
import com.timeout.chatbot.domain.payload.PayloadType;
import com.timeout.chatbot.graffitti.response.facets.v5.GraffittiFacetV5Node;
import com.timeout.chatbot.graffitti.response.search.page.GraffittiSearchResponse;
import com.timeout.chatbot.graffitti.response.search.page.PageItem;
import com.timeout.chatbot.graffitti.urlbuilder.SearchUrlBuilder;
import com.timeout.chatbot.graffitti.urlbuilder.TilesDiscoverUrlBuilder;
import com.timeout.chatbot.services.GraffittiService;
import com.timeout.chatbot.session.Session;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Component
public class DiscoverBlock {

    private final MessengerSendClient messengerSendClient;
    private final RestTemplate restTemplate;
    private final TimeoutConfiguration timeoutConfiguration;
    private final TilesDiscoverUrlBuilder tilesDiscoverUrlBuilder;
    private final QuickReplyBuilderForCurrentSessionState quickReplyBuilderForCurrentSessionState;
    private final GraffittiService graffittiService;
    private final SearchUrlBuilder searchUrlBuilder;

    @Autowired
    public DiscoverBlock(
        TimeoutConfiguration timeoutConfiguration,
        MessengerSendClient messengerSendClient,
        RestTemplate restTemplate,
        TilesDiscoverUrlBuilder tilesDiscoverUrlBuilder,
        QuickReplyBuilderForCurrentSessionState quickReplyBuilderForCurrentSessionState,
        GraffittiService graffittiService,
        SearchUrlBuilder searchUrlBuilder
    ) {
        this.timeoutConfiguration = timeoutConfiguration;
        this.messengerSendClient = messengerSendClient;
        this.restTemplate = restTemplate;
        this.tilesDiscoverUrlBuilder = tilesDiscoverUrlBuilder;
        this.quickReplyBuilderForCurrentSessionState = quickReplyBuilderForCurrentSessionState;
        this.graffittiService = graffittiService;
        this.searchUrlBuilder = searchUrlBuilder;
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

        final List<GraffittiFacetV5Node> children =
            graffittiService.getGraffittiFacetV5Response().getBody().getFacets().getWhat().getChildren();

        for (GraffittiFacetV5Node node : children) {

            System.out.println(node.getName());

            final PageItem pageItem = getPageItemWithImage(node);
            if (pageItem != null) {
                addPageItemToListBuilder(
                    listBuilder,
                    node.getName(),
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
        listBuilder
            .addElement(nodeName)
            .imageUrl(pageItem.getImage_url())
            .buttons(
                Button.newListBuilder()
                    .addPostbackButton(
                        nodeName.length() <= 20 ? nodeName : nodeName.substring(0, 20),
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
        GraffittiFacetV5Node node
    ) {
        final GraffittiSearchResponse graffittiSearchResponse =
            restTemplate.getForObject(
                searchUrlBuilder.buildForDiscoverBlock(node.getId()).toUri(),
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
