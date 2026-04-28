package com.ironhack.restoranmanagementsystem;
import com.ironhack.restoranmanagementsystem.dto.request.TableCreateRequest;
import com.ironhack.restoranmanagementsystem.dto.response.TableResponse;
import com.ironhack.restoranmanagementsystem.entity.RestaurantTable;
import com.ironhack.restoranmanagementsystem.repository.RestaurantTableRepository;
import com.ironhack.restoranmanagementsystem.service.RestaurantTableService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.util.List;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RestaurantTableServiceTest {
    @Mock
    private RestaurantTableRepository restaurantTableRepository;
    @InjectMocks
    private RestaurantTableService restaurantTableService;
    private RestaurantTable table;
    @BeforeEach
    void setUp(){
        table=new RestaurantTable();
        table.setId(1L);
        table.setTableNumber(5);
        table.setCapacity(4);
        table.setAvailable(true);
    }
    @Test
    void createTable_Success() {
        TableCreateRequest request=new TableCreateRequest();
        request.setTableNumber(5);
        request.setCapacity(4);
        request.setAvailable(true);

        when(restaurantTableRepository.save(any(RestaurantTable.class))).thenReturn(table);
        TableResponse response=restaurantTableService.createTable(request);
        assertNotNull(response);
        assertEquals(5,response.getTableNumber());
        verify(restaurantTableRepository,times(1)).save(any(RestaurantTable.class));
    }
    @Test
    void findTableNumber_Success() {
        when(restaurantTableRepository.findByTableNumber(5)).thenReturn(Optional.of(table));
        TableResponse response=restaurantTableService.findTableNumber(5);
        assertNotNull(response);
        assertEquals(5,response.getTableNumber());
    }
    @Test
    void findTableNumber_NotFound_ThrowsException(){
        when(restaurantTableRepository.findByTableNumber(99)).thenReturn(Optional.empty());
        assertThrows(RuntimeException.class,() -> restaurantTableService.findTableNumber(99));
    }
    @Test
    void updateTable_Success() {
        TableCreateRequest updateRequest=new TableCreateRequest();
        updateRequest.setCapacity(6);
        updateRequest.setAvailable(false);

        when(restaurantTableRepository.findById(1L)).thenReturn(Optional.of(table));
        when(restaurantTableRepository.save(any(RestaurantTable.class))).thenReturn(table);
        TableResponse response=restaurantTableService.updateTable(1L,updateRequest);
        assertNotNull(response);
        verify(restaurantTableRepository).save(table);
    }
    @Test
    void deleteTable_Success(){
        when(restaurantTableRepository.existsById(1L)).thenReturn(true);
        restaurantTableService.deleteTable(1L);
        verify(restaurantTableRepository,times(1)).deleteById(1L);
    }
    @Test
    void deleteTable_NotFound_ThrowsException(){
        when(restaurantTableRepository.existsById(1L)).thenReturn(false);
        assertThrows(RuntimeException.class,() -> restaurantTableService.deleteTable(1L));
    }
    @Test
    void findAllAvailables_ReturnsList(){
        when(restaurantTableRepository.findAllByIsAvailable(true)).thenReturn(List.of(table));
        List<TableResponse>responses=restaurantTableService.findAllAvailables(true);
        assertFalse(responses.isEmpty());
        assertEquals(1,responses.size());
    }
    @Test
    void getTablesByMinCapacity_Success(){
        when(restaurantTableRepository.findByCapacityGreaterThanEqual(4)).thenReturn(List.of(table));
        List<TableResponse>responses=restaurantTableService.getTablesByMinCapacity(4);
        assertNotNull(responses);
        assertEquals(1,responses.size());
        verify(restaurantTableRepository).findByCapacityGreaterThanEqual(4);
    }
}