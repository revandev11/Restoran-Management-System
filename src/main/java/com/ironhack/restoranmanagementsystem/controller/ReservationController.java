package com.ironhack.restoranmanagementsystem.controller;
import com.ironhack.restoranmanagementsystem.dto.request.ReservationCreateRequest;
import com.ironhack.restoranmanagementsystem.dto.request.ReservationUpdateRequest;
import com.ironhack.restoranmanagementsystem.dto.response.ReservationResponse;
import com.ironhack.restoranmanagementsystem.enums.ReservationStatus;
import com.ironhack.restoranmanagementsystem.service.ReservationService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/reservation")
public class ReservationController {
public final ReservationService reservationService;
    public ReservationController(ReservationService reservationService) {
        this.reservationService = reservationService;
    }
    @PostMapping("/{userId}")
    public ResponseEntity<ReservationResponse>createReservation(
            @PathVariable Long userId,
            @Valid @RequestBody ReservationCreateRequest request) {
        ReservationResponse response = reservationService.createReservation(userId, request);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }
    @GetMapping
    public List<ReservationResponse>getAll(){
        return reservationService.getAllReservations();
    }
    @GetMapping("/{id}")
    public ReservationResponse getById(@PathVariable Long id){
        return reservationService.getById(id);
    }
    @GetMapping("/user/{userId}")
    public List<ReservationResponse>getMyReservations(@PathVariable Long userId){
        return reservationService.getMyReservations(userId);
    }
    @PatchMapping("/{id}/confirm")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ReservationResponse>confirmReservation(@PathVariable Long id){
        return ResponseEntity.ok(reservationService.confirmReservation(id));
    }
    @PatchMapping("/{id}/cancel")
    @PreAuthorize("hasAnyRole('ADMIN','USER')")
    public ResponseEntity<ReservationResponse>cancelReservation(@PathVariable Long id) {
        return ResponseEntity.ok(reservationService.cancelReservation(id));
    }
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public void delete(@PathVariable Long id) {
        reservationService.deleteReservation(id);
    }
    @PutMapping("/{id}")
    public ReservationResponse update(@PathVariable Long id,@Valid @RequestBody ReservationUpdateRequest request){
        return reservationService.updateReservation(id, request);
    }
    @GetMapping("/status")
    public ResponseEntity<List<ReservationResponse>>getByStatus(@RequestParam ReservationStatus status){
        return ResponseEntity.ok(reservationService.getReservationByStatus(status));
    }
    @GetMapping("/user/{userId}/ordered")
    public List<ReservationResponse>getByUserIdOrdered(@PathVariable Long userId){
        return reservationService.getByUserIdOrderByTime(userId);}
    @GetMapping("/min_guests")
    public List<ReservationResponse>getByMinGuests(@RequestParam int count){
        return reservationService.getByMinGuestCount(count);
    }

}
