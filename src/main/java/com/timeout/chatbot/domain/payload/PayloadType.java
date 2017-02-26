package com.timeout.chatbot.domain.payload;

public enum PayloadType {
    get_started,

    searching_ItemMoreOptions,
    searching_VenuesShowAreas,
    searching_ShowSubcategories,
    searching_VenuesSetNeighborhood,
    searching_SetSubcategory,
    searching_WhereEverywhere,
    searching_SeeMore,

    item_Back,
    item_Book,
    item_Photos,
    item_SubmitPhoto,
    item_SubmitReview,

    help,
    search_suggestions,
    discover,
    whats_new,
    most_loved,
    start_over,

    cancel,

    utterance,

    get_a_summary,

    subcategory_any,

    venues_more_info,
    set_geolocation,
    films_more_info,
    films_find_cinemas,
    no_see_at_timeout,
    booking_people_count,

    submitting_review_rate,
    submitting_review_no_comment,
    submitting_review_confirmation_yes,
    submitting_review_confirmation_no,

    booking_date,
    booking_time,
    booking_info_ok,
    booking_info_not_ok,
    booking_first_name_fb_ok,
    booking_first_name_fb_not_ok,
    booking_last_name_fb_ok,
    booking_last_name_fb_not_ok,
    booking_personal_info_ok,
    booking_personal_info_not_ok,
    booking_save_ok,
    booking_save_not_ok,


    temporaly_disabled;
}
