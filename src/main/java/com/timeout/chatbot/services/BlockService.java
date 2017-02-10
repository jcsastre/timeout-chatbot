package com.timeout.chatbot.services;

import com.github.messenger4j.exceptions.MessengerApiException;
import com.github.messenger4j.exceptions.MessengerIOException;
import com.timeout.chatbot.block.*;
import com.timeout.chatbot.domain.Venue;
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
    private final AreasQuickrepliesBlock areasQuickrepliesBlock;
    private final VenueSummaryBlock venueSummaryBlock;
    private final GeolocationAskBlock geolocationAskBlock;
    private final FilmsPageBlock filmsPageBlock;
    private final SeeVenueItemBlock seeVenueItemBlock;
    private final PhoneCallBlock phoneCallBlock;
    private final ErrorBlock errorBlock;

    @Autowired
    public BlockService(
        WelcomeFirstTimeBlock welcomeFirstTimeBlock,
        SuggestionsBlock suggestionsBlock,
        DiscoverBlock discoverBlock,
        MostLovedBlock mostLovedBlock,
        WelcomeBackBlock welcomeBackBlock,
        MainOptionsBlock mainOptionsBlock,
        VenuesPageBlock venuesPageBlock,
        VenuesRemainingBlock venuesRemainingBlock,
        SubcategoriesQuickrepliesBlock subcategoriesQuickrepliesBlock,
        AreasQuickrepliesBlock areasQuickrepliesBlock,
        VenueSummaryBlock venueSummaryBlock,
        GeolocationAskBlock geolocationAskBlock,
        FilmsPageBlock filmsPageBlock,
        SeeVenueItemBlock seeVenueItemBlock,
        PhoneCallBlock phoneCallBlock,
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
        this.areasQuickrepliesBlock = areasQuickrepliesBlock;
        this.venueSummaryBlock = venueSummaryBlock;
        this.geolocationAskBlock = geolocationAskBlock;
        this.filmsPageBlock = filmsPageBlock;
        this.seeVenueItemBlock = seeVenueItemBlock;
        this.phoneCallBlock = phoneCallBlock;
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
        Session session
    ) throws MessengerApiException, MessengerIOException {
        suggestionsBlock.send(session);
    }

    public void sendDiscoverBlock(
        User user
    ) {
        discoverBlock.send(
            user.getMessengerId()
        );
    }

    public void sendMostLovedBlock(
        Session session
    ) throws MessengerApiException, MessengerIOException {

        mostLovedBlock.send(session);
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
    ) throws MessengerApiException, MessengerIOException {

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
    ) {
        subcategoriesQuickrepliesBlock.send(session, pageNumber);
    }

    public void sendNeighborhoodsQuickrepliesBlock(
        Session session,
        Integer pageNumber
    ) throws MessengerApiException, MessengerIOException {
        areasQuickrepliesBlock.send(session, pageNumber);
    }

    public void sendGeolocationAskBlock(
        String userId
    ) {
        geolocationAskBlock.send(userId);
    }

    public void sendSeeVenueItemBlock(
        String userId,
        Venue venue
    ) throws MessengerApiException, MessengerIOException {

        seeVenueItemBlock.send(userId, venue);
    }

    public void sendPhoneCallBlock(
        String userId,
        String phoneNumber,
        String venueName
    ) throws MessengerApiException, MessengerIOException {

        phoneCallBlock.send(
            userId,
            phoneNumber,
            venueName
        );
    }

    public void sendErrorBlock(
        User user
    ) {
        errorBlock.send(user);
    }
}
