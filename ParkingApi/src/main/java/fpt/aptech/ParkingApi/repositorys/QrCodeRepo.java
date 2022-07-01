/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/SpringFramework/Repository.java to edit this template
 */
package fpt.aptech.ParkingApi.repositorys;

import fpt.aptech.ParkingApi.entities.Account;
import fpt.aptech.ParkingApi.entities.Qrcode;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.web.bind.annotation.PathVariable;

/**
 *
 * @author CHIEN
 */
public interface QrCodeRepo extends JpaRepository<Qrcode, Integer> {

    @Query("SELECT q FROM Qrcode q WHERE q.accountid.username = :username and q.title = :title")
    Qrcode getByUserName(@PathVariable("username") String username, @PathVariable("title") String title);
}
