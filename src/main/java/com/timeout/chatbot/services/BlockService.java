package com.timeout.chatbot.services;

import com.github.messenger4j.exceptions.MessengerApiException;
import com.github.messenger4j.exceptions.MessengerIOException;
import com.timeout.chatbot.block.*;
import com.timeout.chatbot.block.deprecated.MainOptionsBlock;
import com.timeout.chatbot.block.deprecated.PhoneCallBlock;
import com.timeout.chatbot.block.deprecated.WhatsNewBlock;
import com.timeout.chatbot.block.state.submittingreview.BlockSubmittingReviewAskConfirmation;
import com.timeout.chatbot.block.state.submittingreview.BlockSubmittingReviewComment;
import com.timeout.chatbot.block.state.submittingreview.BlockSubmittingReviewRate;
import com.timeout.chatbot.domain.Venue;
import com.timeout.chatbot.domain.user.User;
import com.timeout.chatbot.session.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.validation.constraints.NotNull;
import java.io.IOException;

@Component
public class BlockService {

    private final WelcomeFirstTimeBlock welcomeFirstTimeBlock;
    private final WelcomeBackBlock welcomeBackBlock;
    private final DiscoverBlock discoverBlock;
    private final VersionInfoBlock versionInfoBlock;
    private final SearchSuggestionsBlock searchSuggestionsBlock;
    private final MostLovedBlock mostLovedBlock;
    private final WhatsNewBlock whatsNewBlock;
    private final MainOptionsBlock mainOptionsBlock;
    private final VenuesPageBlock venuesPageBlock;
    private final VenuesRemainingBlock venuesRemainingBlock;
    private final SubcategoriesQuickrepliesBlock subcategoriesQuickreplies;
    private final VenueSummaryBlock venueSummaryBlock;
    private final GeolocationAskBlock geolocationAskBlock;
    private final FilmsPageBlock filmsPageBlock;
    private final SeeVenueItemBlock seeVenueItemBlock;
    private final PhoneCallBlock phoneCallBlock;
    private final BlockSubmittingReviewRate submittingReviewRate;
    private final BlockSubmittingReviewComment submittingReviewComment;
    private final BlockSubmittingReviewAskConfirmation submittingReviewAskConfirmation;
    private final BlockAreas areas;
    private final BlockError error;


    @Autowired
    public BlockService(
        WelcomeFirstTimeBlock welcomeFirstTimeBlock,
        DiscoverBlock discoverBlock, VersionInfoBlock versionInfoBlock,
        SearchSuggestionsBlock searchSuggestionsBlock,
        MostLovedBlock mostLovedBlock,
        WhatsNewBlock whatsNewBlock,
        WelcomeBackBlock welcomeBackBlock,
        MainOptionsBlock mainOptionsBlock,
        VenuesPageBlock venuesPageBlock,
        VenuesRemainingBlock venuesRemainingBlock,
        SubcategoriesQuickrepliesBlock subcategoriesQuickreplies,
        VenueSummaryBlock venueSummaryBlock,
        GeolocationAskBlock geolocationAskBlock,
        FilmsPageBlock filmsPageBlock,
        SeeVenueItemBlock seeVenueItemBlock,
        PhoneCallBlock phoneCallBlock,
        BlockSubmittingReviewRate submittingReviewRate,
        BlockSubmittingReviewComment submittingReviewComment,
        BlockSubmittingReviewAskConfirmation submittingReviewAskConfirmation,
        BlockAreas areas, BlockError error
    ) {
        this.welcomeFirstTimeBlock = welcomeFirstTimeBlock;
        this.discoverBlock = discoverBlock;
        this.versionInfoBlock = versionInfoBlock;
        this.searchSuggestionsBlock = searchSuggestionsBlock;
        this.mostLovedBlock = mostLovedBlock;
        this.whatsNewBlock = whatsNewBlock;
        this.welcomeBackBlock = welcomeBackBlock;
        this.mainOptionsBlock = mainOptionsBlock;
        this.venuesPageBlock = venuesPageBlock;
        this.venuesRemainingBlock = venuesRemainingBlock;
        this.subcategoriesQuickreplies = subcategoriesQuickreplies;
        this.venueSummaryBlock = venueSummaryBlock;
        this.geolocationAskBlock = geolocationAskBlock;
        this.filmsPageBlock = filmsPageBlock;
        this.seeVenueItemBlock = seeVenueItemBlock;
        this.phoneCallBlock = phoneCallBlock;
        this.submittingReviewRate = submittingReviewRate;
        this.submittingReviewComment = submittingReviewComment;
        this.submittingReviewAskConfirmation = submittingReviewAskConfirmation;
        this.areas = areas;
        this.error = error;
    }

    public WelcomeFirstTimeBlock getWelcomeFirstTimeBlock() { return welcomeFirstTimeBlock; }

    public DiscoverBlock getDiscoverBlock() { return discoverBlock; }

    public WelcomeBackBlock getWelcomeBack() { return welcomeBackBlock; }

    public BlockSubmittingReviewRate getSubmittingReviewRate() {
        return submittingReviewRate;
    }

    public BlockSubmittingReviewComment getSubmittingReviewComment() {
        return submittingReviewComment;
    }

    public BlockSubmittingReviewAskConfirmation getSubmittingReviewAskConfirmation() {
        return  submittingReviewAskConfirmation;
    }

    public void sendVersionInfoBlock(
        String userId
    ) throws MessengerApiException, MessengerIOException {

        versionInfoBlock.send(userId);
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

    public VenuesPageBlock getVenuesPageBlock() { return  venuesPageBlock; }

    public VenuesRemainingBlock getVenuesRemainingBlock() { return venuesRemainingBlock; }

    public void sendVenueSummaryBlock(
        String userId,
        String restaurantId
    ) {
        venueSummaryBlock.send(userId, restaurantId);
    }

    public SubcategoriesQuickrepliesBlock getSubcategoriesQuickreplies() { return subcategoriesQuickreplies; }

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

    public BlockAreas getAreas() { return areas; }
    public BlockError getError() { return error; }
}
