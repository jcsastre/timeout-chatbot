package com.timeout.chatbot.services;

import com.github.messenger4j.exceptions.MessengerApiException;
import com.github.messenger4j.exceptions.MessengerIOException;
import com.timeout.chatbot.block.*;
import com.timeout.chatbot.block.deprecated.MainOptionsBlock;
import com.timeout.chatbot.block.deprecated.PhoneCallBlock;
import com.timeout.chatbot.block.deprecated.WhatsNewBlock;
import com.timeout.chatbot.block.state.submittingreview.SubmittingReviewCommentBlock;
import com.timeout.chatbot.block.state.submittingreview.SubmittingReviewConfirmationBlock;
import com.timeout.chatbot.block.state.submittingreview.SubmittingReviewRateBlock;
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
    private final VersionInfoBlock versionInfoBlock;
    private final SearchSuggestionsBlock searchSuggestionsBlock;
    private final DiscoverBlock discoverBlock;
    private final MostLovedBlock mostLovedBlock;
    private final WhatsNewBlock whatsNewBlock;
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
    private final SubmittingReviewRateBlock submittingReviewRateBlock;
    private final SubmittingReviewCommentBlock submittingReviewCommentBlock;
    private final SubmittingReviewConfirmationBlock submittingReviewConfirmationBlock;
    private final ErrorBlock errorBlock;

    @Autowired
    public BlockService(
        WelcomeFirstTimeBlock welcomeFirstTimeBlock,
        VersionInfoBlock versionInfoBlock,
        SearchSuggestionsBlock searchSuggestionsBlock,
        DiscoverBlock discoverBlock,
        MostLovedBlock mostLovedBlock,
        WhatsNewBlock whatsNewBlock,
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
        SubmittingReviewRateBlock submittingReviewRateBlock,
        SubmittingReviewCommentBlock submittingReviewCommentBlock,
        SubmittingReviewConfirmationBlock submittingReviewConfirmationBlock,
        ErrorBlock errorBlock
    ) {
        this.welcomeFirstTimeBlock = welcomeFirstTimeBlock;
        this.versionInfoBlock = versionInfoBlock;
        this.searchSuggestionsBlock = searchSuggestionsBlock;
        this.discoverBlock = discoverBlock;
        this.mostLovedBlock = mostLovedBlock;
        this.whatsNewBlock = whatsNewBlock;
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
        this.submittingReviewRateBlock = submittingReviewRateBlock;
        this.submittingReviewCommentBlock = submittingReviewCommentBlock;
        this.submittingReviewConfirmationBlock = submittingReviewConfirmationBlock;
        this.errorBlock = errorBlock;
    }

    public void sendWelcomeFirstTimeBlock(
        User user
    ) {
        welcomeFirstTimeBlock.send(user);
    }

    public void sendVersionInfoBlock(
        String userId
    ) throws MessengerApiException, MessengerIOException {

        versionInfoBlock.send(userId);
    }

    public void sendWelcomeBackBlock(
        User user
    ) {
        welcomeBackBlock.send(user);
    }

    public void sendSuggestionsBlock(
        Session session
    ) throws MessengerApiException, MessengerIOException {

        searchSuggestionsBlock.send(session);
    }

    public void sendDiscoverBlock(
        Session session
    ) throws MessengerApiException, MessengerIOException {

        discoverBlock.send(session);
    }

    public void sendMostLovedBlock(
        Session session
    ) throws MessengerApiException, MessengerIOException {

        mostLovedBlock.send(session);
    }

    public void sendWhatsNewBlock(
        Session session
    ) throws MessengerApiException, MessengerIOException {

        whatsNewBlock.send(session);
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
    ) throws MessengerApiException, MessengerIOException {

        subcategoriesQuickrepliesBlock.send(session, pageNumber);
    }

    public void sendAreasQuickrepliesBlock(
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

    public void sendSubmittingReviewRateBlock(
        String userId
    ) throws MessengerApiException, MessengerIOException {

        submittingReviewRateBlock.send(userId);
    }

    public void sendSubmittingReviewCommentBlock(
        String userId
    ) throws MessengerApiException, MessengerIOException {

        submittingReviewCommentBlock.send(userId);
    }

    public void sendSubmittingReviewConfirmationBlock(
        Session session
    ) throws MessengerApiException, MessengerIOException {

        submittingReviewConfirmationBlock.send(session);
    }

    public void sendErrorBlock(
        User user
    ) {
        errorBlock.send(user);
    }
}
