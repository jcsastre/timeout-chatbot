package com.timeout.chatbot.graffitti.domain.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Meta {
    private static final Logger log = LoggerFactory.getLogger(Meta.class);

    private String url;
    private Integer total_items;
    private Page page;

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Integer getTotal_items() {
        return total_items;
    }

    public void setTotal_items(Integer total_items) {
        this.total_items = total_items;
    }

    public Page getPage() {
        return page;
    }

    public void setPage(Page page) {
        this.page = page;
    }

    @Override
    public String toString() {
        return
            "{" +
                "url= "+ url + ", " +
                "total_items=" + total_items + ", " +
                page.toString() +
            '}';
    }
}
