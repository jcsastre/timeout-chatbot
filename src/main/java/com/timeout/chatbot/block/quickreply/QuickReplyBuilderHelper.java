package com.timeout.chatbot.block.quickreply;

import com.github.messenger4j.send.QuickReply;
import com.timeout.chatbot.domain.Venue;
import com.timeout.chatbot.domain.payload.PayloadType;
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

    public void addDiscoverToList(QuickReply.ListBuilder listBuilder) {
        listBuilder.addTextQuickReply(
            "Discover",
            new JSONObject()
                .put("type", PayloadType.discover)
                .toString()
        ).toList();
    }

    public void addWhatsNewToList(QuickReply.ListBuilder listBuilder) {
        listBuilder.addTextQuickReply(
            "What's new",
            new JSONObject()
                .put("type", PayloadType.whats_new)
                .toString()
        ).toList();
    }

    public void addMostLovedToList(QuickReply.ListBuilder listBuilder) {
        listBuilder.addTextQuickReply(
            "Most loved",
            new JSONObject()
                .put("type", PayloadType.most_loved)
                .toString()
        ).toList();
    }

    public void addSearchSuggestionsToList(QuickReply.ListBuilder listBuilder) {
        listBuilder.addTextQuickReply(
            "Search suggestions",
            new JSONObject()
                .put("type", PayloadType.search_suggestions)
                .toString()
        ).toList();
    }

    public List<QuickReply> buildForSeeVenueItem(
        Venue venue
    ) {
        final QuickReply.ListBuilder listBuilder = QuickReply.newListBuilder();

        listBuilder.addTextQuickReply(
            "Back",
            new JSONObject()
                .put("type", PayloadType.back)
                .toString()
        ).toList();

        listBuilder.addTextQuickReply(
            "Book",
            new JSONObject()
                .put("type", PayloadType.book)
                .toString()
        ).toList();

//        listBuilder.addTextQuickReply(
//            "Get a summary",
//            new JSONObject()
//                .put("type", PayloadType.get_a_summary)
//                .toString()
//        ).toList();

        if (venue.getImages()!=null && venue.getImages().size()>0) {
            listBuilder.addTextQuickReply(
                "Photos",
                new JSONObject()
                    .put("type", PayloadType.photos)
                    .toString()
            ).toList();
        }

        listBuilder.addTextQuickReply(
            "Submit a review",
            new JSONObject()
                .put("type", PayloadType.submit_review)
                .toString()
        ).toList();

        listBuilder.addTextQuickReply(
            "Submit a photo",
            new JSONObject()
                .put("type", PayloadType.submit_photo)
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
