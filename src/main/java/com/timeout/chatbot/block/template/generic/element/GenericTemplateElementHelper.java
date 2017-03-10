package com.timeout.chatbot.block.template.generic.element;

import com.github.messenger4j.send.buttons.Button;
import com.github.messenger4j.send.templates.GenericTemplate;
import com.timeout.chatbot.block.cloudinary.CloudinaryUrlBuilder;
import com.timeout.chatbot.configuration.TimeoutConfiguration;
import com.timeout.chatbot.domain.payload.PayloadType;
import com.timeout.chatbot.graffitti.domain.GraffittiType;
import com.timeout.chatbot.graffitti.response.common.categorisation.GraffittiCategorisation;
import com.timeout.chatbot.graffitti.response.search.page.PageItem;
import org.cloudinary.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.util.HtmlUtils;

import java.io.IOException;
import java.util.List;

@Component
public class GenericTemplateElementHelper {

    private final TimeoutConfiguration timeoutConfiguration;
    private final CloudinaryUrlBuilder cloudinaryUrlBuilder;

    @Autowired
    public GenericTemplateElementHelper(
        TimeoutConfiguration timeoutConfiguration,
        CloudinaryUrlBuilder cloudinaryUrlBuilder
    ) {
        this.timeoutConfiguration = timeoutConfiguration;
        this.cloudinaryUrlBuilder = cloudinaryUrlBuilder;
    }

    public void addNotSingleElementInList(
        GenericTemplate.Element.ListBuilder listBuilder,
        PageItem pageItem
    ) throws IOException, InterruptedException {
        final String type = pageItem.getType();
        final GraffittiCategorisation graffittiCategorisation = pageItem.getGraffittiCategorisation();

        final GenericTemplate.Element.Builder builder = listBuilder.addElement(pageItem.getName());

        final String subtitle = buildSubtitle(pageItem);
        if (subtitle != null && !subtitle.isEmpty()) {
            builder.subtitle(subtitle);
        }

        final String cloudinaryUrl = cloudinaryUrlBuilder.buildImageUrl(pageItem);
        if (cloudinaryUrl != null) {
            builder.imageUrl(cloudinaryUrl);
        } else {
            if (pageItem.getImage_url() != null) {
                builder.imageUrl(pageItem.getImage_url());
            } else {
                builder.imageUrl(timeoutConfiguration.getImageUrlPlacholder());
            }
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

        final GraffittiType graffittiType = GraffittiType.fromTypeAsString(pageItem.getType());

        switch (graffittiType) {

            case venue:
                buttonsBuilder.addPostbackButton(
                    "More options ...",
                    new JSONObject()
                        .put("type", PayloadType.searching_ItemMoreOptions)
                        .put("item_type", pageItem.getType())
                        .put("item_id", pageItem.getId())
                        .toString()
                ).toList();
                break;

            case event:
            case film:
                buttonsBuilder.addPostbackButton(
                    "\uD83D\uDEAB More",
                    new JSONObject()
                        .put("type", PayloadType.searching_ItemMoreOptions)
                        .put("item_type", pageItem.getType())
                        .put("item_id", pageItem.getId())
                        .toString()
                ).toList();
                break;

            case page:
                buttonsBuilder.addUrlButton(
                    "Read at Timeout",
                    pageItem.getToWebsite()
                ).toList();
        }

//        addCommonButtonsToButtonsListBuilder(buttonsBuilder, pageItem.getToWebsite());

        return buttonsBuilder.build();
    }
}
