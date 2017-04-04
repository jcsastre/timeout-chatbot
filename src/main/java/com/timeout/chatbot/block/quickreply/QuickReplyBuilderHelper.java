package com.timeout.chatbot.block.quickreply;

import com.github.messenger4j.send.QuickReply;
import com.timeout.chatbot.domain.Venue;
import com.timeout.chatbot.domain.payload.QuickreplyPayload;
import com.timeout.chatbot.graffitti.response.images.GraffittiImage;
import com.timeout.chatbot.graffitti.response.images.GraffittiImagesResponse;
import com.timeout.chatbot.graffitti.response.venue.GraffittiVenueResponse;
import com.timeout.chatbot.graffitti.urlbuilder.VenuesUrlBuilder;
import io.mikael.urlbuilder.UrlBuilder;
import org.json.JSONObject;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Component
public class QuickReplyBuilderHelper {

    private final VenuesUrlBuilder venuesUrlBuilder;
    private final RestTemplate restTemplate;

    public QuickReplyBuilderHelper(
        VenuesUrlBuilder venuesUrlBuilder,
        RestTemplate restTemplate
    ) {
        this.venuesUrlBuilder = venuesUrlBuilder;
        this.restTemplate = restTemplate;
    }

    public List<QuickReply> buildForSeeVenueItem(
        Venue venue
    ) {
        final QuickReply.ListBuilder listBuilder = QuickReply.newListBuilder();

        listBuilder.addTextQuickReply(
            "Back",
            new JSONObject()
                .put("type", QuickreplyPayload.item_back)
                .toString()
        ).toList();

        listBuilder.addTextQuickReply(
            "Book",
            new JSONObject()
                .put("type", QuickreplyPayload.item_book)
                .toString()
        ).toList();

        if (venue.images!=null && venue.images.size()>0) {
            listBuilder.addTextQuickReply(
                "Photos",
                new JSONObject()
                    .put("type", QuickreplyPayload.item_photos)
                    .toString()
            ).toList();
        }

        listBuilder.addTextQuickReply(
            "Submit a review",
            new JSONObject()
                .put("type", QuickreplyPayload.item_submit_review)
                .toString()
        ).toList();

        listBuilder.addTextQuickReply(
            "Submit a photo",
            new JSONObject()
                .put("type", QuickreplyPayload.item_submit_photo)
                .toString()
        ).toList();

        return listBuilder.build();
    }

    public List<GraffittiImage> getImages(
        GraffittiVenueResponse graffittiVenueResponse
    ) {
        final UrlBuilder urlBuilder = venuesUrlBuilder.buildImages(graffittiVenueResponse.getBody().getId());

        final GraffittiImagesResponse graffittiImagesResponse =
            restTemplate.getForObject(
                urlBuilder.toUri(),
                GraffittiImagesResponse.class
            );

        return graffittiImagesResponse.getGraffittiImages();
    }
}
