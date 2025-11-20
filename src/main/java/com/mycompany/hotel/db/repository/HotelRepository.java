package com.mycompany.hotel.db.repository;

import java.util.List;

import com.mycompany.hotel.db.entity.Hotel;

public interface HotelRepository {
    int save(Hotel book);

    Hotel findById(int id);
    
    List<Hotel> findAll();
    
    boolean update(Hotel book);
    
    void deleteById(int id);
}
