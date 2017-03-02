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

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.TextStyle;
import java.util.List;
import java.util.Locale;

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

    private String escapeText(String text) {
        return
            text.replace(" ", "%20").replace(",", "%252C");
    }

    public String buildBookReceiptUrl(
        Integer numberOfPeople,
        LocalDate localDate,
        LocalTime localTime,
        String venueImageId,
        String venueName,
        String address,
        String city,
        String postCode
    ) throws UnsupportedEncodingException {
        // text = text.toUpperCase().replace(" ", "%20");

        Transformation transformation = new Transformation();

        // BOOK header
        transformation =
            transformation
                .overlay("text:Arial_22_bold:BOOK")
                .color("#fefefe")
                .gravity("north")
                .y(80) // previous 0.140
                .chain();

        // Table for N
        String tableText = "Table for " + numberOfPeople;
        transformation =
            transformation
                .overlay("text:Arial_18:" + escapeText(tableText))
                .color("#000000")
                .gravity("north")
                .y(140)
                .chain();

        // on Tuesday, 28 February
        String date =
            "on " +
            localDate.getDayOfWeek().getDisplayName(TextStyle.FULL, Locale.UK) +
            ", " +
            localDate.getDayOfMonth() +
            " " +
            localDate.getMonth().getDisplayName(TextStyle.FULL, Locale.UK);
        transformation =
            transformation
                .overlay("text:Arial_18:" + escapeText(date))
                .color("#000000")
                .gravity("north")
                .y(170)
                .chain();

        // at 20:00
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("H:mm");
        transformation =
            transformation
                .overlay("text:Arial_18:" + escapeText("at " + localTime.format(dtf)))
                .color("#000000")
                .gravity("north")
                .y(200)
                .chain();

        // Venue circled image
        transformation =
            transformation
                .gravity("north")
                .height(100)
                .overlay("timages:"+venueImageId+":image.jpg")
                .radius("max")
                .width(100)
                .y(250)
                .crop("crop")
                .chain();

        // Restaurant name
        transformation =
            transformation
                .overlay("text:Arial_22_bold:" + escapeText(venueName))
                .color("#e1192c")
                .gravity("north")
                .y(365)
                .chain();

        // Address
        transformation =
            transformation
                .overlay("text:Arial_18:" + escapeText(address))
                .color("#000000")
                .gravity("north")
                .y(410)
                .chain();

        // City, Postcode
        String address2 = city + ", " + postCode;
        transformation =
            transformation
                .overlay("text:Arial_18:" + escapeText(address2))
                .color("#000000")
                .gravity("north")
                .y(440)
                .chain();

        return
            cloudinary.url()
                .transformation(transformation)
                .generate("booking_template_tj2o9p");
    }

    public String buildImageUrl(
        String categoryPrimaryName,
        String categorySecondaryName,
        Integer editorialRating,
        Integer userRatingsAverage,
        Integer userRatingsCount,
        GraffittiType graffittiType,
        String location,
        String imageId
    ) throws IOException, InterruptedException {

        Transformation transformation = buildBaseTransformation();

        // Categorisation
        String categorisationText = buildCategorisationText(categoryPrimaryName, categorySecondaryName);
        if (categorisationText != null) {
            transformation = chainCategorisationTransformation(transformation, categorisationText);
        }

        // Editorial rating
        if (editorialRating != null) {
            transformation = chainEditorialRatingTransformation(transformation, editorialRating);
        }

        // User ratings
        if (userRatingsAverage != null) {
            transformation =
                chainUserRatingsTransformation(
                    transformation,
                    editorialRating,
                    userRatingsAverage,
                    userRatingsCount
                );
        }

        // Location if venue
        if (graffittiType==GraffittiType.VENUE && location!=null) {
            transformation = chainLocation(transformation, location);
        }

        String cloudinaryUrl =
            cloudinary.url()
                .transformation(transformation)
                .format("png")
                .type("fetch")
                .generate(buildBaseImageUrl(imageId));

        if (checkUrlAvailability(cloudinaryUrl)) {
            return cloudinaryUrl;
        } else {
            return null;
        }
    }

    public String buildImageUrl(
        PageItem pageItem
    ) throws IOException, InterruptedException {

        // Categorisation
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

        // User ratings
        Integer userRatingsAverage = null;
        Integer userRatingsCount = null;
        final UserRatingsSummary userRatingsSummary = pageItem.getUserRatingsSummary();
        if (userRatingsSummary != null) {
            userRatingsAverage = userRatingsSummary.getAverage();
            userRatingsCount = userRatingsSummary.getCount();
        }

        // Image Id
        String imageId = null;
        final GraffittiImage image = pageItem.getImage();
        if (image != null) {
            imageId = image.getId();
        }

        return buildImageUrl(
            categoryPrimaryName,
            categorySecondaryName,
            pageItem.getEditorialRating(),
            userRatingsAverage,
            userRatingsCount,
            GraffittiType.fromString(pageItem.getType()),
            pageItem.getLocation(),
            imageId
        );
    }

    public String buildImageUrl(
        Venue venue
    ) throws IOException, InterruptedException {

        // Categorisation
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

        // Image Id
        String imageId = null;
        final List<Image> images = venue.getImages();
        if (images!=null && images.size()>0) {
            imageId = images.get(0).getId();
        }

        return buildImageUrl(
            categoryPrimaryName,
            categorySecondaryName,
            venue.getEditorialRating(),
            venue.getUserRatingsAverage(),
            venue.getUserRatingsCount(),
            GraffittiType.VENUE,
            venue.getLocation(),
            imageId
        );
    }

    private boolean checkUrlAvailability(
        String targetUrl
    ) throws IOException, InterruptedException {

        System.out.println("checkUrlAvailability: " + targetUrl);

//        Thread.sleep(5000);

        try {
            HttpURLConnection.setFollowRedirects(false);
            HttpURLConnection httpUrlConnection = (HttpURLConnection) new URL(targetUrl).openConnection();
            httpUrlConnection.setRequestMethod("HEAD");
            httpUrlConnection.setConnectTimeout(3000);

            return (httpUrlConnection.getResponseCode() == HttpURLConnection.HTTP_OK);
        } catch (IOException e) {
            return false;
        }

//        httpUrlConnection.setRequestMethod("HEAD");
//
//        int responseCode = httpUrlConnection.getResponseCode();
//        if (responseCode == HttpURLConnection.HTTP_OK) {
//            return true;
//        } else {
//            System.out.println("\tReceived code " + responseCode);
//            System.out.println("\t" + httpUrlConnection.getHeaderFields().toString());
//            System.out.println("\tWaiting " + 100 + "ms");
//            Thread.sleep(100);
//        }
//
//        responseCode = httpUrlConnection.getResponseCode();
//        if (responseCode == HttpURLConnection.HTTP_OK) {
//            return true;
//        } else {
//            System.out.println("\tReceived code " + responseCode);
//            System.out.println("\t" + httpUrlConnection.getHeaderFields().toString());
//            return false;
//        }

        //        boolean isAvailable = false;
//        int numberOfChecksDone = 0;
//        while (!isAvailable && numberOfChecksDone <=1) {
//            int responseCode = httpUrlConnection.getResponseCode();
//            if (responseCode == HttpURLConnection.HTTP_OK) {
//                isAvailable = true;
//            } else {
//                numberOfChecksDone++;
//                int numberOfMilis = numberOfChecksDone * 100;
//                System.out.println("Waiting " + numberOfMilis + "ms for " + targetUrl);
//                Thread.sleep(numberOfMilis);
//            }
//        }
//
//        return isAvailable;
    }

//    private String buildBaseImageUrl(
//        PageItem pageItem
//    ) {
//        String imageId = null;
//
//        final GraffittiImage image = pageItem.getImage();
//        if (image != null) {
//            imageId = image.getId();
//        }
//
//        return buildBaseImageUrl(imageId);
//    }

//    private String buildBaseImageUrl(
//        Venue venue
//    ) {
//        String imageId = null;
//
//        final List<Image> images = venue.getImages();
//        if (images!=null && images.size()>0) {
//            imageId = images.get(0).getId();
//        }
//
//        return buildBaseImageUrl(imageId);
//    }

    private String buildBaseImageUrl(
        String imageId
    ) {
        String baseImageUrl = timeoutConfiguration.getImageUrlPlacholder();

        if (imageId != null) {
            baseImageUrl = "https://media.timeout.com/images/" + imageId + "/image.jpg";
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

//    private Transformation chainTypeIconTransformation(
//        Transformation transformation,
//        GraffittiType graffittiType
//    ) {
//        String iconName = null;
//
//        switch (graffittiType) {
//
//            case VENUE:
//                iconName = "venue_icon_m8qzpkz";
//                break;
//
//            case EVENT:
//                iconName = "event_icon_burxucz";
//                break;
//
//            case FILM:
//                iconName = "film_icon_csr7j9z";
//                break;
//
//            case PAGE:
//                iconName = "page_icon_zdxsqzz";
//                break;
//        }
//
//        if (iconName != null) {
//            transformation =
//                transformation
//                    .overlay(iconName)
//                    .gravity("north_east")
//                    .x(0.04)
//                    .y(0.04)
//                    .chain();
//        }
//
//        return transformation;
//    }

    private Transformation chainLocation(Transformation transformation, String location) {
        location = location.replace(" ", "%20");

        transformation =
            transformation
                .overlay("venue_icon_m8qzpkz")
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
