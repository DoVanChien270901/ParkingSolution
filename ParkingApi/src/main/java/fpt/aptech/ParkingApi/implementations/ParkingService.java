/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package fpt.aptech.ParkingApi.implementations;

import fpt.aptech.ParkingApi.dto.request.AddParkingReq;
import fpt.aptech.ParkingApi.dto.request.UpdateParkingReq;
import fpt.aptech.ParkingApi.dto.response.LoadStatusParking;
import fpt.aptech.ParkingApi.dto.response.PageParkingHistoryRes;
import fpt.aptech.ParkingApi.dto.response.ParkingHistoryRes;
import fpt.aptech.ParkingApi.dto.response.ParkingRes;
import fpt.aptech.ParkingApi.entities.Parkinghistory;
import fpt.aptech.ParkingApi.entities.Parkinglocation;
import fpt.aptech.ParkingApi.interfaces.IParking;
import fpt.aptech.ParkingApi.repositorys.BookingRepo;
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
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
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
    private BookingRepo _bookingRepo;
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
        fromDateTime = fromDate.atTime(00, 00, 00, 0000);
        LocalDateTime toDateTime = toDate.atStartOfDay();
        toDateTime = toDate.atTime(00, 00, 00, 0000);

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
    public PageParkingHistoryRes getHistory(String parkingname, LocalDate fromDate, LocalDate toDate, int page, int size) {
        LocalDateTime fromDateTime = fromDate.atStartOfDay();
        fromDateTime = fromDate.atTime(00, 00, 00, 0000);
        LocalDateTime toDateTime = toDate.atStartOfDay();
        toDateTime = toDate.atTime(00, 00, 00, 0000);

        List<ParkingHistoryRes> list = _parkingHistoryRepo.getAllParkingHistorySearch(parkingname, fromDateTime, toDateTime);
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
    public boolean add(AddParkingReq parking) {
        Parkinglocation parkinglocation = _parkingRepo.getByName(parking.getName());
        if (parkinglocation == null) {
            parkinglocation = _mapper.map(parking, Parkinglocation.class);
            _parkingRepo.save(parkinglocation);
            return true;
        } else {
            return false;
        }
    }

    @Override
    public void delete(String id) {
        _parkingRepo.deleteById(id);
    }

    @Override
    public boolean update(UpdateParkingReq parking) {
        Parkinglocation parkinglocation = _parkingRepo.getByName(parking.getName());
        if (parkinglocation != null) {
            parkinglocation.setName(parking.getName());
            parkinglocation.setLatitude(parking.getLatitude());
            parkinglocation.setLongtitude(parking.getLongtitude());
            parkinglocation.setAddress(parking.getAddress());
            parkinglocation.setNop(parking.getNop());
            parkinglocation.setRentcost(parking.getRentcost());
            _parkingRepo.save(parkinglocation);
            return true;
        } else {
            return false;
        }
    }

    @Override
    public List<ParkingRes> seachByName(String name) {
        List<Parkinglocation> parkinglocations = _parkingRepo.searchParkingByName(name);
        List<ParkingRes> res = _mapper.mapList(parkinglocations, ParkingRes.class);
        return res;
    }

    @Override
    public PageParkingHistoryRes getHistoryByName(String name, int page, int size) {
        PageRequest pageRequest = PageRequest.of(page, size);
        List<ParkingHistoryRes> list = _parkingHistoryRepo.getListParkingHistoryByParkingName(name);
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
    public int freeOneSlot(String name) {
        return _parkingResponsetory.plusOneBlank(name);
    }

    @Override
    public int fillOneSlot(String name) {
        return _parkingRepo.minusOneBlank(name);
    }

    @Override
    public List<LoadStatusParking> allStatusParking() {
        List<ParkingRes> parkingRes = _mapper.mapList(_parkingResponsetory.findAll(), ParkingRes.class);
        List<LoadStatusParking> loadStatusParkings = new ArrayList<>();
        List<String> listParkingName = new ArrayList<>();
        for (ParkingRes item : parkingRes) {
            listParkingName.add(item.getName());
            LoadStatusParking loadStatusParking = new LoadStatusParking();
            loadStatusParking.setParkingname(item.getName());
            loadStatusParking.setColumnofrow(item.getColumnofrow());
            //add booked
            List<String> codebooked = _bookingRepo.getLocationCodeByParkingName(item.getName());
            loadStatusParking.setCodebooked(codebooked.toArray(new String[codebooked.size()]));
            String code = "A";
            int step = 1;
            String[] arrCode = new String[item.getNop()];
            for (int i = 0; i < item.getNop(); i++) {
                if (i != 0 && (i % item.getColumnofrow() == 0)) {
                    int charValue = code.charAt(0);
                    code = String.valueOf((char) (charValue + 1));
                    step = 1;
                }
                arrCode[i] = (code + (step));
                step++;
            }
            loadStatusParking.setLocationcode(arrCode);
            loadStatusParkings.add(loadStatusParking);
        }
        return loadStatusParkings;
    }

    @Override
    public LoadStatusParking StatusParkingByName(String parkingName) {
        ParkingRes parkingRes = _mapper.map(_parkingRepo.getByName(parkingName), ParkingRes.class);
        LoadStatusParking loadStatusParking = new LoadStatusParking();
        loadStatusParking.setParkingname(parkingRes.getName());
        loadStatusParking.setColumnofrow(parkingRes.getColumnofrow());
        //add booked
        List<String> codebooked = _bookingRepo.getLocationCodeByParkingName(parkingName);
        loadStatusParking.setCodebooked(codebooked.toArray(new String[codebooked.size()]));
        String code = "A";
        int step = 1;
        String[] arrCode = new String[parkingRes.getNop()];
        for (int i = 0; i < parkingRes.getNop(); i++) {
            if (i != 0 && (i % parkingRes.getColumnofrow() == 0)) {
                int charValue = code.charAt(0);
                code = String.valueOf((char) (charValue + 1));
                step = 1;
            }
            arrCode[i] = (code + (step));
            step++;
        }
        loadStatusParking.setLocationcode(arrCode);
        return loadStatusParking;
    }

    @Override
    public List<ParkingHistoryRes> getAllHistoryByUserName(String username) {
        return _parkingHistoryRepo.getListTransByUsername(username);
    }

    @Override
    public List<ParkingHistoryRes> getAllHistoryByUserSearch(String username, LocalDate fromDate, LocalDate toDate) {
        LocalDateTime fromDateTime = fromDate.atStartOfDay();
        fromDateTime = fromDate.atTime(00,00,00,0000);
        LocalDateTime toDateTime = toDate.atStartOfDay();
        toDateTime = toDate.atTime(00,00,00,0000);
        return _parkingHistoryRepo.getListParkingHistoryByUsernameSearch(username, fromDateTime, toDateTime);
    }
    
}
