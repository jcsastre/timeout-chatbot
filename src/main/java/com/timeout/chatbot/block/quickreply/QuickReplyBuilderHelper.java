package com.timeout.chatbot.block.quickreply;

import com.github.messenger4j.send.QuickReply;
import com.timeout.chatbot.domain.payload.PayloadType;
import com.timeout.chatbot.graffitti.response.venues.GraffittiVenueResponse;
import org.json.JSONObject;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class QuickReplyBuilderHelper {

    public List<QuickReply> buildForSeeVenueItem(
        GraffittiVenueResponse graffittiVenueResponse
    ) {

        final GraffittiVenueResponse.Body venue = graffittiVenueResponse.getBody();

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
                .put("type", PayloadType.see_more)
                .toString()
        ).toList();

//        final String phone = venue.getPhone();
//        if (phone != null) {
//            final String phoneNumber = "+34" + phone.replaceAll(" ","");
//            listBuilder.addTextQuickReply(
//                "Call",
//                new JSONObject()
//                    .put("type", PayloadType.phone_call)
//                    .put("phone_number", phoneNumber)
//                    .put("venue_name", venue.getName())
//                    .toString()
//            ).toList();
//        }

        listBuilder.addTextQuickReply(
            "Get a summary",
            new JSONObject()
                .put("type", PayloadType.get_a_summary)
                .toString()
        ).toList();

        listBuilder.addTextQuickReply(
            "More photos",
            new JSONObject()
                .put("type", PayloadType.see_more)
                .toString()
        ).toList();

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
}
