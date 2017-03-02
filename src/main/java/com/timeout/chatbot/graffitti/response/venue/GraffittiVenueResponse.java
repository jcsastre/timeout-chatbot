package com.timeout.chatbot.graffitti.response.venue;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.timeout.chatbot.graffitti.response.common.UserRatingsSummary;
import com.timeout.chatbot.graffitti.response.common.categorisation.GraffittiCategorisation;
import com.timeout.chatbot.graffitti.response.images.GraffittiImage;

@JsonIgnoreProperties(ignoreUnknown = true)
public class GraffittiVenueResponse {

    private Body body;

    public Body getBody() {
        return body;
    }

    public void setBody(Body body) {
        this.body = body;
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public class Body {

        @JsonProperty("categorisation")
        private GraffittiCategorisation graffittiCategorisation;
        private String id;
        private String name;
        private String summary;
        private String url;
        private String image_url;
        private GraffittiImage image;
        private String location;
        private String annotation;
        @JsonProperty("to_website")
        private String toWebsite;
        private String phone;
        @JsonProperty("editorial_rating")
        private Integer editorialRating;
        @JsonProperty("user_ratings_summary")
        private UserRatingsSummary userRatingsSummary;
        private String address1;
        private String city;
        @JsonProperty("postcode")
        private String postCode;

        public GraffittiCategorisation getGraffittiCategorisation() {
            return graffittiCategorisation;
        }

        public void setGraffittiCategorisation(GraffittiCategorisation graffittiCategorisation) {
            this.graffittiCategorisation = graffittiCategorisation;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getSummary() {
            return summary;
        }

        public void setSummary(String summary) {
            this.summary = summary;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }

        public String getImage_url() {
            return image_url;
        }

        public void setImage_url(String image_url) {
            this.image_url = image_url;
        }

        public GraffittiImage getImage() {
            return image;
        }

        public void setImage(GraffittiImage image) {
            this.image = image;
        }

        public String getLocation() {
            return location;
        }

        public void setLocation(String location) {
            this.location = location;
        }

        public String getAnnotation() {
            return annotation;
        }

        public void setAnnotation(String annotation) {
            this.annotation = annotation;
        }

        public String getToWebsite() {
            return toWebsite;
        }

        public void setToWebsite(String toWebsite) {
            this.toWebsite = toWebsite;
        }

        public String getPhone() {
            return phone;
        }

        public void setPhone(String phone) {
            this.phone = phone;
        }

        public Integer getEditorialRating() {
            return editorialRating;
        }

        public void setEditorialRating(Integer editorialRating) {
            this.editorialRating = editorialRating;
        }

        public UserRatingsSummary getUserRatingsSummary() {
            return userRatingsSummary;
        }

        public void setUserRatingsSummary(UserRatingsSummary userRatingsSummary) {
            this.userRatingsSummary = userRatingsSummary;
        }

        public String getAddress1() {
            return address1;
        }

        public void setAddress1(String address1) {
            this.address1 = address1;
        }

        public String getCity() {
            return city;
        }

        public void setCity(String city) {
            this.city = city;
        }

        public String getPostCode() {
            return postCode;
        }

        public void setPostCode(String postCode) {
            this.postCode = postCode;
        }
    }
}
