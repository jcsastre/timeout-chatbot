package com.timeout.chatbot.domain.session;

import com.timeout.chatbot.graffitti.domain.GraffittiType;
import com.timeout.chatbot.graffitti.domain.response.facets.CategoryPrimary;
import com.timeout.chatbot.graffitti.domain.response.facets.CategorySecondary;

import java.time.LocalDate;
import java.time.LocalTime;

public class SessionContextBag {

    private GraffittiType graffittiType;
    private CategoryPrimary categoryPrimary;
    private CategorySecondary categorySecondary;
    private Geolocation geolocation;
    private Double radius = 0.5D;
    private Integer reaminingItems;
    private Integer pageNumber;
    private BookingBag bookingBag;

    public SessionContextBag() {
        bookingBag = new BookingBag();
    }

    public GraffittiType getGraffittiType() {
        return graffittiType;
    }

    public void setGraffittiType(GraffittiType graffittiType) {
        this.graffittiType = graffittiType;
    }

    public CategoryPrimary getCategoryPrimary() {
        return categoryPrimary;
    }

    public void setCategoryPrimary(CategoryPrimary categoryPrimary) {
        this.categoryPrimary = categoryPrimary;
    }

    public CategorySecondary getCategorySecondary() {
        return categorySecondary;
    }

    public void setCategorySecondary(CategorySecondary categorySecondary) {
        this.categorySecondary = categorySecondary;
    }

    public Geolocation getGeolocation() {
        return geolocation;
    }

    public void setGeolocation(Geolocation geolocation) {
        this.geolocation = geolocation;
    }

    public Double getRadius() {
        return radius;
    }

    public void setRadius(Double radius) {
        this.radius = radius;
    }

    public Integer getReaminingItems() {
        return reaminingItems;
    }

    public void setReaminingItems(Integer reaminingItems) {
        this.reaminingItems = reaminingItems;
    }

    public Integer getPageNumber() {
        return pageNumber;
    }


    public void setPageNumber(Integer pageNumber) {
        this.pageNumber = pageNumber;
    }

    public BookingBag getBookingBag() {
        return bookingBag;
    }

    public void setBookingBag(BookingBag bookingBag) {
        this.bookingBag = bookingBag;
    }

    public class Geolocation {
        private Double latitude;
        private Double longitude;

        public Geolocation(Double latitude, Double longitude) {
            this.latitude = latitude;
            this.longitude = longitude;
        }

        public Double getLatitude() {
            return latitude;
        }

        public void setLatitude(Double latitude) {
            this.latitude = latitude;
        }

        public Double getLongitude() {
            return longitude;
        }

        public void setLongitude(Double longitude) {
            this.longitude = longitude;
        }
    }

    public class BookingBag {
        private Integer peopleCount;
        private LocalDate localDate;
        private LocalTime localTime;
        private String firstName;
        private String lastName;
        private String email;
        private String phone;

        public Integer getPeopleCount() {
            return peopleCount;
        }

        public void setPeopleCount(Integer peopleCount) {
            this.peopleCount = peopleCount;
        }

        public LocalDate getLocalDate() {
            return localDate;
        }

        public void setLocalDate(LocalDate localDate) {
            this.localDate = localDate;
        }

        public LocalTime getLocalTime() {
            return localTime;
        }

        public void setLocalTime(LocalTime localTime) {
            this.localTime = localTime;
        }

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

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }

        public String getPhone() {
            return phone;
        }

        public void setPhone(String phone) {
            this.phone = phone;
        }
    }
}
