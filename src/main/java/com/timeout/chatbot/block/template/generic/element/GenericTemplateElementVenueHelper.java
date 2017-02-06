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
                url = "http://media.timeout.com/images/" + imageId + "/image.jpg";
            }
        }

        Transformation transformation =
            new Transformation()
                .aspectRatio("191:100").crop("crop").chain()
                .width(764).crop("scale").chain()
                .overlay("overlay_black_bottom_gradient_loq19q").gravity("south").chain();

        // Location
        String location = pageItem.getLocation();
        if (location != null) {
            location = location.replace(" ", "%20");
            double yText = 0.07;
            if (location.contains("j")) {
                yText = 0.06;
            }
            transformation =
                transformation
                    .overlay("location_vg9fjx").gravity("south_west").x(0.020).y(0.04).chain()
                    .overlay("text:Arial_32:"+location).color("#B1B1B1").gravity("south_west").x(0.08).y(yText).chain();
        }

        // Editorial rating
        //        final Integer editorialRating = pageItem.getEditorialRating();
//        if (editorialRating != null) {
        transformation =
            transformation.overlay("rs35v1").gravity("south_west").x(0.020).y(0.2).chain();
//            transformation =
//                transformation.overlay("rs" + editorialRating + "5").gravity("center").x(0.02).y(0.08).chain();
//        }


        // User ratings
//        final PageItem.UserRatingsSummary userRatingsSummary = pageItem.getUserRatingsSummary();
//        if (userRatingsSummary != null) {
//            final Integer average = userRatingsSummary.getAverage();
//            if (average != null) {
                transformation =
                    transformation.overlay("bs45v1").gravity("south_west").x(0.35).y(0.2).chain();
                transformation =
                    transformation.overlay("text:Arial_36_bold:(23)").color("#FFFFFF").gravity("south_west").x(0.6).y(0.25).chain();

//            }
//        }


//        transformation =
//            transformation.width(320).height(180).gravity("center").crop("crop").chain();





        url =
            cloudinary.url()
                .transformation(transformation)
                .format("png")
                .type("fetch")
                .generate(url);

        System.out.println(url);

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
