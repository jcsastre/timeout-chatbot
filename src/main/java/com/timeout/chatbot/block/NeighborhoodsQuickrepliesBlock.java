package com.timeout.chatbot.block;

import com.github.messenger4j.send.MessengerSendClient;
import com.github.messenger4j.send.QuickReply;
import com.timeout.chatbot.domain.payload.PayloadType;
import com.timeout.chatbot.graffitti.response.facets.v4.GraffittiFacetV4FacetNode;
import com.timeout.chatbot.services.GraffittiService;
import com.timeout.chatbot.session.Session;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class NeighborhoodsQuickrepliesBlock {

    private final MessengerSendClient messengerSendClient;
    private final GraffittiService graffittiService;

    @Autowired
    public NeighborhoodsQuickrepliesBlock(
        MessengerSendClient messengerSendClient,
        GraffittiService graffittiService
    ) {
        this.messengerSendClient = messengerSendClient;
        this.graffittiService = graffittiService;
    }

    public void send(
        Session session,
        Integer pageNumber
    ) throws Exception {

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
        final List<GraffittiFacetV4FacetNode> neighborhoodsNodes = graffittiService.getFacetNeighborhoodsNodes();

        final QuickReply.ListBuilder builder = QuickReply.newListBuilder();

        builder.addTextQuickReply(
            "Cancel",
                new JSONObject()
                    .put("type", PayloadType.cancel)
                    .toString()
        ).toList();

        final int neighborhoodsCount = neighborhoodsNodes.size();

        int initPos = (pageNumber - 1) * PAGE_SIZE;
        for (int i=initPos; i<initPos+PAGE_SIZE && i<neighborhoodsCount; i++) {
            final GraffittiFacetV4FacetNode neighborhoodsNode = neighborhoodsNodes.get(i);
            String name = neighborhoodsNode.getName();
            if (name.length()>20) {
                name = name.substring(0, 20);
            }

            builder.addTextQuickReply(
                name,
                new JSONObject()
                    .put("type", PayloadType.venues_set_neighborhood)
                    .put("neighborhood_id", neighborhoodsNode.getId())
                    .toString()
            ).toList();
        }

        if ((pageNumber + 1) * PAGE_SIZE < neighborhoodsCount) {
            builder.addTextQuickReply(
                "More neighborhoods",
                new JSONObject()
                    .put("type", PayloadType.venues_show_neighborhoods)
                    .put("pageNumber", pageNumber+1)
                    .toString()
            ).toList();
        }

        return builder.build();
    }

    private static final int PAGE_SIZE = 8;
}
