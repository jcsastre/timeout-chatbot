package com.timeout.chatbot.block;

import com.github.messenger4j.send.buttons.Button;
import com.github.messenger4j.send.templates.GenericTemplate;
import com.timeout.chatbot.domain.payload.PayloadType;
import com.timeout.chatbot.session.Session;
import com.timeout.chatbot.graffitti.domain.response.films.GraffitiFilm;
import com.timeout.chatbot.graffitti.domain.response.search.page.PageItem;
import com.timeout.chatbot.graffitti.domain.response.search.page.SearchResponse;
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

        List<GraffitiFilm> graffitiFilms = new ArrayList<>();

        final SearchResponse searchResponse =
            restTemplate.getForObject(
                FilmsEndpoint.buildGeolocatedUri(
                    session.getSessionStateLookingBag().getGeolocation().getLatitude(),
                    session.getSessionStateLookingBag().getGeolocation().getLongitude(),
                    pageNumber
                ),
                SearchResponse.class
            );

        final List<PageItem> pageItems = searchResponse.getPageItems();
        for (PageItem pageItem : pageItems) {
            graffitiFilms.add(
                restTemplate.getForObject(
                    pageItem.getUrl(),
                    GraffitiFilm.class
                )
            );
        }

        sendHorizontalCarroussel(
            session.getUser().getMessengerId(),
            graffitiFilms
        );
    }

    private void sendHorizontalCarroussel(
        String recipientId,
        List<GraffitiFilm> graffitiFilms
    ) {
        final GenericTemplate.Builder genericTemplateBuilder = GenericTemplate.newBuilder();
        final GenericTemplate.Element.ListBuilder listBuilder = genericTemplateBuilder.addElements();
        for (GraffitiFilm graffitiFilm : graffitiFilms) {
            final GenericTemplate.Element.Builder elementBuilder =
                listBuilder.addElement(graffitiFilm.getBody().getName());

            if (graffitiFilm.getBody().getImageUrl() != null) {
                elementBuilder.imageUrl(graffitiFilm.getBody().getImageUrl());
            }

            elementBuilder.subtitle(graffitiFilm.getBody().getGraffittiCategorisation().buildNameMax80());

            final Button.ListBuilder buttonListBuilder = Button.newListBuilder();

            buttonListBuilder.addPostbackButton(
                "More info",
                new JSONObject()
                    .put("type", PayloadType.films_more_info)
                    .put("film_id", graffitiFilm.getBody().getId())
                    .toString()
            ).toList();

            if (graffitiFilm.getBody().getTrailer() != null) {
                buttonListBuilder.addUrlButton(
                    "See trailer",
                    graffitiFilm.getBody().getTrailer().getUrl()
                ).toList();
            }

            buttonListBuilder.addPostbackButton(
                "Find cinemas",
                new JSONObject()
                    .put("type", PayloadType.films_find_cinemas)
                    .put("film_id", graffitiFilm.getBody().getId())
                    .toString()
            ).toList().build();


            elementBuilder.buttons(buttonListBuilder.build()).toList().done();
        }

        final GenericTemplate genericTemplate = genericTemplateBuilder.build();
        messengerSendClientWrapper.sendTemplate(recipientId, genericTemplate);
    }
}
