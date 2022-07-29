/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package fpt.aptech.ParkingApi.interfaces;

import fpt.aptech.ParkingApi.dto.request.AddRevenueReq;
import fpt.aptech.ParkingApi.dto.response.*;
import java.util.List;

/**
 *
 * @author CHIEN
 */
public interface IRevenue {
    boolean add(AddRevenueReq addRevenueReq);
    List<DataRevenueMonthRes> byMonths(int year);
    List<DataRevenueDayRes> byDays(int month, int year);
}
