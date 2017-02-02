package com.timeout.chatbot.services;

import com.timeout.chatbot.block.*;
import com.timeout.chatbot.block.DiscoverBlock;
import com.timeout.chatbot.block.SuggestionsBlock;
import com.timeout.chatbot.block.WelcomeBackBlock;
import com.timeout.chatbot.block.WelcomeFirstTimeBlock;
import com.timeout.chatbot.domain.user.User;
import com.timeout.chatbot.graffitti.response.search.page.PageItem;
import com.timeout.chatbot.session.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.validation.constraints.NotNull;
import java.util.List;

@Component
public class BlockService {

    private final WelcomeFirstTimeBlock welcomeFirstTimeBlock;
    private final SuggestionsBlock suggestionsBlock;
    private final DiscoverBlock discoverBlock;
    private final MostLovedBlock mostLovedBlock;
    private final WelcomeBackBlock welcomeBackBlock;
    private final MainOptionsBlock mainOptionsBlock;
    private final VenuesPageBlock venuesPageBlock;
    private final VenuesRemainingBlock venuesRemainingBlock;
    private final SubcategoriesQuickrepliesBlock subcategoriesQuickrepliesBlock;
    private final VenueSummaryBlock venueSummaryBlock;
    private final GeolocationAskBlock geolocationAskBlock;
    private final FilmsPageBlock filmsPageBlock;

    private final ErrorBlock errorBlock;

    @Autowired
    public BlockService(
        WelcomeFirstTimeBlock welcomeFirstTimeBlock,
        SuggestionsBlock suggestionsBlock,
        DiscoverBlock discoverBlock,
        MostLovedBlock mostLovedBlock, WelcomeBackBlock welcomeBackBlock,
        MainOptionsBlock mainOptionsBlock,
        VenuesPageBlock venuesPageBlock,
        VenuesRemainingBlock venuesRemainingBlock,
        SubcategoriesQuickrepliesBlock subcategoriesQuickrepliesBlock,
        VenueSummaryBlock venueSummaryBlock,
        GeolocationAskBlock geolocationAskBlock,
        FilmsPageBlock filmsPageBlock,
        ErrorBlock errorBlock
    ) {
        this.welcomeFirstTimeBlock = welcomeFirstTimeBlock;
        this.suggestionsBlock = suggestionsBlock;
        this.discoverBlock = discoverBlock;
        this.mostLovedBlock = mostLovedBlock;
        this.welcomeBackBlock = welcomeBackBlock;
        this.mainOptionsBlock = mainOptionsBlock;
        this.venuesPageBlock = venuesPageBlock;
        this.venuesRemainingBlock = venuesRemainingBlock;
        this.subcategoriesQuickrepliesBlock = subcategoriesQuickrepliesBlock;
        this.venueSummaryBlock = venueSummaryBlock;
        this.geolocationAskBlock = geolocationAskBlock;
        this.filmsPageBlock = filmsPageBlock;
        this.errorBlock = errorBlock;
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

    public void sendMostLovedBlock(
        User user
    ) {
        mostLovedBlock.send(
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
        Session session,
        List<PageItem> pageItems,
        String itemPluralName
    ) {
        venuesPageBlock.send(
            session,
            pageItems,
            itemPluralName
        );
    }

    public void sendVenuesRemainingBlock(
        Session session
//        String userMessengerId,
//        Integer remainingItems,
//        Boolean isWhereSet,
//        String itemPluralName,
//        Boolean isCategorySet,
//        String categorySingularName
    ) {
        venuesRemainingBlock.send(
            session
//            userMessengerId,
//            remainingItems,
//            isWhereSet,
//            itemPluralName,
//            isCategorySet,
//            categorySingularName
        );
    }

    public void sendVenueSummaryBlock(
        String userId,
        String restaurantId
    ) {
        venueSummaryBlock.send(userId, restaurantId);
    }

    public void sendSubcategoriesQuickrepliesBlock(
        Session session,
        Integer pageNumber
    ) throws Exception {
        subcategoriesQuickrepliesBlock.send(session, pageNumber);
    }

    public void sendGeolocationAskBlock(
        String userId
    ) {
        geolocationAskBlock.send(userId);
    }

    public void sendErrorBlock(
        User user
    ) {
        errorBlock.send(user);
    }
}
