package com.timeout.chatbot.domain.payload;

public enum PayloadItemState {

    BACK("item_back"),
    BOOK("item_book"),
    PHOTOS("item_photos"),
    SUBMIT_PHOTO("item_submit_photos"),
    SUBMIT_REVIEW("item_submit_review");

    private final String value;

    PayloadItemState(String value) {
        this.value = value;
    }

    public static PayloadItemState fromValue(String value) {
        if (value != null) {
            for (PayloadItemState payloadItemState : values()) {
                if (payloadItemState.value.equals(value)) {
                    return payloadItemState;
                }
            }
        }

        throw new IllegalArgumentException("Invalid PayloadItemState: " + value);
    }

    public String toValue() {
        return value;
    }
}
