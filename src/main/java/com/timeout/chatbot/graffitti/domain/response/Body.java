package com.timeout.chatbot.graffitti.domain.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Body {
    private static final Logger log = LoggerFactory.getLogger(Body.class);

    private String id;
    private String name;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

//    private List<Item> items;
//
//    public List<Item> getItems() {
//        return items;
//    }
//
//    public void setItems(List<Item> items) {
//        this.items = items;
//    }

//    @Override
//    public String toString() {
//        StringBuffer sb = new StringBuffer();
//        sb.append("Body: [");
//        for (Item item : items) {
//            sb.append(item.toString() + ", ");
//        }
//        sb.append("]");
//
//        return sb.toString();
//    }
}
