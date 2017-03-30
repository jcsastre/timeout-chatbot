package com.timeout.chatbot.action;

import com.github.messenger4j.exceptions.MessengerApiException;
import com.github.messenger4j.exceptions.MessengerIOException;
import com.timeout.chatbot.graffitti.domain.GraffittiCategory;
import com.timeout.chatbot.session.Session;
import com.timeout.chatbot.session.bag.SessionStateSearchingBag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class BackFromItemAction {

    private final FindVenuesAction findVenuesAction;

    @Autowired
    public BackFromItemAction(
        FindVenuesAction findVenuesAction
    ) {
        this.findVenuesAction = findVenuesAction;
    }

    public void perform(
        Session session
    ) throws MessengerApiException, MessengerIOException, IOException, InterruptedException {

        final SessionStateSearchingBag bag = session.bagSearching;

        if (
            bag.graffittiCategory == GraffittiCategory.RESTAURANTS ||
            bag.graffittiCategory == GraffittiCategory.HOTELS
        ) {
            findVenuesAction.perform(session);
        }
    }
}
