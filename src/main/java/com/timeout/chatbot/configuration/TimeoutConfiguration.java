package com.timeout.chatbot.configuration;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@ConfigurationProperties(prefix = "timeout")
@Component
public class TimeoutConfiguration {

    private String site;
    private String locale;
    private String cityName;
    private String latitude;
    private String longitude;
    private String imageUrlPlacholder;

    public String getSite() {
        return site;
    }

    public void setSite(String site) {
        this.site = site;
    }

    public String getLocale() {
        return locale;
    }

    public void setLocale(String locale) {
        this.locale = locale;
    }

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getImageUrlPlacholder() {
        return imageUrlPlacholder;
    }

    public void setImageUrlPlacholder(String imageUrlPlacholder) {
        this.imageUrlPlacholder = imageUrlPlacholder;
    }
}
