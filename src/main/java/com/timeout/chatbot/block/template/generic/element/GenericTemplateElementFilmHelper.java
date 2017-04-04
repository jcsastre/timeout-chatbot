package com.timeout.chatbot.block.template.generic.element;

import com.cloudinary.Cloudinary;
import com.cloudinary.Transformation;
import com.github.messenger4j.send.buttons.Button;
import com.github.messenger4j.send.templates.GenericTemplate;
import com.timeout.chatbot.configuration.TimeoutConfiguration;
import com.timeout.chatbot.domain.payload.PostbackPayload;
import com.timeout.chatbot.graffitti.response.common.UserRatingsSummary;
import com.timeout.chatbot.graffitti.response.common.categorisation.GraffittiCategorisation;
import com.timeout.chatbot.graffitti.response.common.categorisation.GraffittiCategorisationSecondary;
import com.timeout.chatbot.graffitti.response.images.GraffittiImage;
import com.timeout.chatbot.graffitti.response.search.page.PageItem;
import org.cloudinary.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class GenericTemplateElementFilmHelper {

    private final Cloudinary cloudinary;
    private final TimeoutConfiguration timeoutConfiguration;

    @Autowired
    public GenericTemplateElementFilmHelper(
        Cloudinary cloudinary,
        TimeoutConfiguration timeoutConfiguration
    ) {
        this.cloudinary = cloudinary;
        this.timeoutConfiguration = timeoutConfiguration;
    }

    public void addElement(
        GenericTemplate.Element.ListBuilder listBuilder,
        PageItem pageItem
    ) {
        final GenericTemplate.Element.Builder builder = listBuilder.addElement(pageItem.getName());

//        final String subtitle = buildSubtitle(pageItem);
//        if (subtitle != null && !subtitle.isEmpty()) {
//            builder.subtitle(subtitle);
//        }

        String subcategoryName = null;
        final GraffittiCategorisation categorisation = pageItem.getGraffittiCategorisation();
        if (categorisation != null) {
            final GraffittiCategorisationSecondary categorisationSecondary = categorisation.getGraffittiCategorisationSecondary();
            if (categorisationSecondary != null) {
                subcategoryName = categorisationSecondary.getName();
            }
        }

        final String imageUrl = buildImageUrl(
            pageItem.getImage(),
            pageItem.getEditorialRating(),
            pageItem.getUserRatingsSummary(),
            subcategoryName
        );
        if (imageUrl != null) {
            builder.imageUrl(imageUrl);
        }

        final List<Button> buttons = buildButtons(pageItem);
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
            transformation.overlay("film_icon_nwf5cn").gravity("north_west").x(0.04).y(0.04).chain();
        if (subcategoryText != null) {
            subcategoryText = subcategoryText.replace(" ", "%20");
            double y = 0.08;
            if (subcategoryText.contains("j") || subcategoryText.contains("g")) {
                y = y - 0.01;
            }
            transformation =
                transformation.overlay("text:Arial_32:"+subcategoryText).color("#B1B1B1").gravity("north_west").x(0.14).y(y).chain();
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

    public List<Button> buildButtons(
        PageItem pageItem
    ) {
        final Button.ListBuilder buttonsBuilder = Button.newListBuilder();

        buttonsBuilder.addPostbackButton(
            "More options",
            new JSONObject()
                .put("type", PostbackPayload.item_see)
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


//    public List<Button> buildButtonsForNotSingleElementInList(
//        PageItem pageItem
//    ) {
//        final GraffitiFilmResponse graffitiFilmResponse =
//            restTemplate.getForObject(
//                pageItem.getUrl(),
//                GraffitiFilmResponse.class
//            );
//
//        final String url = graffitiFilmResponse.getBody().getTrailer().getUrl();
//        if (url != null) {
//            final Button.ListBuilder buttonsBuilder = Button.newListBuilder();
//
//            buttonsBuilder.addUrlButton(
//                "See trailer",
//                url
//            ).toList();
//
//            buttonsBuilder.addPostbackButton(
//                "Find a cinema",
//                new JSONObject()
//                    .put("type", PayloadType.films_find_cinemas)
//                    .toString()
//            ).toList();
//
//            buttonsBuilder.addShareButton().toList();
//
//            return
//                buttonsBuilder.build();
//        }
//
//        return null;
//    }
}
