package com.timeout.chatbot.graffitti.response.search.page;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.timeout.chatbot.graffitti.response.facets.v4.GraffittiFacetV4FacetNode;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class GraffittiSearchResponse {

    private Meta meta;

    @JsonProperty("body")
    private List<PageItem> pageItems;

    private List<GraffittiFacetV4FacetNode> facets;

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

    @JsonIgnoreProperties(ignoreUnknown = true)
    public class Meta {

        private String url;
        @JsonProperty("total_items")
        private Integer totalItems;
        private Page page;
        private String sort;

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }

        public Integer getTotalItems() {
            return totalItems;
        }

        public void setTotalItems(Integer totalItems) {
            this.totalItems = totalItems;
        }

        public Page getPage() {
            return page;
        }

        public void setPage(Page page) {
            this.page = page;
        }

        public String getSort() {
            return sort;
        }

        public void setSort(String sort) {
            this.sort = sort;
        }

        @JsonIgnoreProperties(ignoreUnknown = true)
        public class Page {

            private Integer number;
            @JsonProperty("requested_size")
            private Integer requestedSize;
            private Integer size;
            @JsonProperty("next_url")
            private String nextUrl;
            @JsonProperty("previous_url")
            private String previousUrl;

            public Integer getNumber() {
                return number;
            }

            public void setNumber(Integer number) {
                this.number = number;
            }

            public Integer getRequestedSize() {
                return requestedSize;
            }

            public void setRequestedSize(Integer requestedSize) {
                this.requestedSize = requestedSize;
            }

            public Integer getSize() {
                return size;
            }

            public void setSize(Integer size) {
                this.size = size;
            }

            public String getNextUrl() {
                return nextUrl;
            }

            public void setNextUrl(String nextUrl) {
                this.nextUrl = nextUrl;
            }

            public String getPreviousUrl() {
                return previousUrl;
            }

            public void setPreviousUrl(String previousUrl) {
                this.previousUrl = previousUrl;
            }
        }
    }
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
//        GraffittiSearchResponse searchResponse =
//            restTemplate.getForObject(
//                "http://graffiti.timeout.com/v1/sites/es-barcelona/search?what=node-7083",
//                GraffittiSearchResponse.class
//            );
//
//        System.out.println("searchResponse: " + searchResponse);
//
////        final PageItem[] pageItems = searchResponse.getTileItems();
////        for (PageItem item : pageItems) {
////            System.out.println(item);
////        }
//    }

