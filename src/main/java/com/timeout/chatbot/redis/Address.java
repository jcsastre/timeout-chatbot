package com.timeout.chatbot.redis;

import java.io.Serializable;

public class Address implements Serializable {

    private String address1;
    private String address2;
    private String postCode;
    private String city;

    public Address(String address1, String address2, String postCode, String city) {
        this.address1 = address1;
        this.address2 = address2;
        this.postCode = postCode;
        this.city = city;
    }

    public String getAddress1() {
        return address1;
    }

    public String getAddress2() {
        return address2;
    }

    public String getPostCode() {
        return postCode;
    }

    public String getCity() {
        return city;
    }

    @Override
    public String toString() {
        return "Address [address1=" + address1 + "]";
    }
}
