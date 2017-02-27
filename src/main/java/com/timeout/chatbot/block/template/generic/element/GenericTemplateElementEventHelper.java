package com.timeout.chatbot.block.template.generic.element;

import com.cloudinary.Cloudinary;
import com.cloudinary.Transformation;
import com.github.messenger4j.send.buttons.Button;
import com.github.messenger4j.send.templates.GenericTemplate;
import com.timeout.chatbot.configuration.TimeoutConfiguration;
import com.timeout.chatbot.domain.payload.PayloadType;
import com.timeout.chatbot.graffitti.response.common.UserRatingsSummary;
import com.timeout.chatbot.graffitti.response.common.categorisation.GraffittiCategorisation;
import com.timeout.chatbot.graffitti.response.common.categorisation.GraffittiCategorisationSecondary;
import com.timeout.chatbot.graffitti.response.images.GraffittiImage;
import com.timeout.chatbot.graffitti.response.search.page.PageItem;
import com.timeout.chatbot.graffitti.response.venue.GraffittiVenueResponse;
import org.cloudinary.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class GenericTemplateElementEventHelper {

    private final Cloudinary cloudinary;
    private final TimeoutConfiguration timeoutConfiguration;
    private final GenericTemplateElementFilmHelper genericTemplateElementFilmHelper;

    @Autowired
    public GenericTemplateElementEventHelper(
        Cloudinary cloudinary,
        TimeoutConfiguration timeoutConfiguration,
        GenericTemplateElementFilmHelper genericTemplateElementFilmHelper) {
        this.cloudinary = cloudinary;
        this.timeoutConfiguration = timeoutConfiguration;
        this.genericTemplateElementFilmHelper = genericTemplateElementFilmHelper;
    }

    public void addNotSingleElementInList(
        GenericTemplate.Element.ListBuilder listBuilder,
        PageItem pageItem
    ) {
        final GenericTemplate.Element.Builder builder = listBuilder.addElement(pageItem.getName());

//        final String subtitle = buildSubtitle(pageItem);
//        if (subtitle != null && !subtitle.isEmpty()) {
//            builder.subtitle(subtitle);
//        }

        final String imageUrl = buildImageUrl(
            pageItem.getImage(),
            pageItem.getEditorialRating(),
            pageItem.getUserRatingsSummary(),
            pageItem.getGraffittiCategorisation().getGraffittiCategorisationPrimary().getName()
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

        String subcategoryName = null;
        final GraffittiCategorisation categorisation = graffittiVenueResponse.getBody().getGraffittiCategorisation();
        if (categorisation != null) {
            final GraffittiCategorisationSecondary categorisationSecondary = categorisation.getGraffittiCategorisationSecondary();
            if (categorisationSecondary != null) {
                subcategoryName = categorisationSecondary.getName();
            }
        }

        final String imageUrl = buildImageUrl(
            graffittiVenueResponse.getBody().getImage(),
            graffittiVenueResponse.getBody().getEditorialRating(),
            graffittiVenueResponse.getBody().getUserRatingsSummary(),
            subcategoryName
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

    public String buildImageUrl(
        GraffittiImage graffittiImage,
        Integer editorialRating,
        UserRatingsSummary userRatingsSummary,
        String subcategoryText
    ) {

        String url = timeoutConfiguration.getImageUrlPlacholder();
        if (graffittiImage != null) {
            final String imageId = graffittiImage.getId();
            if (imageId != null) {
                url = "http://media.timeout.com/images/" + imageId + "/image.jpg";
            }
        }

        Transformation transformation =
            new Transformation()
                .aspectRatio("191:100").crop("crop").chain()
                .width(764).crop("scale").chain()
                .overlay("overlay_black_top_gradient_geexo9").gravity("north").chain()
                .overlay("overlay_black_bottom_gradient_loq19q").gravity("south").chain();

        // Category icon + Subcategory text
        transformation =
            transformation.overlay("event_icon_nehhli").gravity("north_west").x(0.04).y(0.04).chain();
        if (subcategoryText != null) {
            subcategoryText = subcategoryText.replace(" ", "%20");
            double y = 0.08;
            if (subcategoryText.contains("j") || subcategoryText.contains("g")) {
                y = y - 0.01;
            }
            transformation =
                transformation.overlay("text:Arial_32:"+subcategoryText).color("#B1B1B1").gravity("north_west").x(0.12).y(y).chain();
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

//        // Type/Category icon + Subcategory
//        transformation =
//            transformation.overlay("event_icon_nehhli").gravity("south_east").x(0.020).y(0.04).chain();
//
//        if (categoryText != null) {
//            categoryText = categoryText.replace(" ", "%20");
//            double y = 0.06;
//            if (categoryText.contains("j") || categoryText.contains("g")) {
//                y = 0.05;
//            }
//            transformation =
//                transformation.overlay("text:Arial_32:"+categoryText).color("#B1B1B1").gravity("south_east").x(0.1).y(y).chain();
//        }

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
                .put("type", PayloadType.searching_ItemMoreOptions)
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
////                .put("restaurant_id", pageItem.getGraffittiId())
////                .toString()
////        ).toList();
//
//        buttonsBuilder.addPostbackButton(
//            "More options",
//            new JSONObject()
//                .put("type", PayloadType.venue_more_options)
//                .put("venue_id", pageItem.getGraffittiId())
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
