/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/SpringFramework/Repository.java to edit this template
 */
package fpt.aptech.ParkingApi.repositorys;

import fpt.aptech.ParkingApi.entities.Parkinglocation;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;

/**
 *
 * @author CHIEN
 */
public interface ParkingRepo extends JpaRepository<Parkinglocation, String> {

    @Query("SELECT p FROM Parkinglocation p WHERE p.name = :parkingname")
    Parkinglocation getByName(@PathVariable("parkingname") String parkingname);

    @Query("SELECT p.rentcost FROM Parkinglocation p WHERE p.name = :parkingname")
    double getRencostByName(@PathVariable("parkingname") String parkingname);

    @Query("SELECT p FROM Parkinglocation p WHERE p.name LIKE %:parkingname%")
    List<Parkinglocation> searchParkingByName(@PathVariable("parkingname") String parkingname);

    @Transactional
    @Modifying(flushAutomatically = true, clearAutomatically = true)
    @Query(value = "UPDATE Parkinglocation SET blank = blank +1 WHERE p.name = :name", nativeQuery = true)
    int plusOneBlank(@PathVariable("name") String name);

    @Transactional
    @Modifying(flushAutomatically = true, clearAutomatically = true)
    @Query(value = "UPDATE Parkinglocation SET blank = blank -1 WHERE name = :name", nativeQuery = true)
    int minusOneBlank(@PathVariable("username") String name);

}
