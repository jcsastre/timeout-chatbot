package com.timeout.chatbot.domain.payload;

public enum PayloadType {
    get_started,

    help,
    suggestions,
    discover,
    whats_new,
    start_over,

    see_more,

    cancel,
    show_subcategories,
    set_subcategory,

//    utterance,

    venues_more_info,
    venues_book,
    venues_see_more,
    venues_set_secondary_category,
    restaurant_get_a_summary,
    restaurants_set_cuisine,
    set_geolocation,
    bar_get_a_summary,
    bar_book,
    films_more_info,
    films_find_cinemas,
    no_see_at_timeout,
    booking_people_count,
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
    booking_save_not_ok
}
