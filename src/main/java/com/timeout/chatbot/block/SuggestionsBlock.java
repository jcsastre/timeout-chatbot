package com.timeout.chatbot.block;

import com.github.messenger4j.send.buttons.Button;
import com.github.messenger4j.send.templates.GenericTemplate;
import com.timeout.chatbot.configuration.TimeoutConfiguration;
import com.timeout.chatbot.domain.payload.PayloadType;
import com.timeout.chatbot.graffitti.response.search.page.PageItem;
import com.timeout.chatbot.graffitti.response.search.page.SearchResponse;
import com.timeout.chatbot.graffitti.response.tiles.TileItem;
import com.timeout.chatbot.graffitti.response.tiles.TilesResponse;
import com.timeout.chatbot.graffitti.urlbuilder.SearchUrlBuilder;
import com.timeout.chatbot.graffitti.urlbuilder.TilesDiscoverUrlBuilder;
import com.timeout.chatbot.messenger4j.send.MessengerSendClientWrapper;
import com.timeout.chatbot.services.GraffittiService;
import com.timeout.chatbot.services.NluService;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.List;

@Component
public class SuggestionsBlock {

    private final MessengerSendClientWrapper messengerSendClientWrapper;
    private final RestTemplate restTemplate;
    private final GraffittiService graffittiService;
    private final TimeoutConfiguration timeoutConfiguration;
    private final TilesDiscoverUrlBuilder tilesDiscoverUrlBuilder;
    private final NluService nluService;
    private final SearchUrlBuilder searchUrlBuilder;

    @Autowired
    public SuggestionsBlock(
        TimeoutConfiguration timeoutConfiguration,
        MessengerSendClientWrapper messengerSendClientWrapper,
        RestTemplate restTemplate,
        GraffittiService graffittiService,
        TilesDiscoverUrlBuilder tilesDiscoverUrlBuilder,
        NluService nluService,
        SearchUrlBuilder searchUrlBuilder) {
        this.timeoutConfiguration = timeoutConfiguration;
        this.messengerSendClientWrapper = messengerSendClientWrapper;
        this.restTemplate = restTemplate;
        this.graffittiService = graffittiService;
        this.tilesDiscoverUrlBuilder = tilesDiscoverUrlBuilder;
        this.nluService = nluService;
        this.searchUrlBuilder = searchUrlBuilder;
    }

    public void send(
        String userId
    ) {
        messengerSendClientWrapper.sendTemplate(
            userId,
            buildGenericTemplate()
        );
    }

    private GenericTemplate buildGenericTemplate() {

        final GenericTemplate.Element.ListBuilder listBuilder = GenericTemplate.newBuilder().addElements();

        //TODO: Element Discover (1 element)
        //TODO: Tiles discovery (n elements)
        //TODO: Most loved weekly (10-n-1 elements)
        addElementDiscover(listBuilder);
        addElementsTiles(listBuilder);

        return listBuilder.done().build();

//        return
//            GenericTemplate.newBuilder().addElements()
//                .addElement("Discover " + timeoutConfiguration.getCityName())
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
//                .addElement("In cinemas now")
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
//                .addElement("Bars & pubs nearby")
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
//                .addElement("RestaurantsManager nearby")
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
//                .addElement("Things to do this week")
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
//                .addElement("Book a theatre show")
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
//                .addElement("Art this week")
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

    public void addElementDiscover(
        GenericTemplate.Element.ListBuilder listBuilder
    ) {
        String imageUrlCity = "https://media.giphy.com/media/QGMiTNBw8hB72/giphy.gif";
        if (timeoutConfiguration.getSite().equalsIgnoreCase("es-barcelona")) {
            imageUrlCity = "http://2015.phpconference.es/wp-content/uploads/2015/05/barcelona1.jpg";
        }

            listBuilder
                .addElement("Discover " + timeoutConfiguration.getCityName())
                .imageUrl(imageUrlCity)
                .buttons(
                    Button.newListBuilder()
                        .addPostbackButton(
                            "Discover",
                            new JSONObject()
                                .put("type", "utterance")
                                .put("utterance", "Discover")
                                .toString()
                        ).toList()
                        .build()
                )
                .toList();
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
                        "Let's go",
                        new JSONObject()
                            .put("type", PayloadType.utterance)
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
                final SearchResponse searchResponse =
                    restTemplate.getForObject(
                        urlDecoded,
                        SearchResponse.class
                    );

                final List<PageItem> pageItems = searchResponse.getPageItems();
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
