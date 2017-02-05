package com.timeout.chatbot.block.template.generic.element;

import com.cloudinary.Cloudinary;
import com.cloudinary.Transformation;
import com.github.messenger4j.send.buttons.Button;
import com.github.messenger4j.send.templates.GenericTemplate;
import com.timeout.chatbot.configuration.TimeoutConfiguration;
import com.timeout.chatbot.domain.payload.PayloadType;
import com.timeout.chatbot.graffitti.response.search.page.PageItem;
import org.cloudinary.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.HtmlUtils;

import java.util.List;

@Component
public abstract class GenericTemplateElementHelper {

    final Cloudinary cloudinary;
    final RestTemplate restTemplate;
    final TimeoutConfiguration timeoutConfiguration;

    @Autowired
    public GenericTemplateElementHelper(
        Cloudinary cloudinary,
        RestTemplate restTemplate,
        TimeoutConfiguration timeoutConfiguration
    ) {
        this.cloudinary = cloudinary;
        this.restTemplate = restTemplate;
        this.timeoutConfiguration = timeoutConfiguration;
    }

    public void addElement(
        GenericTemplate.Element.ListBuilder listBuilder,
        PageItem pageItem
    ) {
        final GenericTemplate.Element.Builder builder = listBuilder.addElement(pageItem.getName());

        final String subtitle = buildSubtitle(pageItem);
        if (subtitle != null && !subtitle.isEmpty()) {
            builder.subtitle(subtitle);
        }

        final String imageUrl = buildImageUrl(pageItem);
        if (imageUrl != null) {
            builder.imageUrl(imageUrl);
        }

        final List<Button> buttons = buildButtons(pageItem);
        if (buttons != null) {
            builder.buttons(buttons);
        }

        builder.toList().done();
    }

    public String buildSubtitle(PageItem pageItem) {
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

    public String buildImageUrl(PageItem pageItem) {
//        String name = pageItem.getName();
//        name = name.replace(" ", "%20");

        String url = timeoutConfiguration.getImageUrlPlacholder();

        final PageItem.Image image = pageItem.getImage();
        if (image != null) {
            final String imageId = image.getId();
            if (imageId != null) {
                url = "http://media.timeout.com/images/" + imageId + "/320/210/image.jpg";
            }
        }

        Transformation transformation = new Transformation();
        transformation =
            transformation.width(320).height(180).gravity("center").crop("crop").chain();
//        transformation =
//            transformation.overlay("see_at_timeout").gravity("north_east").x(0.02).y(0.08).chain();

        final Integer editorialRating = pageItem.getEditorialRating();
        if (editorialRating != null) {
            transformation =
                transformation.overlay("rs" + editorialRating + "5").gravity("south_west").x(0.02).y(0.08);
        }

        url =
            cloudinary.url()
                .transformation(transformation)
                .format("png")
                .type("fetch")
                .generate(url);

        return url;
    }

    public List<Button> buildButtons(
        PageItem pageItem
    ) {
        final Button.ListBuilder buttonsBuilder = Button.newListBuilder();

        buttonsBuilder.addPostbackButton(
            "More options",
            new JSONObject()
                .put("type", PayloadType.item_more_options)
                .put("item_type", pageItem.getType())
                .put("item_id", pageItem.getId())
                .toString()
        ).toList();

        buttonsBuilder.addUrlButton(
            "See at Timeout",
            pageItem.getToWebsite()
        ).toList();

        buttonsBuilder.addShareButton().toList();

        return
            buttonsBuilder.build();
    }
}
