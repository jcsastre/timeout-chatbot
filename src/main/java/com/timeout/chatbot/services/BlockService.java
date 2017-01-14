package com.timeout.chatbot.services;

import com.timeout.chatbot.block.*;
import com.timeout.chatbot.domain.User;
import com.timeout.chatbot.domain.session.Session;
import com.timeout.chatbot.domain.session.SessionContextBag;
import com.timeout.chatbot.graffitti.domain.response.search.page.PageItem;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.validation.constraints.NotNull;
import java.util.List;

@Component
public class BlockService {

    private final WelcomeFirstTimeBlock welcomeFirstTimeBlock;
    private final SuggestionsBlock suggestionsBlock;
    private final DiscoverBlock discoverBlock;
    private final WelcomeBackBlock welcomeBackBlock;
    private final MainOptionsBlock mainOptionsBlock;
    private final VenuesPageBlock venuesPageBlock;
    private final VenueSummaryBlock venueSummaryBlock;
    private final GeolocationAskBlock geolocationAskBlock;
    private final FilmsPageBlock filmsPageBlock;

    @Autowired
    public BlockService(
        WelcomeFirstTimeBlock welcomeFirstTimeBlock,
        SuggestionsBlock suggestionsBlock,
        DiscoverBlock discoverBlock,
        WelcomeBackBlock welcomeBackBlock,
        MainOptionsBlock mainOptionsBlock,
        VenuesPageBlock venuesPageBlock,
        VenueSummaryBlock venueSummaryBlock,
        GeolocationAskBlock geolocationAskBlock,
        FilmsPageBlock filmsPageBlock
    ) {
        this.welcomeFirstTimeBlock = welcomeFirstTimeBlock;
        this.suggestionsBlock = suggestionsBlock;
        this.discoverBlock = discoverBlock;
        this.welcomeBackBlock = welcomeBackBlock;
        this.mainOptionsBlock = mainOptionsBlock;
        this.venuesPageBlock = venuesPageBlock;
        this.venueSummaryBlock = venueSummaryBlock;
        this.geolocationAskBlock = geolocationAskBlock;
        this.filmsPageBlock = filmsPageBlock;
    }

    public void sendWelcomeFirstTimeBlock(
        User user
    ) {
        welcomeFirstTimeBlock.send(user);
    }

    public void sendWelcomeBackBlock(
        User user
    ) {
        welcomeBackBlock.send(user);
    }

    public void sendSuggestionsBlock(
        User user
    ) {
        suggestionsBlock.send(user.getMessengerId());
    }

    public void sendDiscoverBlock(
        User user
    ) {
        discoverBlock.send(
            user.getMessengerId()
        );
    }

    public void sendFilmsPageBlock(
        Session session,
        @NotNull Integer pageNumber
    ) {
        filmsPageBlock.send(
            session,
            pageNumber
        );
    }

    public void sendMainOptionsBlock(
        User user
    ) {
        mainOptionsBlock.send(user);
    }

    public void sendVenuesPageBlock(
        String userId,
        SessionContextBag.Geolocation sessionGeolocation,
        List<PageItem> pageItems,
        String itemPluralName,
        Integer nextPageNumber,
        Integer remainingItems
    ) {
        venuesPageBlock.send(
            userId,
            pageItems,
            itemPluralName,
            remainingItems,
            nextPageNumber,
            sessionGeolocation
        );
    }

    public void sendVenueSummaryBlock(
        String userId,
        String restaurantId
    ) {
        venueSummaryBlock.send(userId, restaurantId);
    }

    public void sendGeolocationAskBlock(
        String userId
    ) {
        geolocationAskBlock.send(userId);
    }
}
