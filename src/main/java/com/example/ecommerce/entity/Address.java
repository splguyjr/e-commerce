package com.example.ecommerce.entity;

import jakarta.persistence.Embeddable;

@Embeddable

public class Address {

    private String city;
    private String street;     //도로명
    private String zipcode;    //우편번호

    protected Address() {}

    public Address(String city, String street, String zipcode) {
        this.city = city;
        this.street = street;
        this.zipcode = zipcode;
    }


}
