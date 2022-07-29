/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package fpt.aptech.ParkingApi.implementations;

import fpt.aptech.ParkingApi.dto.request.AddRevenueReq;
import fpt.aptech.ParkingApi.dto.response.*;
import fpt.aptech.ParkingApi.entities.Revenue;
import fpt.aptech.ParkingApi.interfaces.IRevenue;
import fpt.aptech.ParkingApi.repositorys.RevenueRepo;
import fpt.aptech.ParkingApi.utils.ModelMapperUtil;
import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author CHIEN
 */
@Service
public class RevenueServices implements IRevenue {

    @Autowired
    private ModelMapperUtil _mapper;
    @Autowired
    private RevenueRepo _revenueRepo;

    @Override
    public boolean add(AddRevenueReq addRevenueReq) {
        try {
            Revenue revenue = _mapper.map(addRevenueReq, Revenue.class);
            _revenueRepo.save(revenue);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public List<DataRevenueMonthRes> byMonths(int year) {
        List<RevenueByMoth> revenueByMoths = _revenueRepo.getRevenueMonthInYear(year);
        List<DataRevenueMonthRes> res = new ArrayList<>();
        List<ParkingName> parkingNames = _revenueRepo.getParkingNameInYear(year);
        for (ParkingName item : parkingNames) {
            DataRevenueMonthRes dr = new DataRevenueMonthRes();
            dr.setLabel(item.getParkingname());
            res.add(dr);
        }
        for (RevenueByMoth revenueByMoth : revenueByMoths) {
            for (DataRevenueMonthRes re : res) {
                if (revenueByMoth.getPartkingname().equals(re.getLabel())) {
                    re.setData(revenueByMoth.getMonth() - 1, (int) revenueByMoth.getSumM());
                    break;
                }
            }
        }
        return res;
    }

    @Override
    public List<DataRevenueDayRes> byDays(int month, int year) {
        List<RevenueByDay> revenueByDays = _revenueRepo.getRevenueDayInMonth(month, year);
        List<DataRevenueDayRes> res = new ArrayList<>();
        List<ParkingName> parkingNames = _revenueRepo.getParkingNameInMonth(month, year);
        for (ParkingName item : parkingNames) {
            DataRevenueDayRes dr = new DataRevenueDayRes();
            dr.setLabel(item.getParkingname());
            res.add(dr);
        }
        for (RevenueByDay revenueByDay : revenueByDays) {
            for (DataRevenueDayRes re : res) {
                if (revenueByDay.getPartkingname().equals(re.getLabel())) {
                    re.setData(revenueByDay.getDay()- 1, (int) revenueByDay.getSumD());
                    break;
                }
            }
        }
        return res;
    }
    
}
