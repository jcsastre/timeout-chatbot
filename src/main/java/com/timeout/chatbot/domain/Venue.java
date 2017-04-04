package com.timeout.chatbot.domain;

import com.timeout.chatbot.graffitti.response.common.UserRatingsSummary;
import com.timeout.chatbot.graffitti.response.common.categorisation.GraffittiCategorisation;
import com.timeout.chatbot.graffitti.response.common.categorisation.GraffittiCategorisationPrimary;
import com.timeout.chatbot.graffitti.response.common.categorisation.GraffittiCategorisationSecondary;
import com.timeout.chatbot.graffitti.response.images.GraffittiImage;
import com.timeout.chatbot.graffitti.response.venue.GraffittiVenueResponse;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Venue implements Serializable {

    public String id;
    public CategoryNode categoryPrimary;
    public CategoryNode categorySecondary;
    public String name;
    public String summary;
    public String location;
    public String annotation;
    public String toWebsite;
    public String phone;
    public Integer editorialRating;
    public Integer userRatingsCount;
    public Integer userRatingsAverage;
    public List<Image> images;
    public String address1;
    public String city;
    public String postCode;

    public static Venue build(
        GraffittiVenueResponse.Body gvrb,
        List<GraffittiImage> lgi
    ) {
        Venue venue = new Venue();

        venue.id = gvrb.getId();

        final GraffittiCategorisation gc = gvrb.getGraffittiCategorisation();
        if (gc != null) {
            final GraffittiCategorisationPrimary gcp = gc.getGraffittiCategorisationPrimary();
            if (gcp != null) {
                venue.categoryPrimary =
                    new CategoryNode(
                        gcp.getName(),
                        gcp.getTreeNodeId()
                    );
            }
            final GraffittiCategorisationSecondary gcs = gc.getGraffittiCategorisationSecondary();
            if (gcs != null) {
                venue.categorySecondary =
                    new CategoryNode(
                        gcs.getName(),
                        gcs.getTreeNodeId()
                    );
            }
        }

        venue.name = gvrb.getName();
        venue.summary = gvrb.getSummary();
        venue.location = gvrb.getLocation();
        venue.annotation = gvrb.getAnnotation();
        venue.toWebsite = gvrb.getToWebsite();
        venue.phone = gvrb.getPhone();
        venue.editorialRating = gvrb.getEditorialRating();
        venue.address1 = gvrb.getAddress1();
        venue.city = gvrb.getCity();
        venue.postCode = gvrb.getPostCode();

        final UserRatingsSummary userRatingsSummary = gvrb.getUserRatingsSummary();
        if (userRatingsSummary != null) {
            venue.userRatingsCount = userRatingsSummary.getCount();
            venue.userRatingsAverage = userRatingsSummary.getAverage();
        }

        if (lgi != null) {
            venue.images = new ArrayList<>();
            for (GraffittiImage gi : lgi) {
                venue.images.add(
                    Image.build(gi)
                );
            }
        }

        return venue;
    }

    public Image getMainImage() {

        Image image = null;

        if (images != null) {
            if (images.size() > 0) {
                image = images.get(0);
            }
        }

        return image;
    }
}
