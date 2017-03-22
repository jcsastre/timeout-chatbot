package com.timeout.chatbot.block;

import com.github.messenger4j.exceptions.MessengerApiException;
import com.github.messenger4j.exceptions.MessengerIOException;
import com.github.messenger4j.send.MessengerSendClient;
import com.github.messenger4j.send.QuickReply;
import com.timeout.chatbot.domain.Geolocation;
import com.timeout.chatbot.domain.Neighborhood;
import com.timeout.chatbot.domain.payload.PayloadType;
import com.timeout.chatbot.domain.payload.QuickreplyPayload;
import com.timeout.chatbot.graffitti.domain.GraffittiCategory;
import com.timeout.chatbot.graffitti.domain.GraffittiSubcategory;
import com.timeout.chatbot.services.GraffittiService;
import com.timeout.chatbot.session.Session;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class BlockAreas {

    private final MessengerSendClient msc;
    private final GraffittiService graffittiService;

    @Autowired
    public BlockAreas(
        MessengerSendClient msc,
        GraffittiService graffittiService
    ) {
        this.msc = msc;
        this.graffittiService = graffittiService;
    }

    public void send(
        Session session,
        Integer pageNumber
    ) throws MessengerApiException, MessengerIOException {

        msc.sendTextMessage(
            session.user.messengerId,
            "Please, select one",
            buildQuickReplies(
                pageNumber,
                session.bagSearching.graffittiCategory,
                session.bagSearching.graffittiSubcategory,
                session.bagSearching.neighborhood,
                session.bagSearching.geolocation
            )
        );
    }

    private List<QuickReply> buildQuickReplies(
        Integer pageNumber,
        GraffittiCategory graffittiCategory,
        GraffittiSubcategory graffittiSubcategory,
        Neighborhood neighborhood,
        Geolocation geolocation
    ) {
        final List<Neighborhood> neighborhoods = graffittiService.getNeighborhoods();

        final QuickReply.ListBuilder builder = QuickReply.newListBuilder();

        builder.addTextQuickReply(
            "Cancel",
                new JSONObject()
                    .put("type", PayloadType._Cancel)
                    .toString()
        ).toList();

        if (pageNumber == 1) {
            builder.addTextQuickReply(
                "Everywhere",
                new JSONObject()
                    .put("type", QuickreplyPayload.searching_set_area_any)
//                    // We add extra info to allow to execute action even when session has expired
//                    .put("graffitti_category_id", graffittiCategory.graffittiId)
//                    .put("graffitti_subcategory_id", graffittiSubcategory.graffittiId)
                    .toString()
            ).toList();

            builder.addLocationQuickReply().toList();
        }

        final int neighborhoodsCount = neighborhoods.size();

        int initPos = (pageNumber - 1) * PAGE_SIZE;
        for (int i=initPos; i<initPos+PAGE_SIZE && i<neighborhoodsCount; i++) {
            final Neighborhood currentNeighborhood = neighborhoods.get(i);
            String name = currentNeighborhood.name;
            if (name.length()>20) {
                name = name.substring(0, 20);
            }

            builder.addTextQuickReply(
                name,
                new JSONObject()
                    .put("type", QuickreplyPayload.searching_set_area_neighborhood)
                    .put("neighborhood_id", currentNeighborhood.graffitiId)
                    .toString()
            ).toList();
        }

        if (pageNumber * PAGE_SIZE < neighborhoodsCount) {
            builder.addTextQuickReply(
                "More neighborhoods",
                new JSONObject()
                    .put("type", QuickreplyPayload.searching_show_areas)
                    .put("pageNumber", pageNumber+1)
                    .toString()
            ).toList();
        }

        return builder.build();
    }

    private static final int PAGE_SIZE = 6;
}
