package com.timeout.chatbot.block;

import com.cloudinary.Cloudinary;
import com.cloudinary.Transformation;
import com.github.messenger4j.send.buttons.Button;
import com.github.messenger4j.send.templates.GenericTemplate;
import com.timeout.chatbot.block.types.BlockTypeVenueHelper;
import com.timeout.chatbot.block.types.EventListButtonsBuilder;
import com.timeout.chatbot.graffitti.domain.GraffittiType;
import com.timeout.chatbot.graffitti.response.search.page.PageItem;
import com.timeout.chatbot.graffitti.response.search.page.SearchResponse;
import com.timeout.chatbot.graffitti.urlbuilder.SearchUrlBuilder;
import com.timeout.chatbot.messenger4j.send.MessengerSendClientWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class MostLovedBlock {

    private final MessengerSendClientWrapper messengerSendClientWrapper;
    private final RestTemplate restTemplate;
    private final SearchUrlBuilder searchUrlBuilder;
    private final BlockTypeVenueHelper blockTypeVenueHelper;
    private final EventListButtonsBuilder eventListButtonsBuilder;
    private final Cloudinary cloudinary;

    @Autowired
    public MostLovedBlock(
        MessengerSendClientWrapper messengerSendClientWrapper,
        RestTemplate restTemplate,
        SearchUrlBuilder searchUrlBuilder,
        BlockTypeVenueHelper blockTypeVenueHelper,
        EventListButtonsBuilder eventListButtonsBuilder
    ) {
        this.messengerSendClientWrapper = messengerSendClientWrapper;
        this.restTemplate = restTemplate;
        this.searchUrlBuilder = searchUrlBuilder;
        this.blockTypeVenueHelper = blockTypeVenueHelper;
        this.eventListButtonsBuilder = eventListButtonsBuilder;

        Map config = new HashMap();
        config.put("cloud_name", "drdyx3lpb");
        config.put("api_key", "414418524479882");
        config.put("api_secret", "iY0YAPSOo-xztbHp-42UO2BS_t4");
        cloudinary = new Cloudinary(config);
    }

    public void send(
        String userId
    ) {
        messengerSendClientWrapper.sendTemplate(
            userId,
            buildGenericTemplate()
        );
    }

    private GenericTemplate buildGenericTemplate() {

        final GenericTemplate.Builder builder = GenericTemplate.newBuilder();
        final GenericTemplate.Element.ListBuilder listBuilder = builder.addElements();

        addElements(listBuilder);

        return builder.build();
    }

    private void addElements(
        GenericTemplate.Element.ListBuilder listBuilder
    ) {
        final SearchResponse searchResponse =
            restTemplate.getForObject(
                searchUrlBuilder.buildForMostLovedBlock().toUri(),
                SearchResponse.class
            );

        final List<PageItem> pageItems = searchResponse.getPageItems();
        for (PageItem pageItem : pageItems) {
            addElement(listBuilder, pageItem);
        }
    }

    // https://media.timeout.com/images/103669506/320/210/image.jpg
    // editorial_rating: 4
    // 1. blend
    // 2. put s3
    // 3. persist db
    //   3.1. key
    //     3.1.1 original image url
    //     3.1.2 title text
    //     3.1.3 timeout rating
    //     3.1.4 users rating
    //     3.1.2 location text
    //     3.1.2 xxx
    //   3.2. s3 url

    private void addElement(
        GenericTemplate.Element.ListBuilder listBuilder,
        PageItem pageItem
    ) {
        final GenericTemplate.Element.Builder builder = listBuilder.addElement(pageItem.getName());
//        final GenericTemplate.Element.Builder builder = listBuilder.addElement("title");

//        builder.itemUrl(pageItem.getToWebsite());

//        final String subtitle = buildSubtitle(pageItem);
//        if (subtitle != null && !subtitle.isEmpty()) {
//            builder.subtitle(subtitle);
//        }
//        builder.subtitle("subtitle");

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

    private String buildSubtitle(
        PageItem pageItem
    ) {
        final GraffittiType type = GraffittiType.fromString(pageItem.getType());

        if (type == GraffittiType.VENUE) {
            return blockTypeVenueHelper.buildGenericTemplateElementSubtitle(pageItem);
        } else if (type == GraffittiType.EVENT) {
            //TODO
            return null;
        } else if (type == GraffittiType.FILM) {
            //TODO
            return null;
        } else {
            //TODO
            return null;
        }
    }

    private String buildImageUrl(
        PageItem pageItem
    ) {
        String name = pageItem.getName();
        try {
            name =
                URLEncoder.encode(
                    name,
                    java.nio.charset.StandardCharsets.UTF_8.toString()
                );
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return null;
        }

        final PageItem.Image image = pageItem.getImage();
        if (image != null) {
            final String imageId = image.getId();
            if (imageId != null) {
//                String url = "http://res.cloudinary.com/drdyx3lpb/image/fetch/https://media.timeout.com/images/103669506/320/210/image.jpg";

                String url = "http://media.timeout.com/images/" + imageId + "/320/210/image.jpg";
//
                url =
                    cloudinary.url()
                        .transformation(
                            new Transformation()
                                .overlay("text:Arial_15_bold_left:" + name)
                                .gravity("south_west")
                                .x(0.04)
                                .y(0.30)
                        )
                        .type("fetch")
                        .generate(url);

                System.out.println(url);
                return url;

//                String url =
//                    "http://res.cloudinary.com/demo/image/fetch/l_text:Arial_15_bold_left:" +
//                    pageItem.getName() +
//                    ",g_south_west,x_0.04,y_0.30,co_rgb:FFFFFF/" +
//                    ;
//
//                return cloudinary.url();
            }
        }

        return null;
    }

    private List<Button> buildButtons(
        PageItem pageItem
    ) {
        final GraffittiType type = GraffittiType.fromString(pageItem.getType());

        if (type == GraffittiType.VENUE) {
            return blockTypeVenueHelper.buildButtonsList(pageItem);
        } else if (type == GraffittiType.EVENT) {
            return eventListButtonsBuilder.build(pageItem);
        } else if (type == GraffittiType.FILM) {
            //TODO
            return eventListButtonsBuilder.build(pageItem);
        } else {
            return null;
        }
    }
}
