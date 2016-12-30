package com.timeout.chatbot.graffitti.domain.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.timeout.chatbot.http.HeaderRequestInterceptor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Response {
    private static final Logger log = LoggerFactory.getLogger(Response.class);

    private Meta meta;

    @JsonProperty("body")
    private List<Item> items;

    public Meta getMeta() {
        return meta;
    }

    public void setMeta(Meta meta) {
        this.meta = meta;
    }

    public List<Item> getItems() {
        return items;
    }

    public void setItems(List<Item> items) {
        this.items = items;
    }

    @Override
    public String toString() {
        return
            "{" +
                "meta: " + meta.toString() +
            "}";
    }

    public static void main(String[] args) {
        List<ClientHttpRequestInterceptor> interceptors = new ArrayList<ClientHttpRequestInterceptor>();
        interceptors.add(
            new HeaderRequestInterceptor(
                "Authorization",
                "Bearer 8EOpBX2cpcZkCf3l7bBh476rzlpRtcKPzZVv4t1TGNMu24OIs1lhDMhUIVAil-9q"
            )
        );

        RestTemplate restTemplate = new RestTemplate();
        restTemplate.setInterceptors(interceptors);

        Response response =
            restTemplate.getForObject(
                "http://graffiti.timeout.com/v1/sites/es-barcelona/search?what=node-7083",
                Response.class
            );

        System.out.println("response: " + response);

//        final Item[] items = response.getItems();
//        for (Item item : items) {
//            System.out.println(item);
//        }
    }
}
