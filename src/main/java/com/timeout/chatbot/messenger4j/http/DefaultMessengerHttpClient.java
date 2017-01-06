package com.timeout.chatbot.messenger4j.http;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;

import java.io.IOException;

/**
 * @author Max Grabenhorst
 * @since 0.6.0
 */
public final class DefaultMessengerHttpClient implements MessengerHttpClient {

    private static final String APPLICATION_JSON_CHARSET_UTF_8 = "application/json; charset=utf-8";

    private final OkHttpClient okHttp = new OkHttpClient();

    @Override
    public Response executePost(String url, String jsonBody) throws IOException {
        final MediaType jsonMediaType = MediaType.parse(APPLICATION_JSON_CHARSET_UTF_8);
        final RequestBody body = RequestBody.create(jsonMediaType, jsonBody);
        final Request request = new Request.Builder().url(url).post(body).build();
        try (okhttp3.Response response = this.okHttp.newCall(request).execute()) {
            return new Response(response.code(), response.body().string());
        }
    }
}