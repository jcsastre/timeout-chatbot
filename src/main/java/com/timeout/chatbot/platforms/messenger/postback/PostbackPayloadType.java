package com.timeout.chatbot.platforms.messenger.postback;

public enum PostbackPayloadType {
    FIND_RESTAURANTS,
    FIND_CAMPINGS,
    FIND_OFFERS,
    CAMPING_MORE_INFO,
    RESTAURANT_MORE_INFO,
    CAMPING_SEE_PHOTOS,
    CAMPING_BOOK;

    public static void main(String[] args) {
        System.out.println(CAMPING_BOOK.name());
        System.out.println(PostbackPayloadType.valueOf("CAMPING_BOOK"));
    }
}