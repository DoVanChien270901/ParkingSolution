/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package fpt.aptech.ParkingApi.interfaces;

import fpt.aptech.ParkingApi.dto.request.RegisterReq;
import fpt.aptech.ParkingApi.entities.Account;
import java.util.List;

/**
 *
 * @author CHIEN
 */
public interface IAccount {
    public boolean create(RegisterReq registerReq);
    public List<Account> getAccount();
    public Account getAccountById(String id);
    public List<Account> findAll();
}
