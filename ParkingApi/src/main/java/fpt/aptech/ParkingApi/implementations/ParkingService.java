/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package fpt.aptech.ParkingApi.implementations;

import fpt.aptech.ParkingApi.dto.response.PageParkingHistoryRes;
import fpt.aptech.ParkingApi.dto.response.ParkingHistoryRes;
import fpt.aptech.ParkingApi.dto.response.ParkingRes;
import fpt.aptech.ParkingApi.entities.Parkinghistory;
import fpt.aptech.ParkingApi.entities.Parkinglocation;
import fpt.aptech.ParkingApi.interfaces.IParking;
import fpt.aptech.ParkingApi.repositorys.ParkingHistoryRepo;
import fpt.aptech.ParkingApi.repositorys.ParkingRepo;
import fpt.aptech.ParkingApi.utils.ModelMapperUtil;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.support.PagedListHolder;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

/**
 *
 * @author CHIEN
 */
@Service
public class ParkingService implements IParking {

    @Autowired
    private ParkingRepo _parkingResponsetory;
    @Autowired
    private ParkingHistoryRepo _parkingHistoryRepo;
    @Autowired
    private ParkingRepo _parkingRepo;
    @Autowired
    private ModelMapperUtil _mapper;

    @Override
    public ParkingRes getByParkingName(String parkingName) {
        ParkingRes res = _mapper.map(_parkingRepo.getByName(parkingName), ParkingRes.class);    
        return res;
    }

    @Override
    public List<ParkingRes> listParking() {
        List<ParkingRes> res = _mapper.mapList(_parkingResponsetory.findAll(), ParkingRes.class);
        return res;
    }

    @Override
    public PageParkingHistoryRes getHistoryByUserName(String username, int page, int size) {
        PageRequest pageRequest = PageRequest.of(page, size);
        List<ParkingHistoryRes> list = _parkingHistoryRepo.getListTransByUsername(username);
        PagedListHolder holder = new PagedListHolder(list);
        holder.setPageSize(size);
        holder.setPage(page);
        PageParkingHistoryRes res = new PageParkingHistoryRes();
        res.setCurrentPage(page);
        res.setSize(size);
        res.setTotalPages(holder.getPageCount());
        res.setListParkingHistory(holder.getPageList());
        return res;
    }

    @Override
    public PageParkingHistoryRes getHistoryByUserName(String username, LocalDate fromDate, LocalDate toDate, int page, int size) {
        LocalDateTime fromDateTime = fromDate.atStartOfDay();
        fromDateTime = fromDate.atTime(00,00,00,0000);
        LocalDateTime toDateTime = toDate.atStartOfDay();
        toDateTime = toDate.atTime(00,00,00,0000);
        
        List<ParkingHistoryRes> list = _parkingHistoryRepo.getListParkingHistoryByUsernameSearch(username, fromDateTime, toDateTime);
        PagedListHolder holder = new PagedListHolder(list);
        holder.setPageSize(size);
        holder.setPage(page);
        PageParkingHistoryRes res = new PageParkingHistoryRes();
        res.setCurrentPage(page);
        res.setSize(size);
        res.setTotalPages(holder.getPageCount());
        res.setListParkingHistory(holder.getPageList());
        return res;
    }

    @Override
    public PageParkingHistoryRes getHistory(int page, int size) {
        PageRequest pageRequest = PageRequest.of(page, size);
        List<Parkinghistory> list1 = _parkingHistoryRepo.findAll(Sort.by(Sort.Direction.DESC, "starttime"));
        List<ParkingHistoryRes> list = new ArrayList<>();
        for (Parkinghistory item : list1) {
            ParkingHistoryRes parkingHistoryRes = new ParkingHistoryRes(item.getId(), item.getStarttime(),
            item.getTimenumber(), item.getCarname(), item.getLisenceplates(), item.getParkingname().getName());
            list.add(parkingHistoryRes);
        }
        PagedListHolder holder = new PagedListHolder(list);
        holder.setPageSize(size);
        holder.setPage(page);
        PageParkingHistoryRes res = new PageParkingHistoryRes();
        res.setCurrentPage(page);
        res.setSize(size);
        res.setTotalPages(holder.getPageCount());
        res.setListParkingHistory(holder.getPageList());
        return res;
    }

    @Override
    public PageParkingHistoryRes getHistory(LocalDate fromDate, LocalDate toDate, int page, int size) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
}
