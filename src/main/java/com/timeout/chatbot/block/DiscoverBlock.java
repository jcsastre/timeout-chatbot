package com.timeout.chatbot.block;

import com.github.messenger4j.send.buttons.Button;
import com.github.messenger4j.send.templates.GenericTemplate;
import com.timeout.chatbot.graffitti.domain.response.search.page.PageItem;
import com.timeout.chatbot.graffitti.domain.response.search.page.SearchResponse;
import com.timeout.chatbot.graffitti.domain.response.tiles.TileItem;
import com.timeout.chatbot.messenger4j.send.MessengerSendClientWrapper;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;

@Component
public class DiscoverBlock {

    private final MessengerSendClientWrapper messengerSendClientWrapper;
    private final RestTemplate restTemplate;

    @Autowired
    public DiscoverBlock(
        MessengerSendClientWrapper messengerSendClientWrapper,
        RestTemplate restTemplate) {
        this.messengerSendClientWrapper = messengerSendClientWrapper;
        this.restTemplate = restTemplate;
    }

    public void send(
        String userId
    ) {

        final GenericTemplate genericTemplate =
            GenericTemplate.newBuilder().addElements()
                .addElement("Things to do this week")
                    .imageUrl("https://media.timeout.com/images/102872844/image.jpg")
                    .buttons(
                        Button.newListBuilder()
                            .addPostbackButton(
                                "Discover",
                                new JSONObject()
                                    .put("type", "utterance")
                                    .put("utterance", "Things to do this week")
                                    .toString()
                            ).toList()
                            .build()

                    )
                    .toList()
                .addElement("Bars & pubs nearby")
                    .imageUrl("https://media.timeout.com/images/103466376/image.jpg")
                    .buttons(
                        Button.newListBuilder()
                            .addPostbackButton(
                                "Discover",
                                new JSONObject()
                                    .put("type", "utterance")
                                    .put("utterance", "Bars & pubs nearby")
                                    .toString()
                            ).toList()
                            .build()

                    )
                    .toList()
                .done()
                .build();

        messengerSendClientWrapper.sendTemplate(userId, genericTemplate);

        /////////////////

//        final GenericTemplate.Builder genericTemplateBuilder = GenericTemplate.newBuilder();
//        final GenericTemplate.Element.ListBuilder listBuilder = genericTemplateBuilder.addElements();
//
//        for (TileItem tileItem : tilesResponse.getTileItems()) {
//            listBuilder.addElement(tileItem.getName())
//                .imageUrl(getImageUrl(tileItem))
//                .buttons(
//                    Button.newListBuilder()
//                        .addPostbackButton(
//                            "Explore",
//                            new JSONObject()
//                                .put("type", "url")
//                                .put("restaurant_id", tileItem.getUrl())
//                                .toString()
//                        ).toList()
//                        .build()
//                )
//                .toList().done();
//        }
//
//        final GenericTemplate genericTemplate = genericTemplateBuilder.build();
    }

    public String getImageUrl(TileItem tileItem) {
        String urlImage = null;

        String url = null;
        try {
            url = URLDecoder.decode(tileItem.getUrl(), "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        System.out.println(url);

        final SearchResponse response =
            restTemplate.getForObject(
                url,
                SearchResponse.class
            );

        for (PageItem pageItem : response.getPageItems()) {
            if (pageItem.getImage_url() != null) {
                urlImage = pageItem.getImage_url();
            }
        }

        return urlImage;
    }
}
