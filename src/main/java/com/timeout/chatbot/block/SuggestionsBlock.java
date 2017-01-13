package com.timeout.chatbot.block;

import com.github.messenger4j.send.buttons.Button;
import com.github.messenger4j.send.templates.GenericTemplate;
import com.timeout.chatbot.messenger4j.send.MessengerSendClientWrapper;
import com.timeout.chatbot.services.GraffittiService;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class SuggestionsBlock {

    private final MessengerSendClientWrapper messengerSendClientWrapper;
    private final RestTemplate restTemplate;
    private final GraffittiService graffittiService;

    @Autowired
    public SuggestionsBlock(
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
        messengerSendClientWrapper.sendTextMessage(
            userId,
            "These are my suggestions for you today. If you want to see them again just type 'suggestions'"
        );

        messengerSendClientWrapper.sendTemplate(
            userId,
            buildGenericTemplate()
        );
    }

    private GenericTemplate buildGenericTemplate() {
        return
            GenericTemplate.newBuilder().addElements()
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
                .addElement("Restaurants nearby")
                    .imageUrl("https://media.timeout.com/images/102173995/image.jpg")
                    .buttons(
                        Button.newListBuilder()
                            .addPostbackButton(
                                "Let's go",
                                new JSONObject()
                                    .put("type", "utterance")
                                    .put("utterance", "Restaurants nearby")
                                    .toString()
                            ).toList()
                            .build()
                    )
                    .toList()
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
                .done()
                .build();
    }
}
