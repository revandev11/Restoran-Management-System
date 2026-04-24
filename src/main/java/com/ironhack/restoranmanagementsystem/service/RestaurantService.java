package com.ironhack.restoranmanagementsystem.service;
import com.ironhack.restoranmanagementsystem.dto.request.TableCreateRequest;
import com.ironhack.restoranmanagementsystem.dto.response.TableResponse;
import com.ironhack.restoranmanagementsystem.entity.RestaurantTable;
import com.ironhack.restoranmanagementsystem.mapper.RestaurantTableMapper;
import com.ironhack.restoranmanagementsystem.repository.RestaurantRepository;
import org.springframework.stereotype.Service;
import java.util.List;
@Service
public class RestaurantService {
    private final RestaurantRepository restaurantRepository;

    public RestaurantService(RestaurantRepository restaurantRepository) {
        this.restaurantRepository = restaurantRepository;
    }
    public TableResponse createTable(TableCreateRequest request){
        RestaurantTable table=new RestaurantTable();
        table.setTableNumber(request.getTableNumber());
        table.setCapacity(request.getCapacity());
        table.setAvailable(request.getAvailable());
        RestaurantTable savedTable=restaurantRepository.save(table);
        return RestaurantTableMapper.toResponse(savedTable);
    }
    public TableResponse updateTable(Long id,TableCreateRequest request){
        RestaurantTable table=restaurantRepository.findById(id)
                .orElseThrow(() ->new RuntimeException("Table not found"));
        table.setCapacity(request.getCapacity());
        table.setAvailable(request.getAvailable());
        RestaurantTable updatedTable=restaurantRepository.save(table);
        return RestaurantTableMapper.toResponse(updatedTable);
    }
    public void deleteTable(Long id){
        if(!restaurantRepository.existsById(id)){
            throw new RuntimeException("Table to be deleted not found");
        }        restaurantRepository.deleteById(id);
    }
    public TableResponse findTableNumber(int tableNumber){
        RestaurantTable table=restaurantRepository.findByTableNumber(tableNumber)
                .orElseThrow(()->new RuntimeException("Table number not found:"+tableNumber));
        return RestaurantTableMapper.toResponse(table);
    }
    public List<TableResponse>findAllAvailables(boolean status){
       List<RestaurantTable>tables=restaurantRepository.findAllByIsAvailable(status);
        return RestaurantTableMapper.toResponseList(tables);
    }
    public List<TableResponse> getTablesByMinCapacity(int capacity){
       List<RestaurantTable>tables=restaurantRepository.findByCapacityGreaterThanEqual(capacity);
        return RestaurantTableMapper.toResponseList(tables);
    }
}
