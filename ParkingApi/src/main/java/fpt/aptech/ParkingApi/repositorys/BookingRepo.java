/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/SpringFramework/Repository.java to edit this template
 */
package fpt.aptech.ParkingApi.repositorys;

import fpt.aptech.ParkingApi.dto.response.BookingDetailRes;
import fpt.aptech.ParkingApi.dto.response.BookingRes;
import fpt.aptech.ParkingApi.entities.Booking;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.web.bind.annotation.PathVariable;

/**
 *
 * @author CHIEN-
 */
public interface BookingRepo extends JpaRepository<Booking, Integer> {
    @Query(name = "getListBookingByUsername", nativeQuery = true)
    List<BookingRes> getListBookingByUsername(@Param("username")String username);
    @Query(name = "getDetailBookingById", nativeQuery = true)
    BookingDetailRes getDetailBookingById(@Param("id")int id);
}
