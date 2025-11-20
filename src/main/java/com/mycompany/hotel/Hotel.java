package com.mycompany.hotel;
import com.mycompany.hotel.db.config.DatabaseConfig;
import com.mycompany.hotel.db.repository.HotelRepository;
import com.mycompany.hotel.db.repository.HotelRepositoryImpl;
import com.mycompany.hotel.http.HotelHttp;
import com.mycompany.hotel.service.HotelService;
import com.mycompany.hotel.service.HotelServiceImpl;

/**
 *
 * @author u611
 */
public class Hotel {

    public static void main(String[] args) {
        String jdbcUrl = "jdbc:postgresql://localhost:5432/hotel";
        String username = "user";
        String password = "password";
        
        var dbConfig = new DatabaseConfig(jdbcUrl, username, password);
        HotelRepository repository = new HotelRepositoryImpl(dbConfig.getDataSource());
        HotelService service = new HotelServiceImpl(repository);

        new HotelHttp(service);
    }
}
