package com.timeout.chatbot.services;

import com.timeout.chatbot.block.*;
import com.timeout.chatbot.domain.User;
import com.timeout.chatbot.graffitti.domain.response.search.page.PageItem;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class BlockService {

    private final WelcomeFirstTimeBlock welcomeFirstTimeBlock;
    private final WelcomeBackBlock welcomeBackBlock;
    private final MainOptionsBlock mainOptionsBlock;
    private final RestaurantsPageBlock restaurantsPageBlock;
    private final RestaurantSummaryBlock restaurantSummaryBlock;

    @Autowired
    public BlockService(
        WelcomeFirstTimeBlock welcomeFirstTimeBlock,
        WelcomeBackBlock welcomeBackBlock,
        MainOptionsBlock mainOptionsBlock,
        RestaurantsPageBlock restaurantsPageBlock,
        RestaurantSummaryBlock restaurantSummaryBlock
    ) {
        this.welcomeFirstTimeBlock = welcomeFirstTimeBlock;
        this.welcomeBackBlock = welcomeBackBlock;
        this.mainOptionsBlock = mainOptionsBlock;
        this.restaurantsPageBlock = restaurantsPageBlock;
        this.restaurantSummaryBlock = restaurantSummaryBlock;
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

    public void sendMainOptionsBlock(
        User user
    ) {
        mainOptionsBlock.send(user);
    }

    public void sendRestaurantsPageBlock(
        String userId,
        List<PageItem> restaurants,
        Integer totalItems,
        Boolean tooMuchItems,
        Boolean suggestionRestaurantsFineSearchRequired
    ) {
        restaurantsPageBlock.send(
            userId,
            restaurants,
            totalItems,
            tooMuchItems,
            suggestionRestaurantsFineSearchRequired
        );
    }

    public void sendRestaurantSummaryBlock(
        String userId,
        String restaurantId
    ) {
        restaurantSummaryBlock.send(userId, restaurantId);
    }
}
