/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/springframework/Repository.java to edit this template
 */
package fpt.aptech.ParkingApi.repositorys;

import fpt.aptech.ParkingApi.entities.Parkinghistory;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 *
 * @author vantu
 */
public interface ParkingHistoryRepo extends JpaRepository<Parkinghistory, Integer> {
    
}
