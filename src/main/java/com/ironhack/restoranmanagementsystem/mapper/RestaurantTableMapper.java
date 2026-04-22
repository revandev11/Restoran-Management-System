package com.ironhack.restoranmanagementsystem.mapper;

import com.ironhack.restoranmanagementsystem.dto.request.TableCreateRequest;
import com.ironhack.restoranmanagementsystem.dto.request.TableUpdateRequest;
import com.ironhack.restoranmanagementsystem.dto.response.TableResponse;
import com.ironhack.restoranmanagementsystem.entity.RestaurantTable;

import java.util.List;
import java.util.stream.Collectors;

public class RestaurantTableMapper {
    public static RestaurantTable toEntity(TableCreateRequest request) {
        if (request == null) return null;

        RestaurantTable table = new RestaurantTable();
        table.setTableNumber(request.getTableNumber());
        table.setCapacity(request.getCapacity());
        table.setAvailable(request.getAvailable());
        return table;
    }

    public static TableResponse toResponse(RestaurantTable entity) {
        if (entity == null) return null;

        TableResponse response = new TableResponse();
        response.setId(entity.getId());
        response.setTableNumber(entity.getTableNumber());
        response.setCapacity(entity.getCapacity());
        response.setAvailable(entity.isAvailable());
        return response;
    }

    public static List<TableResponse> toResponseList(List<RestaurantTable> tables) {
        if (tables == null) return null;
        return tables.stream()
                .map(RestaurantTableMapper::toResponse)
                .collect(Collectors.toList());
    }

    public static void updateEntity(TableUpdateRequest request, RestaurantTable table) {
        if (request == null || table == null) return;

        table.setCapacity(request.getCapacity());
        table.setAvailable(request.getAvailable());
    }
}
