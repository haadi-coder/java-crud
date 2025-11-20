package com.mycompany.hotel.service;

public class HotelNotFoundException extends RuntimeException {
    public HotelNotFoundException(String message) {
        super(message);
    }
}
