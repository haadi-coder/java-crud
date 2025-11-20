package com.mycompany.hotel.db.repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

import com.mycompany.hotel.db.entity.Hotel;


public class HotelRepositoryImpl implements HotelRepository {    
    
    private final DataSource dataSource;
    
    private static final String INSERT_SQL = 
        "INSERT INTO hotel (title, rating) VALUES (?, ?)";
    private static final String SELECT_BY_ID_SQL = 
        "SELECT id, title, rating FROM hotel WHERE id = ?";
    private static final String SELECT_ALL_SQL = 
        "SELECT id, title, rating FROM hotel";
    private static final String UPDATE_SQL = 
        "UPDATE hotel SET title = ?, rating = ? WHERE id = ?";
    private static final String DELETE_SQL = 
        "DELETE FROM hotel WHERE id = ?";
    
    public HotelRepositoryImpl(DataSource dataSource) {
        this.dataSource = dataSource;
    }
    
    @Override
    public int save(Hotel hotel) {
        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(INSERT_SQL, 
                 Statement.RETURN_GENERATED_KEYS)) {
            
            stmt.setString(1, hotel.getTitle());
            stmt.setInt(2, hotel.getRating());
            
            int affectedRows = stmt.executeUpdate();
            
            if (affectedRows == 0) {
                throw new SQLException("Creating hotel failed, no rows affected.");
            }
            
            try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    return generatedKeys.getInt(1);
                } else {
                    throw new SQLException("Creating hotel failed, no ID obtained.");
                }
            }
            
        } catch (SQLException e) {
            throw new RuntimeException("Error saving hotel: " + e.getMessage(), e);
        }
    }
    
    @Override
    public Hotel findById(int id) {
        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(SELECT_BY_ID_SQL)) {
            
            stmt.setInt(1, id);
            
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToHotel(rs);
                }

                return null;
            }
            
        } catch (SQLException e) {
            throw new RuntimeException("Error finding hotel by id: " + e.getMessage(), e);
        }
    }
    
    @Override
    public List<Hotel> findAll() {
        List<Hotel> hotels = new ArrayList<>();
        
        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(SELECT_ALL_SQL);
             ResultSet rs = stmt.executeQuery()) {
            
            while (rs.next()) {
                hotels.add(mapResultSetToHotel(rs));
            }
            
            return hotels;
            
        } catch (SQLException e) {
            throw new RuntimeException("Error finding all hotels: " + e.getMessage(), e);
        }
    }
    
    @Override
    public boolean update(Hotel hotel) {
        if (hotel.getId() == null) {
            throw new IllegalArgumentException("hotel id cannot be null for update");
        }
        
        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(UPDATE_SQL)) {
            
            stmt.setString(1, hotel.getTitle());
            stmt.setInt(2, hotel.getRating());
            stmt.setInt(3, hotel.getId());
            
            int affectedRows = stmt.executeUpdate();
            
            return affectedRows > 0;
            
        } catch (SQLException e) {
            throw new RuntimeException("Error updating hotel: " + e.getMessage(), e);
        }
    }
    
    @Override
    public void deleteById(int id) {
        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(DELETE_SQL)) {
            
            stmt.setInt(1, id);
            stmt.executeUpdate();
            
            
        } catch (SQLException e) {
            throw new RuntimeException("Error deleting hotel: " + e.getMessage(), e);
        }
    }
    
   
    private Hotel mapResultSetToHotel(ResultSet rs) throws SQLException {
        return new Hotel(
            rs.getInt("id"),
            rs.getString("title"),
            rs.getInt("rating")
        );
    }
}
