package com.timeout.chatbot.block.quickreply;

import com.github.messenger4j.send.QuickReply;
import com.timeout.chatbot.graffitti.domain.GraffittiType;
import com.timeout.chatbot.services.GraffittiService;
import com.timeout.chatbot.session.Session;
import com.timeout.chatbot.session.bag.SessionStateItemBag;
import com.timeout.chatbot.session.state.SessionState;
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
        final SessionState sessionState = session.state;

        switch (sessionState) {

            case DISCOVER:
                return handleDiscover(session);

            case ITEM:
                return handleStateItem(session);

            default:
                //TODO
                return null;
        }
    }

    public  List<QuickReply> handleDiscover(
        Session session
    ) {
        final QuickReply.ListBuilder listBuilder = QuickReply.newListBuilder();

        return listBuilder.build();
    }

//    public  List<QuickReply> handleWhatsNew(
//        Session session
//    ) {
//        final QuickReply.ListBuilder listBuilder = QuickReply.newListBuilder();
//
//        quickReplyBuilderHelper.addDiscoverToList(listBuilder);
//        quickReplyBuilderHelper.addMostLovedToList(listBuilder);
//        quickReplyBuilderHelper.addSearchSuggestionsToList(listBuilder);
//
//        return listBuilder.build();
//    }

    public  List<QuickReply> handleStateItem(
        Session session
    ) {
        final SessionStateItemBag itemBag = session.bagItem;

        final GraffittiType graffittiType = itemBag.graffittiType;

        switch (graffittiType) {

            case VENUE:
                return
                    quickReplyBuilderHelper.buildForSeeVenueItem(
                        itemBag.venue
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
