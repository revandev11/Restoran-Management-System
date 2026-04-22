package com.ironhack.restoranmanagementsystem.mapper;

import com.ironhack.restoranmanagementsystem.dto.request.ReservationCreateRequest;
import com.ironhack.restoranmanagementsystem.dto.response.ReservationResponse;
import com.ironhack.restoranmanagementsystem.entity.Reservation;
import com.ironhack.restoranmanagementsystem.entity.RestaurantTable;

import java.util.List;

public class ReservationMapper {
    public static Reservation toEntity(ReservationCreateRequest request) {
        if (request == null) return null;

        Reservation reservation = new Reservation();
        reservation.setReservationTime(request.getReservationTime());
        reservation.setGuestCount(request.getGuestCount());

        RestaurantTable table = new RestaurantTable();
        table.setId(request.getRestaurantTableId());
        reservation.setRestaurantTable(table);

        return reservation;
    }

    public static ReservationResponse toResponse(Reservation reservation) {
        if (reservation == null) return null;

        ReservationResponse response = new ReservationResponse();
        response.setId(reservation.getId());
        response.setReservationTime(reservation.getReservationTime());
        response.setGuestCount(reservation.getGuestCount());
        response.setStatus(reservation.getStatus());
        response.setCreatedAt(reservation.getCreatedAt());

        if (reservation.getUser() != null) {
            response.setUserId(reservation.getUser().getId());
            response.setUserFullName(reservation.getUser().getFullName());
        }

        if (reservation.getRestaurantTable() != null) {
            response.setTableId(reservation.getRestaurantTable().getId());
            response.setTableNumber(reservation.getRestaurantTable().getTableNumber());
        }

        return response;
    }

    public static List<ReservationResponse> toResponseList(List<Reservation> reservations) {
        if (reservations == null) return List.of();

        return reservations.stream()
                .map(ReservationMapper::toResponse)
                .toList();
    }
}
