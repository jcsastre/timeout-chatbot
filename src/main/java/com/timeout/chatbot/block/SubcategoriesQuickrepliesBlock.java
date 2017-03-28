package com.timeout.chatbot.block;

import com.github.messenger4j.exceptions.MessengerApiException;
import com.github.messenger4j.exceptions.MessengerIOException;
import com.github.messenger4j.send.MessengerSendClient;
import com.github.messenger4j.send.QuickReply;
import com.timeout.chatbot.domain.payload.PayloadType;
import com.timeout.chatbot.domain.payload.QuickreplyPayload;
import com.timeout.chatbot.graffitti.domain.GraffittiSubcategory;
import com.timeout.chatbot.session.Session;
import com.timeout.chatbot.session.bag.SessionStateSearchingBag;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class SubcategoriesQuickrepliesBlock {

    private final MessengerSendClient messengerSendClient;

    @Autowired
    public SubcategoriesQuickrepliesBlock(
        MessengerSendClient messengerSendClient
    ) {
        this.messengerSendClient = messengerSendClient;
    }

    public void send(
        Session session,
        Integer pageNumber
    ) throws MessengerApiException, MessengerIOException {

        messengerSendClient.sendTextMessage(
            session.user.messengerId,
            "Please, select one",
            buildQuickReplies(
                session,
                pageNumber
            )
        );
    }

    private List<QuickReply> buildQuickReplies(
        Session session,
        Integer pageNumber
    ) {
        final QuickReply.ListBuilder builder = QuickReply.newListBuilder();

        builder.addTextQuickReply(
            "Cancel",
            new JSONObject()
                .put("type", PayloadType._Cancel)
                .toString()
        ).toList();

        final SessionStateSearchingBag bag = session.bagSearching;

        if (pageNumber == 1) {
            builder.addTextQuickReply(
                "All " + bag.graffittiCategory.subcategoriesNamePlural.toLowerCase(),
                new JSONObject()
                    .put("type", PayloadType.subcategory_any)
                    .toString()
            ).toList();
        }

        final List<GraffittiSubcategory> subcategories = bag.graffittiCategory.subcategories;

        final int count = subcategories.size();

        int initPos = (pageNumber - 1) * PAGE_SIZE;
        for (int i=initPos; i<initPos+PAGE_SIZE && i<count; i++) {
            final GraffittiSubcategory subcategory = subcategories.get(i);
            String name = subcategory.name;
            if (name.length()>20) {
                name = name.substring(0, 20);
            }

            builder.addTextQuickReply(
                name,
                new JSONObject()
                    .put("type", QuickreplyPayload.searching_set_subcategory)
                    .put("subcategory_id", subcategory.graffittiId)
                    .toString()
            ).toList();
        }

        if (pageNumber * PAGE_SIZE < count) {
            builder.addTextQuickReply(
                "More " + bag.graffittiCategory.subcategoriesNamePlural.toLowerCase(),
                new JSONObject()
                    .put("type", QuickreplyPayload.searching_show_subcategories)
                    .put("pageNumber", pageNumber+1)
                    .toString()
            ).toList();
        }

        return builder.build();
    }

    private static final int PAGE_SIZE = 6;
}
