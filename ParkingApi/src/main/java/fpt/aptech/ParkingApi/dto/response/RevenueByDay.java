/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package fpt.aptech.ParkingApi.dto.response;

/**
 *
 * @author CHIEN
 */
public class RevenueByDay {

    private String partkingname;
    private Integer day;
    private Double sumD;

    public RevenueByDay() {
    }

    public RevenueByDay(String partkingname, Integer day, Double sumD) {
        this.partkingname = partkingname;
        this.day = day;
        this.sumD = sumD;
    }

    public String getPartkingname() {
        return partkingname;
    }

    public void setPartkingname(String partkingname) {
        this.partkingname = partkingname;
    }

    public Integer getDay() {
        return day;
    }

    public void setDay(Integer day) {
        this.day = day;
    }

    public double getSumD() {
        return sumD;
    }

    public void setSumD(Double sumD) {
        this.sumD = sumD;
    }

}
