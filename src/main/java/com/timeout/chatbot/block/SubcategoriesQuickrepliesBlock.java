package com.timeout.chatbot.block;

import com.github.messenger4j.send.QuickReply;
import com.timeout.chatbot.domain.payload.PayloadType;
import com.timeout.chatbot.graffitti.response.facets.v4.GraffittiFacetV4FacetNode;
import com.timeout.chatbot.messenger4j.send.MessengerSendClientWrapper;
import com.timeout.chatbot.session.Session;
import com.timeout.chatbot.session.SessionStateLookingBag;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class SubcategoriesQuickrepliesBlock {

    private final MessengerSendClientWrapper messengerSendClientWrapper;
    private final DiscoverBlock discoverBlock;

    @Autowired
    public SubcategoriesQuickrepliesBlock(
        MessengerSendClientWrapper messengerSendClientWrapper,
        DiscoverBlock discoverBlock
    ) {
        this.messengerSendClientWrapper = messengerSendClientWrapper;
        this.discoverBlock = discoverBlock;
    }

    public void send(
        Session session,
        Integer pageNumber
    ) {
        final SessionStateLookingBag bag = session.getSessionStateLookingBag();

        final GraffittiFacetV4FacetNode categoryNode = bag.getGraffittiWhatCategoryNode();
        final List<GraffittiFacetV4FacetNode> children = categoryNode.getChildren();
        if (children!=null && children.size()>0) {
            buildAndSendMessage(session, children, pageNumber);
        } else {
            discoverBlock.send(session.getUser().getMessengerId());
        }
    }

    public void buildAndSendMessage(
        Session session,
        List<GraffittiFacetV4FacetNode> children,
        Integer pageNumber
    ) {
        messengerSendClientWrapper.sendTextMessage(
            session.getUser().getMessengerId(),
            "Please, select one",
            buildQuickReplies(
                session, children, pageNumber
            )
        );
    }

    private List<QuickReply> buildQuickReplies(
        Session session,
        List<GraffittiFacetV4FacetNode> children,
        Integer pageNumber
    ) {

        final QuickReply.ListBuilder builder = QuickReply.newListBuilder();

        builder.addTextQuickReply(
            "Cancel",
                new JSONObject()
                    .put("type", PayloadType.cancel)
                    .toString()
        ).toList();

        int initPos = (pageNumber - 1) * 9;
        for (int i=initPos; i<initPos+9; i++) {
            final GraffittiFacetV4FacetNode subcategory = children.get(i);
            String name = subcategory.getName();
            if (name.length()>20) {
                name = name.substring(0, 20);
            }
            builder.addTextQuickReply(
                name,
                new JSONObject()
                    .put("type", PayloadType.set_subcategory)
                    .put("id", subcategory.getId())
                    .toString()
            ).toList();
        }

//        if (bag.getReaminingItems() > 0) {
//            listBuilder.addTextQuickReply(
//                "See more",
//                new JSONObject()
//                    .put("type", PayloadType.see_more)
//                    .toString()
//            ).toList();
//        }
//
//        listBuilder.addLocationQuickReply().toList();
//
//        listBuilder.addTextQuickReply(
//            bag.getGraffittiWhere() != null ? "Change neighborhood" : "Set neighborhood",
//            new JSONObject()
//                .put("type", PayloadType.set_geolocation)
//                .toString()
//        ).toList();
//
//        String categorySingularName = "Cuisine";
//        if (bag.getWhat() == What.BAR) {
//            categorySingularName = "Style";
//        }
//
//        listBuilder.addTextQuickReply(
//            bag.getGraffittiWhatCategoryNode().getChildren() == null ?
//                "Change " + categorySingularName : "Set " + categorySingularName,
//            new JSONObject()
//                .put("type", PayloadType.set_graffitti_category)
//                .toString()
//        ).toList();
//
        return builder.build();
    }
}
