package com.timeout.chatbot.graffitti.response.tiles;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.timeout.chatbot.graffitti.response.search.page.Meta;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class TilesResponse {
    private static final Logger log = LoggerFactory.getLogger(TilesResponse.class);

    private Meta meta;

    @JsonProperty("body")
    private List<TileItem> tileItems;

    public Meta getMeta() {
        return meta;
    }

    public void setMeta(Meta meta) {
        this.meta = meta;
    }

    public List<TileItem> getTileItems() {
        return tileItems;
    }

    public void setTileItems(List<TileItem> tileItems) {
        this.tileItems = tileItems;
    }

    @Override
    public String toString() {
        return
            "{" +
                "meta: " + meta.toString() +
            "}";
    }
//
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
//        TilesResponse tilesResponse =
//            restTemplate.getForObject(
//                "http://graffiti.timeout.com/v1/sites/es-barcelona/search?what=node-7083",
//                TilesResponse.class
//            );
//
//        System.out.println("tilesResponse: " + tilesResponse);
//
////        final PageItem[] tileItems = tilesResponse.getTileItems();
////        for (PageItem item : tileItems) {
////            System.out.println(item);
////        }
//    }
}
