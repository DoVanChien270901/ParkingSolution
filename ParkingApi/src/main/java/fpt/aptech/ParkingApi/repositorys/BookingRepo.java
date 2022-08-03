/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/SpringFramework/Repository.java to edit this template
 */
package fpt.aptech.ParkingApi.repositorys;

import fpt.aptech.ParkingApi.dto.response.BookingDetailRes;
import fpt.aptech.ParkingApi.dto.response.BookingOfParkingRes;
import fpt.aptech.ParkingApi.dto.response.BookingRes;
import fpt.aptech.ParkingApi.dto.response.ItemPageBooking;
import fpt.aptech.ParkingApi.entities.Booking;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;

/**
 *
 * @author CHIEN-
 */
public interface BookingRepo extends JpaRepository<Booking, Integer> {

    @Query(name = "getListBookingByUsername", nativeQuery = true)
    List<BookingRes> getListBookingByUsername(@Param("username") String username);

    @Query(name = "getDetailBookingById", nativeQuery = true)
    BookingDetailRes getDetailBookingById(@Param("id") int id);

    @Query(name = "Booking.findById")
    Booking getBookingById(@Param("id") int id);

    @Transactional
    @Modifying(flushAutomatically = true, clearAutomatically = true)
    @Query("UPDATE Booking b SET b.status = :status, b.checkin = :checkin WHERE b.id = :id")
    int updateStatusAndCheckIn(@PathVariable("status") String status, @PathVariable("checkin") LocalDateTime checkin, @PathVariable("id") int id);

    @Transactional
    @Modifying(flushAutomatically = true, clearAutomatically = true)
    @Query("UPDATE Booking b SET b.status = :status, b.checkout = :checkout WHERE b.id = :id")
    int updateStatusAndCheckOut(@PathVariable("status") String status, @PathVariable("checkout") LocalDateTime checkout, @PathVariable("id") int id);

    @Query(name = "getListAllBooking", nativeQuery = true)
    List<ItemPageBooking> getListAllBooking();

    @Query(value = "SELECT b.locationcode FROM Booking b WHERE b.parkingname = :parkingname", nativeQuery = true)
    List<String> getLocationCodeByParkingName(@Param("parkingname") String useranme);
    
    @Query(name = "getListBookingByParkingName", nativeQuery = true)
    List<ItemPageBooking> getListBookingByParkingName(@Param("parkingname") String parkingname);
}
