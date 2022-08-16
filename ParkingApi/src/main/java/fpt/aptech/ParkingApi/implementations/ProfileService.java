/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package fpt.aptech.ParkingApi.implementations;

import fpt.aptech.ParkingApi.dto.enumm.TitleQrCode;
import fpt.aptech.ParkingApi.dto.request.EditProfileReq;
import fpt.aptech.ParkingApi.dto.request.RegisterReq;
import fpt.aptech.ParkingApi.dto.response.ItemPageProfile;
import fpt.aptech.ParkingApi.interfaces.IProfile;
import fpt.aptech.ParkingApi.dto.response.PageProfileRes;
import fpt.aptech.ParkingApi.dto.response.UserDetailsRes;
import fpt.aptech.ParkingApi.dto.response.ProfileRes;
import fpt.aptech.ParkingApi.entities.Parkinglocation;
import fpt.aptech.ParkingApi.entities.Profile;
import fpt.aptech.ParkingApi.entities.Qrcode;
import fpt.aptech.ParkingApi.entities.Transactioninformation;
import fpt.aptech.ParkingApi.repositorys.ParkingRepo;
import fpt.aptech.ParkingApi.utils.ModelMapperUtil;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import fpt.aptech.ParkingApi.repositorys.ProfileRepo;
import fpt.aptech.ParkingApi.repositorys.QrCodeRepo;
import fpt.aptech.ParkingApi.repositorys.TransactionRepo;
import java.time.LocalDateTime;
import org.springframework.beans.support.PagedListHolder;
import org.springframework.data.domain.Pageable;

/**
 *
 * @author CHIEN
 */
@Service
public class ProfileService implements IProfile {

    @Autowired
    private ProfileRepo _profileRepository;
    @Autowired
    private QrCodeRepo _qrCodeRepository;
    @Autowired
    private TransactionRepo _transactionRepo;
    @Autowired
    private ParkingRepo _ParkingRepository;
    @Autowired
    private ModelMapperUtil _mapper;

    @Override
    public PageProfileRes getListUser(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Profile> pageprofile = _profileRepository.getListUser(pageable);
        List<ItemPageProfile> listpro = _mapper.mapList(pageprofile.getContent(), ItemPageProfile.class);
        PageProfileRes pageProfileRes = new PageProfileRes();
        pageProfileRes.setListProfile(listpro);
        pageProfileRes.setCurrentPage(pageprofile.getPageable().getPageNumber());
        pageProfileRes.setSize(pageprofile.getSize());
        pageProfileRes.setTotalPages(pageprofile.getTotalPages());
        return pageProfileRes;
    }

    @Override
    public PageProfileRes findAll(int page, int size) {
        PageRequest pageRequest = PageRequest.of(page, size);
        Page<Profile> pageprofile = _profileRepository.findAll(pageRequest);
        List<ItemPageProfile> listpro = _mapper.mapList(pageprofile.getContent(), ItemPageProfile.class);
        PageProfileRes pageProfileRes = new PageProfileRes();
        pageProfileRes.setListProfile(listpro);
        pageProfileRes.setCurrentPage(pageprofile.getPageable().getPageNumber());
        pageProfileRes.setSize(pageprofile.getSize());
        pageProfileRes.setTotalPages(pageprofile.getTotalPages());
        return pageProfileRes;
    }

    @Override
    public ProfileRes getByUserName(String username) {
        Profile profile = _profileRepository.getByUsername(username);
        ProfileRes pres = _mapper.map(profile, ProfileRes.class);
        Qrcode qrcode = _qrCodeRepository.getByUserName(username, TitleQrCode.PROFILE.toString());
        pres.setQrcontent(qrcode.getContent());
        return pres;
    }

    @Override
    public Profile create(RegisterReq registerReq) {
        Profile profile = _mapper.map(registerReq, Profile.class);
        _profileRepository.save(profile);
        return profile;
    }

    @Override
    public boolean edit(EditProfileReq editProfileReq, String username) {
        Profile profile = _profileRepository.getByUsername(username);
        if (profile != null) {
            profile.setFullname(editProfileReq.getFullname());
            profile.setDob(editProfileReq.getDob());
            profile.setIdentitycard(editProfileReq.getIdentitycard());
            profile.setPhone(editProfileReq.getPhone());
            profile.setEmail(editProfileReq.getEmail());
            _profileRepository.save(profile);
            return true;
        } else {
            return false;
        }
    }

    @Override
    public boolean deductionBalanceForBooking(int timenumber, String username, String parkingname) {
        double balance = _profileRepository.getBalanceByUsername(username);
        double amount = timenumber * _ParkingRepository.getRencostByName(parkingname);
        if (balance - amount >= 0) {
            _profileRepository.updateBalanceByUsername(balance - amount, username);
            //create transaction
            Transactioninformation t = new Transactioninformation();
            t.setTransno((LocalDateTime.now()).toString());
            t.setAccountid(new Profile(username));
            t.setParkingname(new Parkinglocation(parkingname));
            t.setAmount(amount);
            t.setChannel("Parking Wallet");
            t.setDatetime(LocalDateTime.now());
            t.setStype("e-Booking");
            t.setStatuscode(0);
            _transactionRepo.save(t);
            return true;
        } else {
            return false;
        }
    }

    @Override
    public PageProfileRes getListUserByRole(String role, int page, int size) {
        List<ItemPageProfile> list = _profileRepository.getUserByRole("user");
        PagedListHolder holder = new PagedListHolder(list);
        holder.setPageSize(size);
        holder.setPage(page);
        PageProfileRes pageProfileRes = new PageProfileRes();
        pageProfileRes.setCurrentPage(page);
        pageProfileRes.setSize(size);
        pageProfileRes.setTotalPages(holder.getPageCount());
        pageProfileRes.setListProfile(holder.getPageList());
        return pageProfileRes;
    }

    @Override
    public UserDetailsRes profileDetailsByUsername(String username) {
        Profile profile = _profileRepository.getByUsername(username);
        UserDetailsRes pres = _mapper.map(profile, UserDetailsRes.class);
        return pres;
    }

    @Override
    public PageProfileRes getListUserByRole(String role, String username, int page, int size) {
        List<ItemPageProfile> list = _profileRepository.listUserByRoleSortName("user", username);
        PagedListHolder holder = new PagedListHolder(list);
        holder.setPageSize(size);
        holder.setPage(page);
        PageProfileRes pageProfileRes = new PageProfileRes();
        pageProfileRes.setCurrentPage(page);
        pageProfileRes.setSize(size);
        pageProfileRes.setTotalPages(holder.getPageCount());
        pageProfileRes.setListProfile(holder.getPageList());
        return pageProfileRes;
    }

}
