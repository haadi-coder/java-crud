import com.mycompany.hotel.db.entity.Hotel;
import com.mycompany.hotel.db.repository.HotelRepository;
import com.mycompany.hotel.service.HotelNotFoundException;
import com.mycompany.hotel.service.HotelServiceImpl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.*;
import static org.mockito.ArgumentMatchers.argThat;

@ExtendWith(MockitoExtension.class)
class HotelServiceImplTest {

    @Mock
    private HotelRepository repository;

    @InjectMocks
    private HotelServiceImpl service;

    private Hotel hotel1;
    private Hotel hotel2;

    @BeforeEach
    void setUp() {
        hotel1 = new Hotel(1, "Redisson", 10);
        hotel2 = new Hotel(2, "Vatan", 6);
    }

    @Test
    void save_success() {
        when(repository.save(any(Hotel.class))).thenReturn(5);

        int id = service.save("Hilton", 8);

        assertEquals(5, id);
        verify(repository).save(argThat(h -> 
            h.getTitle().equals("Hilton") && h.getRating() == 8
        ));
    }

    @Test
    void findById_found() {
        when(repository.findById(1)).thenReturn(hotel1);

        Hotel found = service.findById(1);

        assertEquals(hotel1, found);
    }

    @Test
    void findById_notFound_throwsException() {
        when(repository.findById(999)).thenReturn(null);

        HotelNotFoundException ex = assertThrows(HotelNotFoundException.class, () -> service.findById(999));

        assertTrue(ex.getMessage().contains("999"));
    }

    @Test
    void findAll() {
        when(repository.findAll()).thenReturn(Arrays.asList(hotel1, hotel2));

        List<Hotel> all = service.findAll();

        assertEquals(2, all.size());
        assertTrue(all.contains(hotel1));
        assertTrue(all.contains(hotel2));
    }

    @Test
    void update_success() {
        Hotel updated = new Hotel(1, "Redisson Lux", 10);
        when(repository.findById(1)).thenReturn(hotel1);

        service.update(updated);

        verify(repository).update(updated);
    }

    @Test
    void update_notFound_throwsException() {
        Hotel updated = new Hotel(999, "Unknown", 1);
        when(repository.findById(999)).thenReturn(null);

        HotelNotFoundException ex = assertThrows(HotelNotFoundException.class, () -> service.update(updated));

        assertTrue(ex.getMessage().contains("999"));
    }

    @Test
    void update_nullId_throwsIllegalArgument() {
        Hotel hotel = new Hotel(null, "NoId", 5);

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () -> service.update(hotel));

        assertTrue(ex.getMessage().contains("id must not be null"));
    }

    @Test
    void deleteById_success() {
        when(repository.findById(1)).thenReturn(hotel1);

        service.deleteById(1);

        verify(repository).deleteById(1);
    }

    @Test
    void deleteById_notFound_throwsException() {
        when(repository.findById(999)).thenReturn(null);

        HotelNotFoundException ex = assertThrows(HotelNotFoundException.class, () -> service.deleteById(999));

        assertTrue(ex.getMessage().contains("999"));
        verify(repository, never()).deleteById(anyInt());
    }
}
