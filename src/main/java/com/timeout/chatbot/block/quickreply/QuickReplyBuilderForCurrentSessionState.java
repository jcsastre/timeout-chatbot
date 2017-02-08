package com.timeout.chatbot.block.quickreply;

import com.github.messenger4j.send.QuickReply;
import com.timeout.chatbot.graffitti.domain.GraffittiType;
import com.timeout.chatbot.session.Session;
import com.timeout.chatbot.session.SessionStateItemBag;
import com.timeout.chatbot.session.context.SessionState;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class QuickReplyBuilderForCurrentSessionState {

    private final QuickReplyBuilderHelper quickReplyBuilderHelper;

    @Autowired
    public QuickReplyBuilderForCurrentSessionState(
        QuickReplyBuilderHelper quickReplyBuilderHelper
    ) {
        this.quickReplyBuilderHelper = quickReplyBuilderHelper;
    }

    public List<QuickReply> build(
        Session session
    ) {
        final SessionState sessionState = session.getSessionState();

        switch (sessionState) {

//            case UNDEFINED:
//                //TODO
//                break;

//            case WELCOMED:
//                //TODO
//                break;

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
