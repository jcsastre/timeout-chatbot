package com.timeout.chatbot.block.state.booking;

import com.timeout.chatbot.domain.user.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class BookingBlocksHelper {

    private final BookingPeopleCountBlock bookingPeopleCountBlock;
    private final BookingDateBlock bookingDateBlock;
    private final BookingTimeBlock bookingTimeBlock;
    private final BookingFirstnameConfirmationBlock bookingFirstnameConfirmationBlock;
    private final BookingLastnameConfirmationBlock bookingLastnameConfirmationBlock;

    @Autowired
    public BookingBlocksHelper(
        BookingPeopleCountBlock bookingPeopleCountBlock,
        BookingDateBlock bookingDateBlock,
        BookingTimeBlock bookingTimeBlock,
        BookingFirstnameConfirmationBlock bookingFirstnameConfirmationBlock,
        BookingLastnameConfirmationBlock bookingLastnameConfirmationBlock
    )
    {
        this.bookingPeopleCountBlock = bookingPeopleCountBlock;
        this.bookingDateBlock = bookingDateBlock;
        this.bookingTimeBlock = bookingTimeBlock;
        this.bookingFirstnameConfirmationBlock = bookingFirstnameConfirmationBlock;
        this.bookingLastnameConfirmationBlock = bookingLastnameConfirmationBlock;
    }

    public void sendBookingPeopleCountBlock(
        User user
    )
    {
        bookingPeopleCountBlock.send(user.getMessengerId());
    }

    public void sendBookingDateBlock(
        User user
    )
    {
        bookingDateBlock.send(user.getMessengerId());
    }

    public void sendBookingTimeBlock(
        User user
    )
    {
        bookingTimeBlock.send(user.getMessengerId());
    }

    public void sendBookingFirstnameConfirmationBlock(
        User user
    )
    {
        bookingFirstnameConfirmationBlock.send(
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
