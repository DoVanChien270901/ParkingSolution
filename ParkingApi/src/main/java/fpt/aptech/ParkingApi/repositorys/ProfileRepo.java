/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/SpringFramework/Repository.java to edit this template
 */
package fpt.aptech.ParkingApi.repositorys;

import fpt.aptech.ParkingApi.dto.response.BookingDetailRes;
import fpt.aptech.ParkingApi.dto.response.ItemPageProfile;
import fpt.aptech.ParkingApi.entities.Profile;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.query.Procedure;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;

/**
 *
 * @author CHIEN
 */
public interface ProfileRepo extends JpaRepository<Profile, Integer> {

    @Query(value = "SELECT p.fullname FROM Profile p CROSS JOIN Account a WHERE a.role = 'user'",
            countQuery = "SELECT count(p.fullname) FROM Profile p",
            nativeQuery = true)
    Page<Profile> getListUser(Pageable pageable);

    @Query("SELECT p FROM Profile p WHERE p.username.username = :username")
    Profile getByUsername(@PathVariable("username") String username);

    @Query("SELECT p.balance FROM Profile p WHERE p.username.username = :username")
    double getBalanceByUsername(@PathVariable("username") String username);

    @Transactional
    @Modifying(flushAutomatically = true, clearAutomatically = true)
    @Query("UPDATE Profile p SET p.balance = :setbalance WHERE p.username.username = :username")
    int updateBalanceByUsername(@PathVariable("setbalance") double setbalance, @PathVariable("username") String username);
    @Transactional
    @Query(value = "{CALL listUsername(:role)}", nativeQuery = true)
    List<Profile> callProc(@Param("role")String role);
    @Query(name = "getUserByRole", nativeQuery = true)
    List<ItemPageProfile> getUserByRole(@Param("role")String role);
}
