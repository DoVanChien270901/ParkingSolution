/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/SpringFramework/Repository.java to edit this template
 */
package fpt.aptech.ParkingApi.repositorys;

import fpt.aptech.ParkingApi.entities.Profile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.web.bind.annotation.PathVariable;

/**
 *
 * @author CHIEN
 */
public interface ProfileRepo extends JpaRepository<Profile, Integer> {
    @Query("SELECT p FROM Profile p WHERE p.username.username = :username")
    Profile getByUsername(@PathVariable("username") String username);
    
}
