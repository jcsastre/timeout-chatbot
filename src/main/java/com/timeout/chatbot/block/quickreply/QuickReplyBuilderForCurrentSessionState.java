package com.timeout.chatbot.block.quickreply;

import com.github.messenger4j.send.QuickReply;
import com.timeout.chatbot.graffitti.domain.GraffittiType;
import com.timeout.chatbot.graffitti.response.facets.v5.GraffittiFacetV5Node;
import com.timeout.chatbot.services.GraffittiService;
import com.timeout.chatbot.session.Session;
import com.timeout.chatbot.session.SessionStateItemBag;
import com.timeout.chatbot.session.context.SessionState;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class QuickReplyBuilderForCurrentSessionState {

    private final QuickReplyBuilderHelper quickReplyBuilderHelper;
    private final GraffittiService graffittiService;

    @Autowired
    public QuickReplyBuilderForCurrentSessionState(
        QuickReplyBuilderHelper quickReplyBuilderHelper,
        GraffittiService graffittiService
    ) {
        this.quickReplyBuilderHelper = quickReplyBuilderHelper;
        this.graffittiService = graffittiService;
    }

    public List<QuickReply> build(
        Session session
    ) {
        final SessionState sessionState = session.getSessionState();

        switch (sessionState) {

            case UNDEFINED:
                return handleStateUndefined(session);

            case SEARCH_SUGGESTIONS:
                return handleStateSearchSuggestions(session);

            case MOST_LOVED:
                return handleStateMostLoved(session);

//            case LOOKING:
//                //TODO
//                break;

            case ITEM:
                return handleStateItem(session);

            default:
                //TODO
                return null;
        }
    }

    public  List<QuickReply> handleStateMostLoved(
        Session session
    ) {
        final QuickReply.ListBuilder listBuilder = QuickReply.newListBuilder();

        quickReplyBuilderHelper.addDiscoverToList(listBuilder);
        quickReplyBuilderHelper.addWhatsNewToList(listBuilder);
        quickReplyBuilderHelper.addSearchSuggestionsToList(listBuilder);

        return listBuilder.build();
    }

    public  List<QuickReply> handleStateSearchSuggestions(
        Session session
    ) {
        final QuickReply.ListBuilder listBuilder = QuickReply.newListBuilder();

        quickReplyBuilderHelper.addDiscoverToList(listBuilder);
        quickReplyBuilderHelper.addWhatsNewToList(listBuilder);
        quickReplyBuilderHelper.addMostLovedToList(listBuilder);

        return listBuilder.build();
    }

    public  List<QuickReply> handleStateUndefined(
        Session session
    ) {
        final QuickReply.ListBuilder listBuilder = QuickReply.newListBuilder();

        for (GraffittiFacetV5Node primaryCategoryPrimary : graffittiService.getFacetsV5PrimaryCategories()) {
            listBuilder.addTextQuickReply(
                primaryCategoryPrimary.getName(),
                new JSONObject()
                    .put("type", "utterance")
                    .put("utterance", primaryCategoryPrimary.getName())
                    .toString()
            ).toList();
        }

        return listBuilder.build();
    }

    public  List<QuickReply> handleStateItem(
        Session session
    ) {
        final SessionStateItemBag itemBag = session.getSessionStateItemBag();

        final GraffittiType graffittiType = itemBag.getGraffittiType();

        switch (graffittiType) {

            case VENUE:
                return
                    quickReplyBuilderHelper.buildForSeeVenueItem(
                        itemBag.getGraffittiVenueResponse()
                    );

            case FILM:
                //TODO
                return null;

            case EVENT:
                //TODO
                return null;

            default:
                //TODO
                return null;
        }
    }
}
