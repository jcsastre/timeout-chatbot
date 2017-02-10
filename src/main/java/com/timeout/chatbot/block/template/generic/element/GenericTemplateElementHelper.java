package com.timeout.chatbot.block.template.generic.element;

import com.github.messenger4j.send.buttons.Button;
import com.github.messenger4j.send.templates.GenericTemplate;
import com.timeout.chatbot.block.cloudinary.CloudinaryUrlBuilder;
import com.timeout.chatbot.domain.payload.PayloadType;
import com.timeout.chatbot.graffitti.domain.GraffittiType;
import com.timeout.chatbot.graffitti.response.common.categorisation.GraffittiCategorisation;
import com.timeout.chatbot.graffitti.response.search.page.PageItem;
import org.cloudinary.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.util.HtmlUtils;

import java.util.List;

@Component
public class GenericTemplateElementHelper {

    private final CloudinaryUrlBuilder cloudinaryUrlBuilder;

    @Autowired
    public GenericTemplateElementHelper(
        CloudinaryUrlBuilder cloudinaryUrlBuilder
    ) {
        this.cloudinaryUrlBuilder = cloudinaryUrlBuilder;
    }

    public void addNotSingleElementInList(
        GenericTemplate.Element.ListBuilder listBuilder,
        PageItem pageItem
    ) {
        final String type = pageItem.getType();
        final GraffittiCategorisation graffittiCategorisation = pageItem.getGraffittiCategorisation();

        final GenericTemplate.Element.Builder builder = listBuilder.addElement(pageItem.getName());

        final String subtitle = buildSubtitle(pageItem);
        if (subtitle != null && !subtitle.isEmpty()) {
            builder.subtitle(subtitle);
        }

        final String imageUrl = cloudinaryUrlBuilder.buildImageUrl(pageItem);
        if (imageUrl != null) {
            builder.imageUrl(imageUrl);
        }

        final List<Button> buttons = buildButtonsForNotSingleElementInList(pageItem);
        if (buttons != null) {
            builder.buttons(buttons);
        }

        builder.toList().done();
    }

    private String buildSubtitle(PageItem pageItem) {
        String subtitle = pageItem.getSummary();
        if (subtitle == null) {
            subtitle = pageItem.getDescription();
            if (subtitle == null) {
                subtitle = pageItem.getAnnotation();
            }
        }

        subtitle = HtmlUtils.htmlUnescape(subtitle);

        if (subtitle != null && subtitle.length() > 80) {
            subtitle = subtitle.substring(0, 77);
            subtitle = subtitle + "...";
        }

        return subtitle;
    }

    public List<Button> buildButtonsForNotSingleElementInList(
        PageItem pageItem
    ) {
        final Button.ListBuilder buttonsBuilder = Button.newListBuilder();

        final GraffittiType graffittiType = GraffittiType.fromString(pageItem.getType());

        switch (graffittiType) {

            case EVENT:
            case VENUE:
            case FILM:
                buttonsBuilder.addPostbackButton(
                    "More",
                    new JSONObject()
                        .put("type", PayloadType.item_more_options)
                        .put("item_type", pageItem.getType())
                        .put("item_id", pageItem.getId())
                        .toString()
                ).toList();
                break;

            case PAGE:
                buttonsBuilder.addUrlButton(
                    "Read at Timeout",
                    pageItem.getToWebsite()
                ).toList();
        }

//        addCommonButtonsToButtonsListBuilder(buttonsBuilder, pageItem.getToWebsite());

        return buttonsBuilder.build();
    }
}
