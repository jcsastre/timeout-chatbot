package com.timeout.chatbot.graffitti.response.search.page;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class SearchResponse {
    private static final Logger log = LoggerFactory.getLogger(SearchResponse.class);

    private Meta meta;

    @JsonProperty("body")
    private List<PageItem> pageItems;

    public Meta getMeta() {
        return meta;
    }

    public void setMeta(Meta meta) {
        this.meta = meta;
    }

    public List<PageItem> getPageItems() {
        return pageItems;
    }

    public void setPageItems(List<PageItem> pageItems) {
        this.pageItems = pageItems;
    }

    public Integer getNextPageNumber() {
        if (meta.getPage().getNextUrl() != null) {
            return meta.getPage().getNumber() + 1;
        }

        return null;
    }

    public Integer getRemainingItems() {
        final Integer nextPageNumber = getNextPageNumber();
        if (nextPageNumber != null) {
            return meta.getTotalItems() - (10 * (nextPageNumber - 1));
        }

        return 0;
    }

    @Override
    public String toString() {
        return
            "{" +
                "meta: " + meta.toString() +
            "}";
    }

//    public static void main(String[] args) {
//        List<ClientHttpRequestInterceptor> interceptors = new ArrayList<ClientHttpRequestInterceptor>();
//        interceptors.add(
//            new HeaderRequestInterceptor(
//                "Authorization",
//                "Bearer 8EOpBX2cpcZkCf3l7bBh476rzlpRtcKPzZVv4t1TGNMu24OIs1lhDMhUIVAil-9q"
//            )
//        );
//
//        RestTemplate restTemplate = new RestTemplate();
//        restTemplate.setInterceptors(interceptors);
//
//        SearchResponse searchResponse =
//            restTemplate.getForObject(
//                "http://graffiti.timeout.com/v1/sites/es-barcelona/search?what=node-7083",
//                SearchResponse.class
//            );
//
//        System.out.println("searchResponse: " + searchResponse);
//
////        final PageItem[] pageItems = searchResponse.getTileItems();
////        for (PageItem item : pageItems) {
////            System.out.println(item);
////        }
//    }
}
