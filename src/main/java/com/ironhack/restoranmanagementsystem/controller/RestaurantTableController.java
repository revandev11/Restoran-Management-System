package com.ironhack.restoranmanagementsystem.controller;
import com.ironhack.restoranmanagementsystem.dto.request.TableCreateRequest;
import com.ironhack.restoranmanagementsystem.dto.response.TableResponse;
import com.ironhack.restoranmanagementsystem.service.RestaurantTableService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/restaurant")
public class RestaurantTableController {
    public final RestaurantTableService restaurantTableService;
    public RestaurantTableController(RestaurantTableService restaurantTableService) {
        this.restaurantTableService = restaurantTableService;
    }
    @PostMapping
    public TableResponse createTable(@RequestBody @Valid TableCreateRequest request){
        return restaurantTableService.createTable(request);
    }
    @PutMapping("/{id}")
    public TableResponse updateTable(@PathVariable Long id, @RequestBody @Valid TableCreateRequest request){
        return restaurantTableService.updateTable(id, request);
    }
    @DeleteMapping("/{id}")
    public void deleteTable(@PathVariable Long id){
        restaurantTableService.deleteTable(id);}
    @GetMapping("/status")
    public List<TableResponse>findAllByAvailable(@RequestParam boolean status) {
        return restaurantTableService.findAllAvailables(status);
    }
    @GetMapping("/capacity")
    public List<TableResponse>getTablesByMinCapacity(@RequestParam int minCapacity){
        return restaurantTableService.getTablesByMinCapacity(minCapacity);
    }
    @GetMapping("/number/{tableNumber}")
    public TableResponse findByTableNumber(@PathVariable int tableNumber){
        return restaurantTableService.findTableNumber(tableNumber);
    }
}
