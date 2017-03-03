package com.timeout.chatbot.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
@JsonIgnoreProperties(ignoreUnknown = true)
public class FbUserProfile {

    @JsonProperty("first_name")
    @Column(name = "fb_first_name")
    private String firstName;

    @JsonProperty("last_name")
    @Column(name = "fb_last_name")
    private String lastName;

    @JsonProperty("profile_pic")
    @Column(name = "fb_profile_pic")
    private String profilePic;

    @Column(name = "fb_locale")
    private String locale;

    @Column(name = "fb_timezone")
    private String timezone;

    @Column(name = "fb_gender")
    private String gender;

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getProfilePic() {
        return profilePic;
    }

    public void setProfilePic(String profilePic) {
        this.profilePic = profilePic;
    }

    public String getLocale() {
        return locale;
    }

    public void setLocale(String locale) {
        this.locale = locale;
    }

    public String getTimezone() {
        return timezone;
    }

    public void setTimezone(String timezone) {
        this.timezone = timezone;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    @Override
    public String toString() {
        return String.format(
            "FbUserProfile[firstName=%s, lastName=%s]",
            firstName, lastName
        );
    }
}
