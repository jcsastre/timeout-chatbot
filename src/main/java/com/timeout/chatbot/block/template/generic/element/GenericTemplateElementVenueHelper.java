package com.timeout.chatbot.block.template.generic.element;

import com.cloudinary.Cloudinary;
import com.cloudinary.Transformation;
import com.timeout.chatbot.configuration.TimeoutConfiguration;
import com.timeout.chatbot.graffitti.response.search.page.PageItem;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class GenericTemplateElementVenueHelper extends GenericTemplateElementHelper {

    @Autowired
    public GenericTemplateElementVenueHelper(
        Cloudinary cloudinary,
        RestTemplate restTemplate,
        TimeoutConfiguration timeoutConfiguration
    ) {
        super(cloudinary, restTemplate, timeoutConfiguration);
    }

    public String buildImageUrl(PageItem pageItem) {

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

        final Integer editorialRating = pageItem.getEditorialRating();
        if (editorialRating != null) {
            transformation =
                transformation.overlay("rs" + editorialRating + "5").gravity("south_west").x(0.02).y(0.08);
        }

        final String location = pageItem.getLocation();
        if (location != null) {
            transformation =
                transformation.overlay("location").gravity("north_west").x(0.02).y(0.08);
        }

        url =
            cloudinary.url()
                .transformation(transformation)
                .format("png")
                .type("fetch")
                .generate(url);

        return url;
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

//    public List<Button> buildButtons(
//        PageItem pageItem
//    ) {
//        final Button.ListBuilder buttonsBuilder = Button.newListBuilder();
//
////        final String phone = pageItem.getPhone();
////        if (phone != null) {
////            final String curatedPhone = "+34" + phone.replaceAll(" ","");
////            buttonsBuilder.addCallButton(
////                "Call (" + curatedPhone +")",
////                curatedPhone
////            ).toList();
////        }
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
