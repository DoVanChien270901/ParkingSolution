/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/SpringFramework/Repository.java to edit this template
 */
package fpt.aptech.ParkingApi.repositorys;

import fpt.aptech.ParkingApi.dto.response.*;
import fpt.aptech.ParkingApi.entities.Revenue;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

/**
 *
 * @author CHIEN
 */
public interface RevenueRepo extends JpaRepository<Revenue, Integer> {

    //Revenue month in year
    @Query(name = "getParkingNameInYear", nativeQuery = true)
    List<ParkingName> getParkingNameInYear(@Param("year") int year);

    @Query(name = "getRevenueMonthInYear", nativeQuery = true)
    List<RevenueByMoth> getRevenueMonthInYear(@Param("year") int year);

    //Revenue day in month
    @Query(name = "getParkingNameInMonth", nativeQuery = true)
    List<ParkingName> getParkingNameInMonth(@Param("month") int month, @Param("year") int year);

    @Query(name = "getRevenueDayInMonth", nativeQuery = true)
    List<RevenueByDay> getRevenueDayInMonth(@Param("month") int month, @Param("year") int year);

    @Query("SELECT SUM(r.amount) from Revenue r")
    long sumAmount();
}
