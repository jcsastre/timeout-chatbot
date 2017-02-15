package com.timeout.chatbot.block.state.booking;

import com.timeout.chatbot.domain.user.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class BookingBlocksHelper {

    private final BlockBookingDate blockBookingDate;
    private final BlockBookingTime blockBookingTime;
    private final BlockBookingFirstnameConfirmation blockBookingFirstnameConfirmation;
    private final BookingLastnameConfirmationBlock bookingLastnameConfirmationBlock;

    @Autowired
    public BookingBlocksHelper(
        BlockBookingDate blockBookingDate,
        BlockBookingTime blockBookingTime,
        BlockBookingFirstnameConfirmation blockBookingFirstnameConfirmation,
        BookingLastnameConfirmationBlock bookingLastnameConfirmationBlock
    ) {
        this.blockBookingDate = blockBookingDate;
        this.blockBookingTime = blockBookingTime;
        this.blockBookingFirstnameConfirmation = blockBookingFirstnameConfirmation;
        this.bookingLastnameConfirmationBlock = bookingLastnameConfirmationBlock;
    }

    public void sendBookingDateBlock(
        User user
    )
    {
        blockBookingDate.send(user.getMessengerId());
    }

    public void sendBookingTimeBlock(
        User user
    )
    {
        blockBookingTime.send(user.getMessengerId());
    }

    public void sendBookingFirstnameConfirmationBlock(
        User user
    )
    {
        blockBookingFirstnameConfirmation.send(
            user.getMessengerId(),
            user.getFbUserProfile().getFirstName()
        );
    }

    public void sendBookingLastnameConfirmationBlock(
        User user
    )
    {
        bookingLastnameConfirmationBlock.send(
            user.getMessengerId(),
            user.getFbUserProfile().getLastName()
        );
    }
}
