package com.ironhack.restoranmanagementsystem.service;
import com.ironhack.restoranmanagementsystem.dto.request.TableCreateRequest;
import com.ironhack.restoranmanagementsystem.dto.response.TableResponse;
import com.ironhack.restoranmanagementsystem.entity.RestaurantTable;
import com.ironhack.restoranmanagementsystem.exception.ResourceNotFoundException;
import com.ironhack.restoranmanagementsystem.mapper.RestaurantTableMapper;
import com.ironhack.restoranmanagementsystem.repository.RestaurantTableRepository;
import org.springframework.stereotype.Service;
import java.util.List;
@Service
public class RestaurantTableService {
    private final RestaurantTableRepository restaurantTableRepository;

    public RestaurantTableService(RestaurantTableRepository restaurantTableRepository) {
        this.restaurantTableRepository = restaurantTableRepository;
    }
    public TableResponse createTable(TableCreateRequest request){
        RestaurantTable table=new RestaurantTable();
        table.setTableNumber(request.getTableNumber());
        table.setCapacity(request.getCapacity());
        table.setAvailable(request.getAvailable());
        RestaurantTable savedTable=restaurantTableRepository.save(table);
        return RestaurantTableMapper.toResponse(savedTable);
    }
    public TableResponse updateTable(Long id, TableCreateRequest request) {
        RestaurantTable table = restaurantTableRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Table not found: " + id));

        table.setTableNumber(request.getTableNumber());
        table.setCapacity(request.getCapacity());
        table.setAvailable(request.getAvailable());

        return RestaurantTableMapper.toResponse(restaurantTableRepository.save(table));
    }
    public void deleteTable(Long id){
        if(!restaurantTableRepository.existsById(id)){
            throw new ResourceNotFoundException("Table to be deleted not found");
        }        restaurantTableRepository.deleteById(id);
    }
    public TableResponse findTableNumber(int tableNumber){
        RestaurantTable table=restaurantTableRepository.findByTableNumber(tableNumber)
                .orElseThrow(()->new ResourceNotFoundException("Table number not found:"+tableNumber));
        return RestaurantTableMapper.toResponse(table);
    }
    public List<TableResponse>findAllAvailables(boolean status){
       List<RestaurantTable>tables=restaurantTableRepository.findAllByIsAvailable(status);
        return RestaurantTableMapper.toResponseList(tables);
    }
    public List<TableResponse> getTablesByMinCapacity(int capacity){
       List<RestaurantTable>tables=restaurantTableRepository.findByCapacityGreaterThanEqual(capacity);
        return RestaurantTableMapper.toResponseList(tables);
    }
}
