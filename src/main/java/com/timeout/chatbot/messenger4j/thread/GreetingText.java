package com.timeout.chatbot.messenger4j.thread;

import javax.validation.constraints.NotNull;


public class GreetingText {
    private static final String FB_GRAPH_API_URL =
        "https://graph.facebook.com/v2.6/me/thread_settings?access_token==%s";

    private final String setting_type;
    private final String thread_state;
    private final String userDefinedPayload;
    private final String accessToken;

    public GreetingText(
        @NotNull String userDefinedPayload,
        @NotNull String accessToken
    ) {
        this.setting_type = "call_to_actions";
        this.thread_state = "new_thread";
        this.userDefinedPayload = userDefinedPayload;
        this.accessToken = accessToken;
    }

//    public void doPost() {
//        final DefaultMessengerHttpClient httpClient =
//            new DefaultMessengerHttpClient();
//
//        try {
//            final JsonParser jsonParser = new JsonParser();
//
//            final String requestUrl = String.format(FB_GRAPH_API_URL, accessToken);
//
//            final String jsonBody =
//                this.gson.toJson(messagingPayload);
//
//            final MessengerHttpClient.TilesResponse response = httpClient.executePost(requestUrl, jsonBody);
//
//            final JsonObject responseJsonObject = jsonParser.parse(response.getBody()).getAsJsonObject();
//
//            if (response.getStatusCode() >= 200 && response.getStatusCode() < 300) {
//                return MessengerResponse.fromJson(responseJsonObject);
//            } else {
//                throw MessengerApiException.fromJson(responseJsonObject);
//            }
//        } catch (IOException e) {
//            throw new MessengerIOException(e);
//        }
//        //TODO
//    }

    public void doDelete() {

    }
}
