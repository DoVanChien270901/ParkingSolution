/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package fpt.aptech.ParkingApi.implementations;

import fpt.aptech.ParkingApi.dto.response.ParkingRes;
import fpt.aptech.ParkingApi.entities.Parkinglocation;
import fpt.aptech.ParkingApi.interfaces.IParking;
import fpt.aptech.ParkingApi.repositorys.ParkingRepo;
import fpt.aptech.ParkingApi.utils.ModelMapperUtil;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
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
    private ModelMapperUtil _mapper;

    @Override
    public Parkinglocation getByParkingName(String parkingName) {
        return null;
    }

    @Override
    public List<ParkingRes> listParking() {
        List<ParkingRes> res = _mapper.mapList(_parkingResponsetory.findAll(), ParkingRes.class);
        return res;
    }

}
