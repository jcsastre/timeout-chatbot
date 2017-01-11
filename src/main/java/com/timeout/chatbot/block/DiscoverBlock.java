package com.timeout.chatbot.block;

import com.github.messenger4j.send.NotificationType;
import com.github.messenger4j.send.QuickReply;
import com.github.messenger4j.send.Recipient;
import com.github.messenger4j.send.buttons.Button;
import com.github.messenger4j.send.templates.GenericTemplate;
import com.timeout.chatbot.domain.User;
import com.timeout.chatbot.graffitti.domain.response.facets.CategoryPrimary;
import com.timeout.chatbot.messenger4j.send.MessengerSendClientWrapper;
import com.timeout.chatbot.services.GraffittiService;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Component
public class DiscoverBlock {

    private final MessengerSendClientWrapper messengerSendClientWrapper;
    private final RestTemplate restTemplate;
    private final GraffittiService graffittiService;

    @Autowired
    public DiscoverBlock(
        MessengerSendClientWrapper messengerSendClientWrapper,
        RestTemplate restTemplate,
        GraffittiService graffittiService
    ) {
        this.messengerSendClientWrapper = messengerSendClientWrapper;
        this.restTemplate = restTemplate;
        this.graffittiService = graffittiService;
    }

    public void send(
        String userId
    ) {

        messengerSendClientWrapper.sendTemplate(
            Recipient.newBuilder().recipientId(userId).build(),
            NotificationType.REGULAR,
            buildGenericTemplate(),
            buildQuickReplies(),
            "metadata"
        );

//        sendSuggestions(userId);

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

    private GenericTemplate buildGenericTemplate() {
        return
            GenericTemplate.newBuilder().addElements()
                .addElement("Things to do this week")
                    .imageUrl("https://media.timeout.com/images/102872844/image.jpg")
                    .buttons(
                        Button.newListBuilder()
                            .addPostbackButton(
                                "Let's go",
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
                                "Let's go",
                                new JSONObject()
                                    .put("type", "utterance")
                                    .put("utterance", "Bars & pubs nearby")
                                    .toString()
                            ).toList()
                            .build()

                    )
                    .toList()
                .addElement("In cinemas now")
                .imageUrl("https://media.timeout.com/images/103667839/image.jpg")
                .buttons(
                    Button.newListBuilder()
                        .addPostbackButton(
                            "Let's go",
                            new JSONObject()
                                .put("type", "utterance")
                                .put("utterance", "In cinemas now")
                                .toString()
                        ).toList()
                        .build()

                )
                .toList()
                .done()
                .build();
    }

    private void sendSuggestions(String userId) {
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
    }

    public void sendQuickReplies(
        User user
    ) {
        String msg = "Above you can see some exploring suggestions, and below more exploratory options.";

        messengerSendClientWrapper.sendTextMessage(
            user.getMessengerId(),
            msg,
            buildQuickReplies()
        );
    }

    private List<QuickReply> buildQuickReplies() {

        final QuickReply.ListBuilder listBuilder = QuickReply.newListBuilder();

        for (CategoryPrimary primaryCategoryPrimary : graffittiService.getPrimaryCategories()) {
            listBuilder.addTextQuickReply(
                primaryCategoryPrimary.getName(),
                new JSONObject()
                    .put("type", "utterance")
                    .put("utterance", primaryCategoryPrimary.getName())
                    .toString()
            ).toList();
        }

        return listBuilder.build();
    }

//    public String getImageUrl(TileItem tileItem) {
//        String urlImage = null;
//
//        String url = null;
//        try {
//            url = URLDecoder.decode(tileItem.getUrl(), "UTF-8");
//        } catch (UnsupportedEncodingException e) {
//            e.printStackTrace();
//        }
//
//        System.out.println(url);
//
//        final SearchResponse response =
//            restTemplate.getForObject(
//                url,
//                SearchResponse.class
//            );
//
//        for (PageItem pageItem : response.getPageItems()) {
//            if (pageItem.getImage_url() != null) {
//                urlImage = pageItem.getImage_url();
//            }
//        }
//
//        return urlImage;
//    }
}
