package com.timeout.chatbot.graffitti.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.timeout.chatbot.graffitti.domain.response.venues.Venue;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Restaurant extends Venue {
}
