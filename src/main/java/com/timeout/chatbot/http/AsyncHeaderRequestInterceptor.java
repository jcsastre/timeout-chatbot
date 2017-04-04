package com.timeout.chatbot.http;

import org.springframework.http.HttpRequest;
import org.springframework.http.client.AsyncClientHttpRequestExecution;
import org.springframework.http.client.AsyncClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.util.concurrent.ListenableFuture;

import java.io.IOException;

public class AsyncHeaderRequestInterceptor implements AsyncClientHttpRequestInterceptor {

    private final String headerName;
    private final String headerValue;

    public AsyncHeaderRequestInterceptor(String headerName, String headerValue) {
        this.headerName = headerName;
        this.headerValue = headerValue;
    }

    @Override
    public ListenableFuture<ClientHttpResponse> intercept(
        HttpRequest request,
        byte[] body,
        AsyncClientHttpRequestExecution asyncClientHttpRequestExecution
    ) throws IOException {

        request.getHeaders().set(headerName, headerValue);

        return
            asyncClientHttpRequestExecution.executeAsync(request, body);
    }
}
