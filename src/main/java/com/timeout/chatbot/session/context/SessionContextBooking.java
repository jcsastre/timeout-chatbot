package com.timeout.chatbot.session.context;

import com.github.messenger4j.receive.events.AttachmentMessageEvent;
import com.github.messenger4j.receive.events.PostbackEvent;
import com.github.messenger4j.receive.events.QuickReplyMessageEvent;
import com.github.messenger4j.receive.events.TextMessageEvent;
import com.github.messenger4j.send.buttons.Button;
import com.github.messenger4j.send.templates.ButtonTemplate;
import com.timeout.chatbot.block.booking.BookingBlocksHelper;
import com.timeout.chatbot.domain.user.User;
import com.timeout.chatbot.domain.payload.PayloadType;
import com.timeout.chatbot.messenger4j.send.MessengerSendClientWrapper;
import com.timeout.chatbot.services.ApiAiService;
import org.apache.commons.validator.routines.EmailValidator;
import org.json.JSONObject;

import java.time.LocalDate;
import java.time.LocalTime;

public class SessionContextBooking extends SessionContext {

    private User user;
    private BookingState bookingState;

    private final MessengerSendClientWrapper messengerSendClientWrapper;
    private final BookingBlocksHelper bookingBlocksHelper;

    private Integer peopleCount;
    private LocalDate localDate;
    private LocalTime localTime;
    private String firstName;
    private String lastName;
    private String email;
    private String phone;

    public SessionContextBooking(
        User user,
        ApiAiService apiAiService,
        MessengerSendClientWrapper messengerSendClientWrapper,
        BookingBlocksHelper bookingBlocksHelper
    ) {
        super(user, apiAiService, messengerSendClientWrapper);

        this.user = user;
        this.messengerSendClientWrapper = messengerSendClientWrapper;
        this.bookingBlocksHelper = bookingBlocksHelper;

        this.bookingState = BookingState.UNKOWN;
    }

    public void startBooking() {

        sendTextMessage (
            "IMPORTANT: Because I'm a chatbot under development, the booking feature is just a simulation. " +
                "I'm not making a real booking :)"
        );

        this.bookingState = BookingState.PEOPLE_COUNT;

        bookingBlocksHelper.sendBookingPeopleCountBlock(user);
    }

    public void applyUtterance(
        String utterance
    ) {

        switch (bookingState) {

            case PEOPLE_COUNT:
                try {
                    Integer peopleCount = Integer.parseInt(utterance);
                    setPeopleCount(peopleCount);
                    bookingState = BookingState.DATE;
                    bookingBlocksHelper.sendBookingDateBlock(user);
                } catch (NumberFormatException e) {
                    sendTextMessage("Please, type a number");
                }
                break;

            case DATE:
                // TODO: submit to Api.ai
                // TODO: validate
                // setLocalDate(date);
                // bookingState = BookingState.TIME;
                // bookingBlocksHelper.sendBookingTimeBlock(user);
                break;

            case TIME:
                // TODO: submit to Api.ai
                // TODO: validate
                // setLocalTime(LocalTime.of(new Integer(time), 0));
                // bookingState = BookingState.CONFIRMATION_BOOKING_DETAILS;
                // askConfirmationInfo();
                break;

            case FIRST_NAME:
                setFirstName(utterance);
                bookingState = BookingState.LAST_NAME;
                askLastName();
                break;

            case LAST_NAME:
                setLastName(utterance);
                bookingState = BookingState.EMAIL;
                sendTextMessage("Please, type your email");
                break;

            case EMAIL:
                if (EmailValidator.getInstance().isValid(utterance)) {
                    setEmail(utterance);
                    bookingState = BookingState.PHONE;
                    sendTextMessage("Please, type your phone");
                } else {
                    sendTextMessage("Please, type a valid email");
                }
                break;

            case PHONE:
                setPhone(utterance);
                bookingState = BookingState.CONFIRMATION;
                askConfirmationPersonalInfo();
                break;

            default:
                sendTextMessage("Sorry, an error occurred.");
                break;
        }
    }

    private void askConfirmationPersonalInfo() {

        String msg =
            "Is that information correct?:\n" +
                "\n" +
                "First name: " + getFirstName() + "\n" +
                "Last name: " + getLastName() + "\n" +
                "Email: " + getEmail() + "\n" +
                "Phone number: " + getPhone();

        final ButtonTemplate yes = ButtonTemplate.newBuilder(
            msg,
            Button.newListBuilder()
                .addPostbackButton(
                    "Yes",
                    new JSONObject()
                        .put("type", PayloadType.booking_personal_info_ok)
                        .toString()
                ).toList()
                .addPostbackButton(
                    "No",
                    new JSONObject()
                        .put("type", PayloadType.booking_personal_info_not_ok)
                        .toString()
                ).toList()

                .build()
        ).build();

        messengerSendClientWrapper.sendTemplate(user.getMessengerId(), yes);
    }

    private void askConfirmationInfo() {

        String msg =
            "Is that booking information correct?:\n" +
                "\n" +
                "People: " + getPeopleCount() + "\n" +
                "Date: " + getLocalDate() + "\n" +
                "Time: " + getLocalTime();

        final ButtonTemplate yes = ButtonTemplate.newBuilder(
            msg,
            Button.newListBuilder()
                .addPostbackButton(
                    "Yes",
                    new JSONObject()
                        .put("type", PayloadType.booking_info_ok)
                        .toString()
                ).toList()
                .addPostbackButton(
                    "No",
                    new JSONObject()
                        .put("type", PayloadType.booking_info_not_ok)
                        .toString()
                ).toList()

                .build()
        ).build();

        messengerSendClientWrapper.sendTemplate(user.getMessengerId(), yes);
    }

    private void askFirstName() {
        final String firstName = user.getFbUserProfile().getFirstName();
        if (firstName != null) {
            bookingBlocksHelper.sendBookingFirstnameConfirmationBlock(user);
        } else {
            sendTextMessage("Please, type your first name");
        }
    }

    private void askLastName() {
        final String lastName = user.getFbUserProfile().getLastName();
        if (lastName != null) {
            bookingBlocksHelper.sendBookingLastnameConfirmationBlock(user);
        } else {
            sendTextMessage("Please, type your last name");
        }
    }

    public void applyPayload(
        String payloadAsJsonString
    ) {

        final JSONObject payloadAsJson = new JSONObject(payloadAsJsonString);

        try {
            PayloadType payloadType = PayloadType.valueOf(payloadAsJson.getString("type"));
            switch (payloadType) {

                case booking_people_count:
                    setPeopleCount(Integer.parseInt(payloadAsJson.getString("count")));
                    bookingState = BookingState.DATE;
                    bookingBlocksHelper.sendBookingDateBlock(user);
                    break;

                case booking_date:
                    final LocalDate date =
                        LocalDate.parse(
                            payloadAsJson.getString("date")
                        );
                    setLocalDate(date);
                    bookingState = BookingState.TIME;
                    bookingBlocksHelper.sendBookingTimeBlock(user);
                    break;

                case booking_time:
                    final String time = payloadAsJson.getString("time");
                    setLocalTime(LocalTime.of(new Integer(time), 0));
                    bookingState = BookingState.CONFIRMATION_BOOKING_DETAILS;
                    askConfirmationInfo();
                    break;

                case booking_info_ok:
                    bookingState = BookingState.FIRST_NAME;
                    askFirstName();
                    break;

                case booking_info_not_ok:
                    sendTextMessage("Ok, I've cancelled the booking process. You can start the booking again if you like");
                    break;

                case booking_first_name_fb_ok:
                    setFirstName(user.getFbUserProfile().getFirstName());
                    bookingState = BookingState.LAST_NAME;
                    askLastName();
                    break;

                case booking_first_name_fb_not_ok:
                    sendTextMessage("Please, type your first name?");
                    break;

                case booking_last_name_fb_ok:
                    setLastName(user.getFbUserProfile().getLastName());
                    bookingState = BookingState.EMAIL;
                    sendTextMessage("Please, type your email");
                    break;

                case booking_last_name_fb_not_ok:
                    sendTextMessage("Please, type your last name");
                    break;

                case booking_personal_info_ok:
                    bookingState = BookingState.SAVE_INFO;
                    messengerSendClientWrapper.sendTemplate(
                        user.getMessengerId(),
                        ButtonTemplate.newBuilder(
                            "Can I save this information for future bookings?",
                            Button.newListBuilder()
                                .addPostbackButton(
                                    "Yes",
                                    new JSONObject()
                                        .put("type", PayloadType.booking_save_ok)
                                        .toString()
                                ).toList()
                                .addPostbackButton(
                                    "No",
                                    new JSONObject()
                                        .put("type", PayloadType.booking_save_not_ok)
                                        .toString()
                                ).toList()
                                .build()
                        ).build()
                    );
                    break;

                case booking_personal_info_not_ok:
                    sendTextMessage("Ok, I've cancelled the booking process. You can start the booking again if you like");
                    break;

                case booking_save_ok:
                    sendTextMessage("Thanks, saved!");
                    //TODO: save info
                    break;

                case booking_save_not_ok:
                    sendTextMessage("No problem");
                    //TODO
                    break;

                default:
                    sendTextMessage("Sorry, an error occurred.");
                    break;
            }
        } catch(Exception e) {
            e.printStackTrace();
            sendTextMessage("Sorry, an error occurred.");
        }
    }

//    private void submitPeopleCount(String peopleCountAsString) {
//        try {
//            Integer peopleCount = Integer.parseInt(peopleCountAsString);
//            setPeopleCount(peopleCount);
//            bookingState = BookingState.DATE;
//            bookingBlocksHelper.sendBookingDateBlock(user);
//        } catch(NumberFormatException e) {
//            sendTextMessage("Please, type a number");
//        }
//    }

    public Integer getPeopleCount() {
        return peopleCount;
    }

    public void setPeopleCount(Integer peopleCount) {
        this.peopleCount = peopleCount;
    }

    public LocalDate getLocalDate() {
        return localDate;
    }

    public void setLocalDate(LocalDate localDate) {
        this.localDate = localDate;
    }

    public LocalTime getLocalTime() {
        return localTime;
    }

    public void setLocalTime(LocalTime localTime) {
        this.localTime = localTime;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public void sendTextMessage(String msg)
    {
        messengerSendClientWrapper.sendTextMessage(
            session.getUser().getMessengerId(),
            msg
        );
    }

    @Override
    public void handleTextMessageEvent(TextMessageEvent event) {

    }

    @Override
    public void handlePostbackEvent(PostbackEvent event) {

    }

    @Override
    public void handleQuickReplyMessageEvent(QuickReplyMessageEvent event) {

    }

    @Override
    public void handleAttachmentMessageEvent(AttachmentMessageEvent event) {

    }
}
