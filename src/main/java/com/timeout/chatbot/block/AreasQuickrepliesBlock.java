package com.timeout.chatbot.block;

import com.github.messenger4j.exceptions.MessengerApiException;
import com.github.messenger4j.exceptions.MessengerIOException;
import com.github.messenger4j.send.MessengerSendClient;
import com.github.messenger4j.send.QuickReply;
import com.timeout.chatbot.domain.Neighborhood;
import com.timeout.chatbot.domain.payload.PayloadType;
import com.timeout.chatbot.services.GraffittiService;
import com.timeout.chatbot.session.Session;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class AreasQuickrepliesBlock {

    private final MessengerSendClient messengerSendClient;
    private final GraffittiService graffittiService;

    @Autowired
    public AreasQuickrepliesBlock(
        MessengerSendClient messengerSendClient,
        GraffittiService graffittiService
    ) {
        this.messengerSendClient = messengerSendClient;
        this.graffittiService = graffittiService;
    }

    public void send(
        Session session,
        Integer pageNumber
    ) throws MessengerApiException, MessengerIOException {

        messengerSendClient.sendTextMessage(
            session.getUser().getMessengerId(),
            "Please, select one",
            buildQuickReplies(
                pageNumber
            )
        );
    }

    private List<QuickReply> buildQuickReplies(
        Integer pageNumber
    ) {
        final List<Neighborhood> neighborhoods = graffittiService.getNeighborhoods();

        final QuickReply.ListBuilder builder = QuickReply.newListBuilder();

        builder.addTextQuickReply(
            "Cancel",
                new JSONObject()
                    .put("type", PayloadType.cancel)
                    .toString()
        ).toList();

        if (pageNumber == 1) {
            builder.addTextQuickReply(
                "Everywhere",
                new JSONObject()
                    .put("type", PayloadType.searching_WhereEverywhere)
                    .toString()
            ).toList();

            builder.addLocationQuickReply().toList();
        }

        final int neighborhoodsCount = neighborhoods.size();

        int initPos = (pageNumber - 1) * PAGE_SIZE;
        for (int i=initPos; i<initPos+PAGE_SIZE && i<neighborhoodsCount; i++) {
            final Neighborhood neighborhood = neighborhoods.get(i);
            String name = neighborhood.getName();
            if (name.length()>20) {
                name = name.substring(0, 20);
            }

            builder.addTextQuickReply(
                name,
                new JSONObject()
                    .put("type", PayloadType.searching_VenuesSetNeighborhood)
                    .put("neighborhood_id", neighborhood.getGraffitiId())
                    .toString()
            ).toList();
        }

        if (pageNumber * PAGE_SIZE < neighborhoodsCount) {
            builder.addTextQuickReply(
                "More neighborhoods",
                new JSONObject()
                    .put("type", PayloadType.searching_VenuesShowAreas)
                    .put("pageNumber", pageNumber+1)
                    .toString()
            ).toList();
        }

        return builder.build();
    }

    private static final int PAGE_SIZE = 6;
}
