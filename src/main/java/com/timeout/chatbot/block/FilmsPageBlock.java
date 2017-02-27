package com.timeout.chatbot.block;

import com.github.messenger4j.send.buttons.Button;
import com.github.messenger4j.send.templates.GenericTemplate;
import com.timeout.chatbot.domain.payload.PayloadType;
import com.timeout.chatbot.graffitti.response.search.page.GraffittiSearchResponse;
import com.timeout.chatbot.session.Session;
import com.timeout.chatbot.graffitti.response.films.GraffitiFilmResponse;
import com.timeout.chatbot.graffitti.response.search.page.PageItem;
import com.timeout.chatbot.graffitti.uri.FilmsEndpoint;
import com.timeout.chatbot.messenger4j.send.MessengerSendClientWrapper;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

@Component
public class FilmsPageBlock {
    private final RestTemplate restTemplate;
    private final MessengerSendClientWrapper messengerSendClientWrapper;

    @Autowired
    public FilmsPageBlock(
        RestTemplate restTemplate,
        MessengerSendClientWrapper messengerSendClientWrapper
    ) {
        this.restTemplate = restTemplate;
        this.messengerSendClientWrapper = messengerSendClientWrapper;
    }

    public void send(
        Session session,
        @NotNull Integer pageNumber
    ) {
        messengerSendClientWrapper.sendTextMessage(
            session.getUser().getMessengerId(),
            "Looking for Films within 500 meters."
        );

        List<GraffitiFilmResponse> graffitiFilmResponses = new ArrayList<>();

        final GraffittiSearchResponse graffittiSearchResponse =
            restTemplate.getForObject(
                FilmsEndpoint.buildGeolocatedUri(
                    session.getSessionStateSearchingBag().getGeolocation().getLatitude(),
                    session.getSessionStateSearchingBag().getGeolocation().getLongitude(),
                    pageNumber
                ),
                GraffittiSearchResponse.class
            );

        final List<PageItem> pageItems = graffittiSearchResponse.getPageItems();
        for (PageItem pageItem : pageItems) {
            graffitiFilmResponses.add(
                restTemplate.getForObject(
                    pageItem.getUrl(),
                    GraffitiFilmResponse.class
                )
            );
        }

        sendHorizontalCarroussel(
            session.getUser().getMessengerId(),
            graffitiFilmResponses
        );
    }

    private void sendHorizontalCarroussel(
        String recipientId,
        List<GraffitiFilmResponse> graffitiFilmResponses
    ) {
        final GenericTemplate.Builder genericTemplateBuilder = GenericTemplate.newBuilder();
        final GenericTemplate.Element.ListBuilder listBuilder = genericTemplateBuilder.addElements();
        for (GraffitiFilmResponse graffitiFilmResponse : graffitiFilmResponses) {
            final GenericTemplate.Element.Builder elementBuilder =
                listBuilder.addElement(graffitiFilmResponse.getBody().getName());

            if (graffitiFilmResponse.getBody().getImageUrl() != null) {
                elementBuilder.imageUrl(graffitiFilmResponse.getBody().getImageUrl());
            }

            elementBuilder.subtitle(graffitiFilmResponse.getBody().getGraffittiCategorisation().buildNameMax80());

            final Button.ListBuilder buttonListBuilder = Button.newListBuilder();

            buttonListBuilder.addPostbackButton(
                "More info",
                new JSONObject()
                    .put("type", PayloadType.films_more_info)
                    .put("film_id", graffitiFilmResponse.getBody().getId())
                    .toString()
            ).toList();

            if (graffitiFilmResponse.getBody().getTrailer() != null) {
                buttonListBuilder.addUrlButton(
                    "See trailer",
                    graffitiFilmResponse.getBody().getTrailer().getUrl()
                ).toList();
            }

            buttonListBuilder.addPostbackButton(
                "Find cinemas",
                new JSONObject()
                    .put("type", PayloadType.films_find_cinemas)
                    .put("film_id", graffitiFilmResponse.getBody().getId())
                    .toString()
            ).toList().build();


            elementBuilder.buttons(buttonListBuilder.build()).toList().done();
        }

        final GenericTemplate genericTemplate = genericTemplateBuilder.build();
        messengerSendClientWrapper.sendTemplate(recipientId, genericTemplate);
    }
}
