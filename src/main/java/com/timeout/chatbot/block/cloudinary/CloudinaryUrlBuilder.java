package com.timeout.chatbot.block.cloudinary;

import com.cloudinary.Cloudinary;
import com.cloudinary.Transformation;
import com.timeout.chatbot.configuration.TimeoutConfiguration;
import com.timeout.chatbot.domain.CategoryNode;
import com.timeout.chatbot.domain.Image;
import com.timeout.chatbot.domain.Venue;
import com.timeout.chatbot.graffitti.domain.GraffittiType;
import com.timeout.chatbot.graffitti.response.common.UserRatingsSummary;
import com.timeout.chatbot.graffitti.response.common.categorisation.GraffittiCategorisation;
import com.timeout.chatbot.graffitti.response.common.categorisation.GraffittiCategorisationPrimary;
import com.timeout.chatbot.graffitti.response.common.categorisation.GraffittiCategorisationSecondary;
import com.timeout.chatbot.graffitti.response.images.GraffittiImage;
import com.timeout.chatbot.graffitti.response.search.page.PageItem;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class CloudinaryUrlBuilder {

    private final TimeoutConfiguration timeoutConfiguration;
    private final Cloudinary cloudinary;

    @Autowired
    public CloudinaryUrlBuilder(
        TimeoutConfiguration timeoutConfiguration,
        Cloudinary cloudinary
    ) {
        this.timeoutConfiguration = timeoutConfiguration;
        this.cloudinary = cloudinary;
    }

    public String buildImageUrl(
        PageItem pageItem
    ) {
        Transformation transformation = buildBaseTransformation();

        // Categorisation
        String categorisationText = buildCategorisationText(pageItem);
        if (categorisationText != null) {
            transformation = chainCategorisationTransformation(transformation, categorisationText);
        }

        // Type icon
        transformation = chainTypeIconTransformation(transformation, GraffittiType.fromString(pageItem.getType()));

        // Editorial rating
        final Integer editorialRating = pageItem.getEditorialRating();
        if (editorialRating != null) {
            transformation = chainEditorialRatingTransformation(transformation, editorialRating);
        }

        // User ratings
        final UserRatingsSummary userRatingsSummary = pageItem.getUserRatingsSummary();
        if (userRatingsSummary != null) {
            transformation =
                chainUserRatingsTransformation(
                    transformation,
                    editorialRating,
                    userRatingsSummary.getAverage(),
                    userRatingsSummary.getCount()
                );
        }

        // Location if venue
        if (pageItem.getType().equalsIgnoreCase("venue")) {
            String location = pageItem.getLocation();
            if (location != null) {
                transformation = chainLocation(transformation, location);
            }
        }

        String cloudinaryUrl =
            cloudinary.url()
                .transformation(transformation)
                .format("png")
                .type("fetch")
                .generate(buildBaseImageUrl(pageItem));

        System.out.println(cloudinaryUrl);

        return cloudinaryUrl;
    }

    public String buildImageUrl(
        Venue venue
    ) {
        Transformation transformation = buildBaseTransformation();

        // Categorisation
        String categorisationText = buildCategorisationText(venue);
        if (categorisationText != null) {
            transformation = chainCategorisationTransformation(transformation, categorisationText);
        }

        // Type icon
        transformation = chainTypeIconTransformation(transformation, GraffittiType.VENUE);

        // Editorial rating
        final Integer editorialRating = venue.getEditorialRating();
        if (editorialRating != null) {
            transformation = chainEditorialRatingTransformation(transformation, editorialRating);
        }

        // User ratings
        final Integer userRatingsAverage = venue.getUserRatingsAverage();
        if (userRatingsAverage != null) {
            transformation =
                chainUserRatingsTransformation(
                    transformation,
                    editorialRating,
                    userRatingsAverage,
                    venue.getUserRatingsCount()
                );
        }

        String cloudinaryUrl =
            cloudinary.url()
                .transformation(transformation)
                .format("png")
                .type("fetch")
                .generate(buildBaseImageUrl(venue));

        System.out.println(cloudinaryUrl);

        return cloudinaryUrl;
    }

    private String buildBaseImageUrl(
        PageItem pageItem
    ) {
        String imageId = null;

        final GraffittiImage image = pageItem.getImage();
        if (image != null) {
            imageId = image.getId();
        }

        return buildBaseImageUrl(imageId);
    }

    private String buildBaseImageUrl(
        Venue venue
    ) {
        String imageId = null;

        final List<Image> images = venue.getImages();
        if (images!=null && images.size()>0) {
            imageId = images.get(0).getId();
        }

        return buildBaseImageUrl(imageId);
    }

    private String buildBaseImageUrl(
        String imageId
    ) {
        String baseImageUrl = timeoutConfiguration.getImageUrlPlacholder();

        if (imageId != null) {
            baseImageUrl = "http://media.timeout.com/images/" + imageId + "/image.jpg";
        }

        return baseImageUrl;
    }

    private Transformation chainUserRatingsTransformation(
        Transformation transformation,
        Integer editorialRating,
        Integer userRatingsAverage,
        Integer userRatingsCount
    ) {

        if (userRatingsAverage != null) {
            double x = 0.35;
            if (editorialRating == null) {
                x = 0.020;
            }
            transformation =
                transformation.overlay("bs45v1").gravity("south_west").x(x).y(0.18).chain();

            if (userRatingsCount != null) {
                x = 0.630;
                if (editorialRating == null) {
                    x = 0.30;
                }
                transformation =
                    transformation.overlay("text:Arial_30_bold:(" + userRatingsCount + ")").color("#FFFFFF").gravity("south_west").x(x).y(0.18).chain();
            }
        }
        return transformation;
    }

    private Transformation chainEditorialRatingTransformation(Transformation transformation, Integer editorialRating) {
        return
            transformation
                .overlay("rs" + editorialRating + "5v1")
                .gravity("south_west")
                .x(0.020)
                .y(0.18)
                .chain();
    }

    private Transformation chainCategorisationTransformation(Transformation transformation, String categorySubcategoryText) {
        return
            transformation
                .overlay("text:Arial_26:" + categorySubcategoryText)
                .color("#c8c8c8")
                .gravity("north_west")
                .x(0.04)
                .y(0.04)
                .chain();
    }

    private Transformation chainTypeIconTransformation(
        Transformation transformation,
        GraffittiType graffittiType
    ) {
        String iconName = null;

        switch (graffittiType) {

            case VENUE:
                iconName = "venue_icon_m8qzpk";
                break;

            case EVENT:
                iconName = "event_icon_burxuc";
                break;

            case FILM:
                iconName = "film_icon_csr7j9";
                break;

            case PAGE:
                iconName = "page_icon_zdxsqz";
                break;
        }

        if (iconName != null) {
            transformation =
                transformation
                    .overlay(iconName)
                    .gravity("north_east")
                    .x(0.04)
                    .y(0.04)
                    .chain();
        }

        return transformation;
    }

    private Transformation chainLocation(Transformation transformation, String location) {
        transformation =
            transformation
                .overlay("venue_icon_m8qzpk")
                .gravity("south_west")
                .x(0.02)
                .y(0.04)
                .chain();

        double y = 0.060;
        if (
            location.contains("g") ||
            location.contains("j") ||
            location.contains("p") ||
            location.contains("y")
        ) {
            y = 0.050;
        }

        transformation =
            transformation.
                overlay("text:Arial_30:" + location)
                .color("#c8c8c8")
                .gravity("south_west")
                .x(0.07)
                .y(y)
                .chain();

        return transformation;
    }

    private Transformation buildBaseTransformation() {
        return new Transformation()
            .aspectRatio("191:100").crop("crop").chain()
            .width(764).crop("scale").chain()
            .overlay("overlay_black_top_gradient_geexo9").gravity("north").chain()
            .overlay("overlay_black_bottom_gradient_loq19q").gravity("south").chain();
    }

    private String buildCategorisationText(
        PageItem pageItem
    ) {
        String categoryPrimaryName = null;
        String categorySecondaryName = null;

        final GraffittiCategorisation categorisation = pageItem.getGraffittiCategorisation();
        if (categorisation != null) {
            final GraffittiCategorisationPrimary categorisationPrimary =
                categorisation.getGraffittiCategorisationPrimary();
            if (categorisationPrimary != null) {
                categoryPrimaryName = categorisationPrimary.getName();
                final GraffittiCategorisationSecondary categorisationSecondary =
                    categorisation.getGraffittiCategorisationSecondary();
                if (categorisationSecondary != null) {
                    categorySecondaryName = categorisationSecondary.getName();
                }
            }
        }

        return buildCategorisationText(categoryPrimaryName, categorySecondaryName);
    }

    private String buildCategorisationText(
        Venue venue
    ) {
        String categoryPrimaryName = null;
        String categorySecondaryName = null;

        final CategoryNode categoryPrimary = venue.getCategoryPrimary();
        if (categoryPrimary != null) {
            categoryPrimaryName = categoryPrimary.getName();
            if (categoryPrimaryName != null) {
                final CategoryNode categorySecondary = venue.getCategorySecondary();
                if (categorySecondary != null) {
                    categorySecondaryName = categorySecondary.getName();
                }
            }
        }

        return buildCategorisationText(categoryPrimaryName, categorySecondaryName);
    }

    private String buildCategorisationText(
        String categoryPrimaryName,
        String categorySecondaryName
    ) {
        String text = null;

        if (categoryPrimaryName != null) {
            text = categoryPrimaryName;
            if (categorySecondaryName != null) {
                text = text + " %252F " + categorySecondaryName;
            }
        }

        if (text != null) {
            text = text.toUpperCase().replace(" ", "%20");
        }

        return text;
    }
}
