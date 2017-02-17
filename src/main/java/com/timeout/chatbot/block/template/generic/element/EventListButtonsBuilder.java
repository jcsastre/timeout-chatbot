package com.timeout.chatbot.block.template.generic.element;

import com.github.messenger4j.send.buttons.Button;
import com.timeout.chatbot.graffitti.response.search.page.PageItem;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class EventListButtonsBuilder {

    public List<Button> build(
        PageItem pageItem
    ) {
        final Button.ListBuilder buttonsBuilder = Button.newListBuilder();

        buttonsBuilder.addUrlButton(
            "See at Timeout",
            pageItem.getToWebsite()
        ).toList();

        return
            buttonsBuilder.build();
    }
}
