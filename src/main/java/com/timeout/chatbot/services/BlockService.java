package com.timeout.chatbot.services;

import com.timeout.chatbot.block.*;
import com.timeout.chatbot.domain.User;
import com.timeout.chatbot.domain.session.SessionContextBag;
import com.timeout.chatbot.graffitti.domain.response.search.page.PageItem;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class BlockService {

    private final WelcomeFirstTimeBlock welcomeFirstTimeBlock;
    private final DiscoverBlock discoverBlock;
    private final WelcomeBackBlock welcomeBackBlock;
    private final MainOptionsBlock mainOptionsBlock;
    private final VenuesPageBlock venuesPageBlock;
    private final RestaurantSummaryBlock restaurantSummaryBlock;
    private final GeolocationAskBlock geolocationAskBlock;

    @Autowired
    public BlockService(
        WelcomeFirstTimeBlock welcomeFirstTimeBlock,
        DiscoverBlock discoverBlock,
        WelcomeBackBlock welcomeBackBlock,
        MainOptionsBlock mainOptionsBlock,
        VenuesPageBlock venuesPageBlock,
        RestaurantSummaryBlock restaurantSummaryBlock,
        GeolocationAskBlock geolocationAskBlock
    ) {
        this.welcomeFirstTimeBlock = welcomeFirstTimeBlock;
        this.discoverBlock = discoverBlock;
        this.welcomeBackBlock = welcomeBackBlock;
        this.mainOptionsBlock = mainOptionsBlock;
        this.venuesPageBlock = venuesPageBlock;
        this.restaurantSummaryBlock = restaurantSummaryBlock;
        this.geolocationAskBlock = geolocationAskBlock;
    }

    public void sendWelcomeFirstTimeBlock(
        User user
    ) {
        welcomeFirstTimeBlock.send(user);
    }

    public void sendDiscoverBlock(
        User user
    ) {
        discoverBlock.send(
            user.getMessengerId()
        );
    }

    public void sendWelcomeBackBlock(
        User user
    ) {
        welcomeBackBlock.send(user);
    }

    public void sendMainOptionsBlock(
        User user
    ) {
        mainOptionsBlock.send(user);
    }

    public void sendVenuesPageBlock(
        String userId,
        SessionContextBag.Geolocation userGeolocation,
        List<PageItem> pageItems,
        String itemPluralName,
        Integer nextPageNumber,
        Integer remainingItems
    ) {
        venuesPageBlock.send(
            userId,
            userGeolocation,
            pageItems,
            itemPluralName,
            nextPageNumber,
            remainingItems
        );
    }

    public void sendRestaurantSummaryBlock(
        String userId,
        String restaurantId
    ) {
        restaurantSummaryBlock.send(userId, restaurantId);
    }

    public void sendGeolocationAskBlock(
        String userId
    ) {
        geolocationAskBlock.send(userId);
    }
}
