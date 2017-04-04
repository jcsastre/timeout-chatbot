package com.timeout.chatbot.action;

import com.github.messenger4j.exceptions.MessengerApiException;
import com.github.messenger4j.exceptions.MessengerIOException;
import com.timeout.chatbot.handler.intent.IntentFindVenuesHandler;
import com.timeout.chatbot.services.BlockService;
import com.timeout.chatbot.session.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class SetSubcategoryAction {

    private final BlockService blockService;
    private final IntentFindVenuesHandler findRestaurantsHandler;

    @Autowired
    public SetSubcategoryAction(
        BlockService blockService,
        IntentFindVenuesHandler findRestaurantsHandler
    ) {
        this.blockService = blockService;
        this.findRestaurantsHandler = findRestaurantsHandler;
    }

    public void perform(
        Session session,
        String subcategoryId
    ) throws MessengerApiException, MessengerIOException, IOException, InterruptedException {

        session.bagSearching.graffittiSubcategory =
            session.bagSearching.graffittiCategory.findSubcategoryByGraffittiId(subcategoryId);

        findRestaurantsHandler.fetchAndSend(session);
    }
}
