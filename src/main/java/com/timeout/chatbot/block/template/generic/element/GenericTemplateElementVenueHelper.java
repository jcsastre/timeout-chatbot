package com.timeout.chatbot.block.template.generic.element;

import com.cloudinary.Cloudinary;
import com.cloudinary.Transformation;
import com.github.messenger4j.send.buttons.Button;
import com.github.messenger4j.send.templates.GenericTemplate;
import com.timeout.chatbot.configuration.TimeoutConfiguration;
import com.timeout.chatbot.domain.payload.PayloadType;
import com.timeout.chatbot.graffitti.response.common.UserRatingsSummary;
import com.timeout.chatbot.graffitti.response.images.Image;
import com.timeout.chatbot.graffitti.response.search.page.PageItem;
import com.timeout.chatbot.graffitti.response.venues.GraffittiVenueResponse;
import org.cloudinary.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.util.HtmlUtils;

import java.util.List;

@Component
public class GenericTemplateElementVenueHelper {

    private final Cloudinary cloudinary;
    private final TimeoutConfiguration timeoutConfiguration;

    @Autowired
    public GenericTemplateElementVenueHelper(
        Cloudinary cloudinary,
        TimeoutConfiguration timeoutConfiguration
    ) {
        this.cloudinary = cloudinary;
        this.timeoutConfiguration = timeoutConfiguration;
    }

    public void addNotSingleElementInList(
        GenericTemplate.Element.ListBuilder listBuilder,
        PageItem pageItem
    ) {
        final GenericTemplate.Element.Builder builder = listBuilder.addElement(pageItem.getName());

        final String subtitle = buildSubtitle(pageItem);
        if (subtitle != null && !subtitle.isEmpty()) {
            builder.subtitle(subtitle);
        }

        final String imageUrl = buildImageUrl(
            pageItem.getImage(),
            pageItem.getLocation(),
            pageItem.getEditorialRating(),
            pageItem.getUserRatingsSummary()
        );
        if (imageUrl != null) {
            builder.imageUrl(imageUrl);
        }

        final List<Button> buttons = buildButtonsForNotSingleElementInList(pageItem);
        if (buttons != null) {
            builder.buttons(buttons);
        }

        builder.toList().done();
    }

    public void addSingleElementInList(
        GenericTemplate.Element.ListBuilder listBuilder,
        GraffittiVenueResponse graffittiVenueResponse
    ) {
        final GenericTemplate.Element.Builder builder =
            listBuilder.addElement(
                graffittiVenueResponse.getBody().getName()
            );

        final String imageUrl = buildImageUrl(
            graffittiVenueResponse.getBody().getImage(),
            graffittiVenueResponse.getBody().getLocation(),
            graffittiVenueResponse.getBody().getEditorialRating(),
            graffittiVenueResponse.getBody().getUserRatingsSummary()
        );
        if (imageUrl != null) {
            builder.imageUrl(imageUrl);
        }

        final List<Button> buttons = buildButtonsForSingleElementInList(graffittiVenueResponse);
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

    public String buildImageUrl(
        Image image,
        String location,
        Integer editorialRating,
        UserRatingsSummary userRatingsSummary
    ) {

        String url = timeoutConfiguration.getImageUrlPlacholder();
        if (image != null) {
            final String imageId = image.getId();
            if (imageId != null) {
                url = "http://media.timeout.com/images/" + imageId + "/image.jpg";
            }
        }

        Transformation transformation =
            new Transformation()
                .aspectRatio("191:100").crop("crop").chain()
                .width(764).crop("scale").chain()
                .overlay("overlay_black_bottom_gradient_loq19q").gravity("south").chain();

        // Location
        if (location != null) {
            location = location.replace(" ", "%20");
            location = location.replace(",", ".");
            double y = 0.07;
            if (location.contains("j")) {
                y = 0.06;
            }
            transformation =
                transformation
                    .overlay("location_vg9fjx").gravity("south_west").x(0.020).y(0.04).chain()
                    .overlay("text:Arial_32:"+location).color("#B1B1B1").gravity("south_west").x(0.08).y(y).chain();
        }

        // Editorial rating
        if (editorialRating != null) {
            transformation =
                transformation.overlay("rs"+editorialRating+"5v1").gravity("south_west").x(0.020).y(0.2).chain();
        }

        // User ratings
        if (userRatingsSummary != null) {
            final Integer average = userRatingsSummary.getAverage();
            if (average != null) {
                double x = 0.35;
                if (editorialRating == null) {
                    x =0.020;
                }
                transformation =
                    transformation.overlay("bs45v1").gravity("south_west").x(x).y(0.2).chain();
                final Integer count = userRatingsSummary.getCount();
                if (count != null) {
                    x = 0.625;
                    if (editorialRating == null) {
                        x = 0.295;
                    }
                    transformation =
                        transformation.overlay("text:Arial_34_bold:("+count+")").color("#FFFFFF").gravity("south_west").x(x).y(0.197).chain();
                }
            }
        }

        url =
            cloudinary.url()
                .transformation(transformation)
                .format("png")
                .type("fetch")
                .generate(url);

        System.out.println(url);

        return url;
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

    public List<Button> buildButtonsForNotSingleElementInList(
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

        addCommonButtonsToButtonsListBuilder(buttonsBuilder, pageItem.getToWebsite());

        return buttonsBuilder.build();
    }

    public List<Button> buildButtonsForSingleElementInList(
        GraffittiVenueResponse graffittiVenueResponse
    ) {
        final Button.ListBuilder buttonsBuilder = Button.newListBuilder();

        final String phone = graffittiVenueResponse.getBody().getPhone();
        if (phone != null) {
            final String phoneNumber = "+34" + phone.replaceAll(" ","");
            buttonsBuilder.addCallButton(
                "Call " + phoneNumber,
                phoneNumber
            ).toList();
        }

        addCommonButtonsToButtonsListBuilder(buttonsBuilder, graffittiVenueResponse.getBody().getToWebsite());

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
