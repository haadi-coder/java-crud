package com.mycompany.hotel.service;

import java.util.List;

import com.mycompany.hotel.db.entity.Hotel;

public interface HotelService {
    int save(String title, int rating);

    Hotel findById(int id);

    List<Hotel> findAll();

    void update(Hotel hotel);

    void deleteById(int id);
}
