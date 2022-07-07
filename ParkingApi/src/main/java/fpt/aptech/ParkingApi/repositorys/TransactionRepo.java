/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fpt.aptech.ParkingApi.repositorys;

import fpt.aptech.ParkingApi.entities.Transactioninformation;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.web.bind.annotation.PathVariable;

/**
 *
 * @author PCvinhvizg
 */
public interface TransactionRepo extends JpaRepository<Transactioninformation, String> {
    @Query("SELECT p FROM Transactioninformation p WHERE p.transno.transno = :transno")
    Transactioninformation getByTransNo(@PathVariable("transno") String transno);
}
