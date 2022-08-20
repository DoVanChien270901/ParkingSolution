/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package fpt.aptech.ParkingApi.dto.response;

/**
 *
 * @author CHIEN
 */
public class Statistical {

    private Long user;
    private Long parking;
    private Long amount;

    public Statistical() {
    }

    public Long getUser() {
        return user;
    }

    public void setUser(Long user) {
        this.user = user;
    }

    public Long getParking() {
        return parking;
    }

    public void setParking(Long parking) {
        this.parking = parking;
    }

    public Long getAmount() {
        return amount;
    }

    public void setAmount(Long amount) {
        this.amount = amount;
    }

}
