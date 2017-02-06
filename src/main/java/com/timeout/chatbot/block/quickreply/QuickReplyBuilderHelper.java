package com.timeout.chatbot.block.quickreply;

import com.github.messenger4j.send.QuickReply;
import com.timeout.chatbot.domain.payload.PayloadType;
import org.json.JSONObject;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class QuickReplyBuilderHelper {

    public List<QuickReply> buildForSeeVenueItem() {

        final QuickReply.ListBuilder listBuilder = QuickReply.newListBuilder();

        listBuilder.addTextQuickReply(
            "More restaurants",
            new JSONObject()
                .put("type", PayloadType.see_more)
                .toString()
        ).toList();

        listBuilder.addTextQuickReply(
            "Book",
            new JSONObject()
                .put("type", PayloadType.see_more)
                .toString()
        ).toList();

        listBuilder.addTextQuickReply(
            "Call",
            new JSONObject()
                .put("type", PayloadType.see_more)
                .toString()
        ).toList();

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
            "Submit review",
            new JSONObject()
                .put("type", PayloadType.see_more)
                .toString()
        ).toList();

        listBuilder.addTextQuickReply(
            "Submit photo",
            new JSONObject()
                .put("type", PayloadType.see_more)
                .toString()
        ).toList();

        return listBuilder.build();
    }
}
