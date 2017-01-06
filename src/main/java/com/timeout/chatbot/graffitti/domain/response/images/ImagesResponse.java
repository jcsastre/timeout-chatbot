package com.timeout.chatbot.graffitti.domain.response.images;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.timeout.chatbot.graffitti.domain.response.search.page.Meta;
import com.timeout.chatbot.http.HeaderRequestInterceptor;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;

public class ImagesResponse {
    private Meta meta;

    @JsonProperty("body")
    private List<Image> images;

    public Meta getMeta() {
        return meta;
    }

    public void setMeta(Meta meta) {
        this.meta = meta;
    }

    public List<Image> getImages() {
        return images;
    }

    public void setImages(List<Image> images) {
        this.images = images;
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

        ImagesResponse imagesResponse =
            restTemplate.getForObject(
                "http://graffiti.timeout.com/v1/sites/uk-london/venues/244579/images",
                ImagesResponse.class
            );

        for (Image image : imagesResponse.getImages()) {
            System.out.println(image.getUrl());
        }

//        final PageItem[] pageItems = response.getPageItems();
//        for (PageItem item : pageItems) {
//            System.out.println(item);
//        }
    }
}
