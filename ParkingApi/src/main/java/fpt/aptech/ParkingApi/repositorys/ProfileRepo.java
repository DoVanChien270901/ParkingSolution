/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/SpringFramework/Repository.java to edit this template
 */
package fpt.aptech.ParkingApi.repositorys;

import fpt.aptech.ParkingApi.entities.Profile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;

/**
 *
 * @author CHIEN
 */
public interface ProfileRepo extends JpaRepository<Profile, Integer> {

    @Query("SELECT p FROM Profile p WHERE p.username.username = :username")
    Profile getByUsername(@PathVariable("username") String username);
    @Query("SELECT p.balance FROM Profile p WHERE p.username.username = :username")
    double getBalanceByUsername(@PathVariable("username") String username);
    @Transactional
    @Modifying(flushAutomatically = true, clearAutomatically = true)
    @Query("UPDATE Profile p SET p.balance = :setbalance WHERE p.username.username = :username")
    int updateBalanceByUsername(@PathVariable("setbalance")double setbalance, @PathVariable("username") String username);
}
