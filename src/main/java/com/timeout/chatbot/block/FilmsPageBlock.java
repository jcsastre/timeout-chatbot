package com.timeout.chatbot.block;

import com.github.messenger4j.send.buttons.Button;
import com.github.messenger4j.send.templates.GenericTemplate;
import com.timeout.chatbot.domain.payload.PayloadType;
import com.timeout.chatbot.domain.session.Session;
import com.timeout.chatbot.graffitti.domain.response.films.GraffitiFilm;
import com.timeout.chatbot.graffitti.domain.response.search.page.PageItem;
import com.timeout.chatbot.graffitti.domain.response.search.page.SearchResponse;
import com.timeout.chatbot.graffitti.endpoints.FilmsEndpoint;
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

        String url =
            FilmsEndpoint.getUrl(
                "node-7073",
                session.getSessionContextBag().getGeolocation().getLatitude(),
                session.getSessionContextBag().getGeolocation().getLongitude(),
                pageNumber
            );

        final SearchResponse searchResponse =
            restTemplate.getForObject(
                url,
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

            elementBuilder.subtitle(graffitiFilm.getBody().getCategorisation().buildNameMax80());

            elementBuilder.buttons(
                Button.newListBuilder()
                    .addPostbackButton(
                        "More info",
                        new JSONObject()
                            .put("type", PayloadType.films_more_info)
                            .put("film_id", graffitiFilm.getBody().getId())
                            .toString()
                    ).toList()
                    .addUrlButton(
                        "See trailer",
                        graffitiFilm.getBody().getTrailer().getUrl()
                    ).toList()
                    .addPostbackButton(
                        "Find cinemas",
                        new JSONObject()
                            .put("type", PayloadType.films_find_cinemas)
                            .put("film_id", graffitiFilm.getBody().getId())
                            .toString()
                    ).toList()
                .build()
            ).toList().done();
        }

        final GenericTemplate genericTemplate = genericTemplateBuilder.build();
        messengerSendClientWrapper.sendTemplate(recipientId, genericTemplate);
    }
}
