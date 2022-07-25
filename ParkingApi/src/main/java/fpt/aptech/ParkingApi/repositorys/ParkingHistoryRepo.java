/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/springframework/Repository.java to edit this template
 */
package fpt.aptech.ParkingApi.repositorys;

import fpt.aptech.ParkingApi.dto.response.ParkingHistoryRes;
import fpt.aptech.ParkingApi.dto.response.TransactionRes;
import fpt.aptech.ParkingApi.entities.Parkinghistory;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

/**
 *
 * @author vantu
 */
public interface ParkingHistoryRepo extends JpaRepository<Parkinghistory, Integer> {
    @Query(name = "getListParkingHistoryByUsername", nativeQuery = true)
    List<ParkingHistoryRes> getListTransByUsername(@Param("username") String username);
    @Query(name = "getListParkingHistoryByUsernameSearch", nativeQuery = true)
    List<ParkingHistoryRes> getListParkingHistoryByUsernameSearch(@Param("username") String username,
            @Param("fromDate")LocalDateTime fromDate, @Param("toDate")LocalDateTime toDate);
}
