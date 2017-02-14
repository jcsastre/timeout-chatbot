package com.timeout.chatbot.block.template.generic.element;

import com.cloudinary.Cloudinary;
import com.github.messenger4j.send.buttons.Button;
import com.github.messenger4j.send.templates.GenericTemplate;
import com.timeout.chatbot.block.cloudinary.CloudinaryUrlBuilder;
import com.timeout.chatbot.configuration.TimeoutConfiguration;
import com.timeout.chatbot.domain.Image;
import com.timeout.chatbot.domain.Venue;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.List;

@Component
public class GenericTemplateWithSingleElementVenueBuilder {

    private final Cloudinary cloudinary;
    private final CloudinaryUrlBuilder cloudinaryUrlBuilder;
    private final TimeoutConfiguration timeoutConfiguration;

    @Autowired
    public GenericTemplateWithSingleElementVenueBuilder(
        Cloudinary cloudinary,
        CloudinaryUrlBuilder cloudinaryUrlBuilder,
        TimeoutConfiguration timeoutConfiguration
    ) {
        this.cloudinary = cloudinary;
        this.cloudinaryUrlBuilder = cloudinaryUrlBuilder;
        this.timeoutConfiguration = timeoutConfiguration;
    }

    public GenericTemplate build(
        Venue venue
    ) throws IOException, InterruptedException {

        final GenericTemplate.Builder genericTemplateBuilder = GenericTemplate.newBuilder();

        final GenericTemplate.Element.ListBuilder listBuilder = genericTemplateBuilder.addElements();

        final GenericTemplate.Element.Builder elementBuilder =
            listBuilder.addElement(
                venue.getName()
            );

        final String cloudinaryUrl = cloudinaryUrlBuilder.buildImageUrl(venue);
        if (cloudinaryUrl != null) {
            elementBuilder.imageUrl(cloudinaryUrl);
        } else {
            final List<Image> images = venue.getImages();
            if (images != null && images.size()>0) {
                elementBuilder.imageUrl(images.get(0).getUrl());
            } else {
                elementBuilder.imageUrl(timeoutConfiguration.getImageUrlPlacholder());
            }
        }

        final List<Button> buttons = buildButtonsForSingleElementInList(venue);
        if (buttons != null) {
            elementBuilder.buttons(buttons);
        }

        elementBuilder.toList().done();

        return genericTemplateBuilder.build();
    }

    public void addCommonButtonsToButtonsListBuilder(
        Button.ListBuilder buttonsListBuilder,
        String toWebsite
    ) {
        if (toWebsite != null) {
            buttonsListBuilder.addUrlButton(
                "See at Timeout",
                toWebsite
            ).toList();
        }

        buttonsListBuilder.addShareButton().toList();
    }

    public List<Button> buildButtonsForSingleElementInList(
        Venue venue
    ) {
        final Button.ListBuilder buttonsBuilder = Button.newListBuilder();

        final String phone = venue.getPhone();
        if (phone != null) {
            final String phoneNumber = "+34" + phone.replaceAll(" ","");
            buttonsBuilder.addCallButton(
                "Call " + phoneNumber,
                phoneNumber
            ).toList();
        }

        addCommonButtonsToButtonsListBuilder(buttonsBuilder, venue.getToWebsite());

        return buttonsBuilder.build();
    }

//    public String buildSubtitleForGenericTemplateElement(PageItem pageItem) {
//        String subtitle = pageItem.getSummary();
//        if (subtitle == null) {
//            subtitle = pageItem.getDescription();
//            if (subtitle == null) {
//                subtitle = pageItem.getAnnotation();
//            }
//        }
//
//        subtitle = HtmlUtils.htmlUnescape(subtitle);
//
//        if (subtitle != null && subtitle.length() > 80) {
//            subtitle = subtitle.substring(0, 77);
//            subtitle = subtitle + "...";
//        }
//
//        return subtitle;
//    }

//    public String buildGenericTemplateElementSubtitle(PageItem pageItem) {
//        StringBuilder sb = new StringBuilder();
//
//        final GraffittiCategorisation categorisation = pageItem.getGraffittiCategorisation();
//        if (categorisation != null) {
//            final GraffittiCategorisationPrimary categorisationPrimary =
//                categorisation.getGraffittiCategorisationPrimary();
//            if (categorisationPrimary != null) {
//                sb.append(categorisationPrimary.getName());
//            }
//
//            final GraffittiCategorisationSecondary categorisationSecondary =
//                categorisation.getGraffittiCategorisationSecondary();
//            if (categorisationSecondary != null) {
//                if (categorisationPrimary != null) {
//                    sb.append(" ");
//                }
//                sb.append("\ud83c\udf74");
//                sb.append(" ");
//                sb.append(categorisationSecondary.getName());
//            }
//        }
//
////        sb.append(pageItem.getGraffittiCategorisation().buildName());
//
//        if (pageItem.getDistance() != null) {
//            sb.append(" ");
//            sb.append("\uD83D\uDCCC");
//            sb.append(pageItem.getDistanceInMeters().toString());
//            sb.append(" m");
//        } else if (pageItem.getLocation() != null) {
//            sb.append(" ");
//            sb.append("\uD83D\uDCCC");
//            sb.append(" ");
//            sb.append(pageItem.getLocation());
//        }
//
//        if (sb.length() > 80) {
//            sb = sb.delete(80, sb.length());
//        }
//
//        return sb.toString();
//    }

//    public List<Button> buildButtonsForNotSingleElementInList(
//        PageItem pageItem
//    ) {
//        final Button.ListBuilder buttonsBuilder = Button.newListBuilder();
//

////
////        buttonsBuilder.addPostbackButton(
////            "Book",
////            new JSONObject()
////                .put("type", PayloadType.venues_book)
////                .put("restaurant_id", pageItem.getId())
////                .toString()
////        ).toList();
//
//        buttonsBuilder.addPostbackButton(
//            "More options",
//            new JSONObject()
//                .put("type", PayloadType.venue_more_options)
//                .put("venue_id", pageItem.getId())
//                .toString()
//        ).toList();
//
//        buttonsBuilder.addUrlButton(
//            "See at Timeout",
//            pageItem.getToWebsite()
//        ).toList();
//
//        buttonsBuilder.addShareButton().toList();
//
//        return
//            buttonsBuilder.build();
//    }
}
