package com.timeout.chatbot.block;

import com.github.messenger4j.exceptions.MessengerApiException;
import com.github.messenger4j.exceptions.MessengerIOException;
import com.github.messenger4j.send.MessengerSendClient;
import com.github.messenger4j.send.QuickReply;
import com.timeout.chatbot.domain.entities.Category;
import com.timeout.chatbot.domain.entities.Subcategory;
import com.timeout.chatbot.domain.payload.PayloadType;
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
            session.getUser().getMessengerId(),
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
                .put("type", PayloadType.cancel)
                .toString()
        ).toList();

        final SessionStateSearchingBag bag = session.getSessionStateSearchingBag();
        final Category category = bag.getCategory();

        if (pageNumber == 1) {
            builder.addTextQuickReply(
                "All " + category.getSubcategoriesNamePlural().toLowerCase(),
                new JSONObject()
                    .put("type", PayloadType.subcategory_any)
                    .toString()
            ).toList();
        }

        final List<Subcategory> subcategories = category.getSubcategories();

        final int count = subcategories.size();

        int initPos = (pageNumber - 1) * PAGE_SIZE;
        for (int i=initPos; i<initPos+PAGE_SIZE && i<count; i++) {
            final Subcategory subcategory = subcategories.get(i);
            String name = subcategory.getName();
            if (name.length()>20) {
                name = name.substring(0, 20);
            }

            builder.addTextQuickReply(
                name,
                new JSONObject()
                    .put("type", PayloadType.searching_SetSubcategory)
                    .put("subcategory_id", subcategory.getGraffittiId())
                    .toString()
            ).toList();
        }

        if (pageNumber * PAGE_SIZE < count) {
            builder.addTextQuickReply(
                "More " + category.getSubcategoriesNamePlural().toLowerCase(),
                new JSONObject()
                    .put("type", PayloadType.searching_ShowSubcategories)
                    .put("pageNumber", pageNumber+1)
                    .toString()
            ).toList();
        }

        return builder.build();
    }

    private static final int PAGE_SIZE = 6;
}
