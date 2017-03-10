package com.timeout.chatbot.services;

import com.github.messenger4j.exceptions.MessengerApiException;
import com.github.messenger4j.exceptions.MessengerIOException;
import com.timeout.chatbot.block.*;
import com.timeout.chatbot.block.deprecated.MainOptionsBlock;
import com.timeout.chatbot.block.deprecated.PhoneCallBlock;
import com.timeout.chatbot.block.deprecated.WhatsNewBlock;
import com.timeout.chatbot.block.state.submittingreview.BlockSubmittingReviewComment;
import com.timeout.chatbot.block.state.submittingreview.BlockSubmittingReviewAskConfirmation;
import com.timeout.chatbot.block.state.submittingreview.BlockSubmittingReviewRate;
import com.timeout.chatbot.domain.Venue;
import com.timeout.chatbot.domain.user.User;
import com.timeout.chatbot.graffitti.response.search.page.PageItem;
import com.timeout.chatbot.session.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.validation.constraints.NotNull;
import java.io.IOException;
import java.util.List;

@Component
public class BlockService {

    private final WelcomeFirstTimeBlock welcomeFirstTimeBlock;
    private final VersionInfoBlock versionInfoBlock;
    private final SearchSuggestionsBlock searchSuggestionsBlock;
    private final MostLovedBlock mostLovedBlock;
    private final WhatsNewBlock whatsNewBlock;
    private final WelcomeBackBlock welcomeBackBlock;
    private final MainOptionsBlock mainOptionsBlock;
    private final VenuesPageBlock venuesPageBlock;
    private final VenuesRemainingBlock venuesRemainingBlock;
    private final SubcategoriesQuickrepliesBlock subcategoriesQuickrepliesBlock;
    private final VenueSummaryBlock venueSummaryBlock;
    private final GeolocationAskBlock geolocationAskBlock;
    private final FilmsPageBlock filmsPageBlock;
    private final SeeVenueItemBlock seeVenueItemBlock;
    private final PhoneCallBlock phoneCallBlock;
    private final BlockSubmittingReviewRate blockSubmittingReviewRate;
    private final BlockSubmittingReviewComment blockSubmittingReviewComment;
    private final BlockSubmittingReviewAskConfirmation blockSubmittingReviewAskConfirmation;
    private final BlockError blockError;

    @Autowired
    public BlockService(
        WelcomeFirstTimeBlock welcomeFirstTimeBlock,
        VersionInfoBlock versionInfoBlock,
        SearchSuggestionsBlock searchSuggestionsBlock,
        MostLovedBlock mostLovedBlock,
        WhatsNewBlock whatsNewBlock,
        WelcomeBackBlock welcomeBackBlock,
        MainOptionsBlock mainOptionsBlock,
        VenuesPageBlock venuesPageBlock,
        VenuesRemainingBlock venuesRemainingBlock,
        SubcategoriesQuickrepliesBlock subcategoriesQuickrepliesBlock,
        VenueSummaryBlock venueSummaryBlock,
        GeolocationAskBlock geolocationAskBlock,
        FilmsPageBlock filmsPageBlock,
        SeeVenueItemBlock seeVenueItemBlock,
        PhoneCallBlock phoneCallBlock,
        BlockSubmittingReviewRate blockSubmittingReviewRate,
        BlockSubmittingReviewComment blockSubmittingReviewComment,
        BlockSubmittingReviewAskConfirmation blockSubmittingReviewAskConfirmation,
        BlockError blockError
    ) {
        this.welcomeFirstTimeBlock = welcomeFirstTimeBlock;
        this.versionInfoBlock = versionInfoBlock;
        this.searchSuggestionsBlock = searchSuggestionsBlock;
        this.mostLovedBlock = mostLovedBlock;
        this.whatsNewBlock = whatsNewBlock;
        this.welcomeBackBlock = welcomeBackBlock;
        this.mainOptionsBlock = mainOptionsBlock;
        this.venuesPageBlock = venuesPageBlock;
        this.venuesRemainingBlock = venuesRemainingBlock;
        this.subcategoriesQuickrepliesBlock = subcategoriesQuickrepliesBlock;
        this.venueSummaryBlock = venueSummaryBlock;
        this.geolocationAskBlock = geolocationAskBlock;
        this.filmsPageBlock = filmsPageBlock;
        this.seeVenueItemBlock = seeVenueItemBlock;
        this.phoneCallBlock = phoneCallBlock;
        this.blockSubmittingReviewRate = blockSubmittingReviewRate;
        this.blockSubmittingReviewComment = blockSubmittingReviewComment;
        this.blockSubmittingReviewAskConfirmation = blockSubmittingReviewAskConfirmation;
        this.blockError = blockError;
    }

    public void sendWelcomeFirstTimeBlock(
        Session session
    ) throws MessengerApiException, MessengerIOException {

        welcomeFirstTimeBlock.send(
            session.getUser().getMessengerId(),
            session.getFbUserProfile()
        );
    }

    public void sendVersionInfoBlock(
        String userId
    ) throws MessengerApiException, MessengerIOException {

        versionInfoBlock.send(userId);
    }

    public void sendWelcomeBackBlock(
        Session session
    ) {
        welcomeBackBlock.send(session);
    }

    public void sendSuggestionsBlock(
        Session session
    ) throws MessengerApiException, MessengerIOException {

        searchSuggestionsBlock.send(session);
    }

    public void sendMostLovedBlock(
        Session session
    ) throws MessengerApiException, MessengerIOException, IOException, InterruptedException {

        mostLovedBlock.send(session);
    }

    public void sendWhatsNewBlock(
        Session session
    ) throws MessengerApiException, MessengerIOException, IOException, InterruptedException {

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
    ) throws MessengerApiException, MessengerIOException, IOException, InterruptedException {

        venuesPageBlock.send(
            session,
            pageItems,
            itemPluralName
        );
    }

    public void sendVenuesRemainingBlock(
        Session session
    ) {
        venuesRemainingBlock.send(
            session
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

    public void sendGeolocationAskBlock(
        String userId
    ) {
        geolocationAskBlock.send(userId);
    }

    public void sendSeeVenueItemBlock(
        String userId,
        Venue venue
    ) throws MessengerApiException, MessengerIOException, IOException, InterruptedException {

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

        blockSubmittingReviewRate.send(userId);
    }

    public void sendSubmittingReviewCommentBlock(
        String userId
    ) throws MessengerApiException, MessengerIOException {

        blockSubmittingReviewComment.send(userId);
    }

    public void sendSubmittingReviewConfirmationBlock(
        Session session
    ) throws MessengerApiException, MessengerIOException {

        blockSubmittingReviewAskConfirmation.send(session);
    }

    public void sendErrorBlock(
        User user
    ) {
        blockError.send(user.getMessengerId());
    }
}
