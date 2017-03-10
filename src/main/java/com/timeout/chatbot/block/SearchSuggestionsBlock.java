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
import com.timeout.chatbot.graffitti.response.search.page.GraffittiSearchResponse;
import com.timeout.chatbot.graffitti.response.search.page.PageItem;
import com.timeout.chatbot.graffitti.response.tiles.TileItem;
import com.timeout.chatbot.graffitti.response.tiles.TilesResponse;
import com.timeout.chatbot.graffitti.urlbuilder.TilesDiscoverUrlBuilder;
import com.timeout.chatbot.session.Session;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.List;

@Component
public class SearchSuggestionsBlock {

    private final MessengerSendClient messengerSendClient;
    private final RestTemplate restTemplate;
    private final TimeoutConfiguration timeoutConfiguration;
    private final TilesDiscoverUrlBuilder tilesDiscoverUrlBuilder;
    private final QuickReplyBuilderForCurrentSessionState quickReplyBuilderForCurrentSessionState;

    @Autowired
    public SearchSuggestionsBlock(
        TimeoutConfiguration timeoutConfiguration,
        MessengerSendClient messengerSendClient,
        RestTemplate restTemplate,
        TilesDiscoverUrlBuilder tilesDiscoverUrlBuilder,
        QuickReplyBuilderForCurrentSessionState quickReplyBuilderForCurrentSessionState) {
        this.timeoutConfiguration = timeoutConfiguration;
        this.messengerSendClient = messengerSendClient;
        this.restTemplate = restTemplate;
        this.tilesDiscoverUrlBuilder = tilesDiscoverUrlBuilder;
        this.quickReplyBuilderForCurrentSessionState = quickReplyBuilderForCurrentSessionState;
    }

    public void send(
        Session session
    ) throws MessengerApiException, MessengerIOException {

        messengerSendClient.sendTemplate(
            Recipient.newBuilder().recipientId(session.user.messengerId).build(),
            NotificationType.REGULAR,
            buildGenericTemplate(),
            quickReplyBuilderForCurrentSessionState.build(session)
        );
    }

    private GenericTemplate buildGenericTemplate() {

        final GenericTemplate.Element.ListBuilder listBuilder = GenericTemplate.newBuilder().addElements();

        addElementsTiles(listBuilder);

        return listBuilder.done().build();

//        return
//            GenericTemplate.newBuilder().addElements()
//                .addNotSingleElementInList("Discover " + timeoutConfiguration.getCityName())
//                .imageUrl(imageUrlCity)
//                .buttons(
//                    Button.newListBuilder()
//                        .addPostbackButton(
//                            "Discover",
//                            new JSONObject()
//                                .put("type", "utterance")
//                                .put("utterance", "Discover")
//                                .toString()
//                        ).toList()
//                        .buildButtonsList()
//                )
//                .toList()
//                .addNotSingleElementInList("In cinemas now")
//                    .imageUrl("https://media.timeout.com/images/103667839/image.jpg")
//                    .buttons(
//                        Button.newListBuilder()
//                            .addPostbackButton(
//                                "Let's go",
//                                new JSONObject()
//                                    .put("type", "utterance")
//                                    .put("utterance", "In cinemas now")
//                                    .toString()
//                            ).toList()
//                            .buildButtonsList()
//                    )
//                    .toList()
//                .addNotSingleElementInList("Bars & pubs nearby")
//                    .imageUrl("https://media.timeout.com/images/103466376/image.jpg")
//                    .buttons(
//                        Button.newListBuilder()
//                            .addPostbackButton(
//                                "Let's go",
//                                new JSONObject()
//                                    .put("type", "utterance")
//                                    .put("utterance", "Bars & pubs nearby")
//                                    .toString()
//                            ).toList()
//                            .buildButtonsList()
//                    )
//                    .toList()
//                .addNotSingleElementInList("RestaurantsManager nearby")
//                    .imageUrl("https://media.timeout.com/images/102173995/image.jpg")
//                    .buttons(
//                        Button.newListBuilder()
//                            .addPostbackButton(
//                                "Let's go",
//                                new JSONObject()
//                                    .put("type", "utterance")
//                                    .put("utterance", "RestaurantsManager nearby")
//                                    .toString()
//                            ).toList()
//                            .buildButtonsList()
//                    )
//                    .toList()
//                .addNotSingleElementInList("Things to do this week")
//                    .imageUrl("https://media.timeout.com/images/102872844/image.jpg")
//                    .buttons(
//                        Button.newListBuilder()
//                            .addPostbackButton(
//                                "Let's go",
//                                new JSONObject()
//                                    .put("type", "utterance")
//                                    .put("utterance", "Things to do this week")
//                                    .toString()
//                            ).toList()
//                            .buildButtonsList()
//                    )
//                    .toList()
//                .addNotSingleElementInList("Book a theatre show")
//                    .imageUrl("https://media.timeout.com/images/103646228/image.jpg")
//                    .buttons(
//                        Button.newListBuilder()
//                            .addPostbackButton(
//                                "Let's go",
//                                new JSONObject()
//                                    .put("type", "utterance")
//                                    .put("utterance", "Book a theatre show")
//                                    .toString()
//                            ).toList()
//                            .buildButtonsList()
//                    )
//                    .toList()
//                .addNotSingleElementInList("Art this week")
//                    .imageUrl("https://media.timeout.com/images/103659638/image.jpg")
//                    .buttons(
//                        Button.newListBuilder()
//                            .addPostbackButton(
//                                "Let's go",
//                                new JSONObject()
//                                    .put("type", "utterance")
//                                    .put("utterance", "Art this week")
//                                    .toString()
//                            ).toList()
//                            .buildButtonsList()
//                    )
//                    .toList()
//                .done()
//                .buildButtonsList();
    }

    public void addElementsTiles(
        GenericTemplate.Element.ListBuilder listBuilder
    ) {
        final TilesResponse tilesResponse =
            restTemplate.getForObject(
                tilesDiscoverUrlBuilder.build().toUri(),
                TilesResponse.class
            );

        final List<TileItem> tileItems = tilesResponse.getTileItems();
        for (TileItem tileItem : tileItems) {
            final String name = tileItem.getName();
            if (name != null) {
                addElementTile(listBuilder, tileItem);
            }
        }
    }

    public void addElementTile(
        GenericTemplate.Element.ListBuilder listBuilder,
        TileItem tileItem
    ) {

        listBuilder
            .addElement(tileItem.getName())
            .imageUrl(getImageUrlForTileItem(tileItem))
            .buttons(
                Button.newListBuilder()
                    .addPostbackButton(
                        "\ud83d\udeab" +" Let's go",
                        new JSONObject()
                            .put("type", PayloadType.temporaly_disabled)
//                            .put("type", PayloadType.utterance)
                            .put("utterance", tileItem.getName())
                            .toString()
                    ).toList()
                    .build()
            )
            .toList();
    }

    public String getImageUrlForTileItem(TileItem tileItem) {

        String urlAsString = tileItem.getUrl();
        if (urlAsString != null) {
            if (urlAsString.contains("latitude")) {
                urlAsString = urlAsString.replace("latitude=_", "latitude="+timeoutConfiguration.getLatitude());
                urlAsString = urlAsString.replace("longitude=_", "longitude="+timeoutConfiguration.getLongitude());
            }

            String urlDecoded = null;
            try {
                urlDecoded = URLDecoder.decode(urlAsString, "UTF-8");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }

            if (urlDecoded != null) {
                final GraffittiSearchResponse graffittiSearchResponse =
                    restTemplate.getForObject(
                        urlDecoded,
                        GraffittiSearchResponse.class
                    );

                final List<PageItem> pageItems = graffittiSearchResponse.getPageItems();
                if (pageItems != null) {
                    for (PageItem pageItem : pageItems) {
                        final String pageItemImageUrl = pageItem.getImage_url();
                        if (pageItemImageUrl != null) {
                            return pageItemImageUrl;
                        }
                    }
                }
            }
        }

        return timeoutConfiguration.getImageUrlPlacholder();
    }
}
