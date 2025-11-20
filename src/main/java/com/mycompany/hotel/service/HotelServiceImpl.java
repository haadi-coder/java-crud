package com.mycompany.hotel.service;

import java.util.List;

import com.mycompany.hotel.db.entity.Hotel;
import com.mycompany.hotel.db.repository.HotelRepository;

public class HotelServiceImpl implements HotelService {

    private final HotelRepository repository;

    public HotelServiceImpl(HotelRepository repository) {
        this.repository = repository;
    }

    @Override
    public int save(String title, int rating) {
        Hotel hotel = new Hotel(title, rating);
        return repository.save(hotel);
    }

    @Override
    public Hotel findById(int id) {
        Hotel hotel = repository.findById(id);
        if (hotel == null) {
            throw new HotelNotFoundException("Hotel with id " + id + " not found");
        }
        return hotel;
    }

    @Override
    public List<Hotel> findAll() {
        return repository.findAll();
    }

    @Override
    public void update(Hotel hotel) {
        if (hotel.getId() == null) {
            throw new IllegalArgumentException("Hotel id must not be null for update");
        }
        Hotel existing = repository.findById(hotel.getId());
        if (existing == null) {
            throw new HotelNotFoundException("Cannot update: hotel with id " + hotel.getId() + " not found");
        }
        repository.update(hotel);
    }

    @Override
    public void deleteById(int id) {
        findById(id);
        repository.deleteById(id);
    }
}
