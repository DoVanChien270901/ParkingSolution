/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package fpt.aptech.ParkingApi.dto.response;

/**
 *
 * @author CHIEN
 */
public class RevenueByMoth {

    private String partkingname;
    private Integer month;
    private Double sumM;

    public RevenueByMoth() {
    }

    public RevenueByMoth(String partkingname, Integer month, Double sumM) {
        this.partkingname = partkingname;
        this.month = month;
        this.sumM = sumM;
    }

    public String getPartkingname() {
        return partkingname;
    }

    public void setPartkingname(String partkingname) {
        this.partkingname = partkingname;
    }

    public Integer getMonth() {
        return month;
    }

    public void setMonth(Integer month) {
        this.month = month;
    }

    public double getSumM() {
        return sumM;
    }

    public void setSumM(Double sum) {
        this.sumM = sum;
    }

}
