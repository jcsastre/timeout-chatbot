package com.timeout.chatbot.domain;

import com.timeout.chatbot.graffitti.response.common.UserRatingsSummary;
import com.timeout.chatbot.graffitti.response.common.categorisation.GraffittiCategorisation;
import com.timeout.chatbot.graffitti.response.common.categorisation.GraffittiCategorisationPrimary;
import com.timeout.chatbot.graffitti.response.common.categorisation.GraffittiCategorisationSecondary;
import com.timeout.chatbot.graffitti.response.images.GraffittiImage;
import com.timeout.chatbot.graffitti.response.venue.GraffittiVenueResponse;

import java.util.ArrayList;
import java.util.List;

public class Venue {

    private String id;
    private CategoryNode categoryPrimary;
    private CategoryNode categorySecondary;
    private String name;
    private String summary;
    private String location;
    private String annotation;
    private String toWebsite;
    private String phone;
    private Integer editorialRating;
    private Integer userRatingsCount;
    private Integer userRatingsAverage;
    private List<Image> images;

    public Venue(
        GraffittiVenueResponse.Body gvrb,
        List<GraffittiImage> lgi
    ) {
        id = gvrb.getId();

        final GraffittiCategorisation gc = gvrb.getGraffittiCategorisation();
        if (gc != null) {
            final GraffittiCategorisationPrimary gcp = gc.getGraffittiCategorisationPrimary();
            if (gcp != null) {
                categoryPrimary =
                    new CategoryNode(
                        gcp.getName(),
                        gcp.getTreeNodeId()
                    );
            }
            final GraffittiCategorisationSecondary gcs = gc.getGraffittiCategorisationSecondary();
            if (gcs != null) {
                categorySecondary =
                    new CategoryNode(
                        gcp.getName(),
                        gcp.getTreeNodeId()
                    );
            }
        }

        name = gvrb.getName();
        summary = gvrb.getSummary();
        location = gvrb.getLocation();
        annotation = gvrb.getAnnotation();
        toWebsite = gvrb.getToWebsite();
        phone = gvrb.getPhone();
        editorialRating = gvrb.getEditorialRating();

        final UserRatingsSummary userRatingsSummary = gvrb.getUserRatingsSummary();
        if (userRatingsSummary != null) {
            userRatingsCount = userRatingsSummary.getCount();
            userRatingsAverage = userRatingsSummary.getAverage();
        }

        if (lgi != null) {
            images = new ArrayList<>();
            for (GraffittiImage gi : lgi) {
                images.add(new Image(gi));
            }
        }
    }

    public String getId() {
        return id;
    }

    public CategoryNode getCategoryPrimary() {
        return categoryPrimary;
    }

    public CategoryNode getCategorySecondary() {
        return categorySecondary;
    }

    public String getName() {
        return name;
    }

    public String getSummary() {
        return summary;
    }

    public String getLocation() {
        return location;
    }

    public String getAnnotation() {
        return annotation;
    }

    public String getToWebsite() {
        return toWebsite;
    }

    public String getPhone() {
        return phone;
    }

    public Integer getEditorialRating() {
        return editorialRating;
    }

    public Integer getUserRatingsCount() {
        return userRatingsCount;
    }

    public Integer getUserRatingsAverage() {
        return userRatingsAverage;
    }

    public List<Image> getImages() {
        return images;
    }
}
