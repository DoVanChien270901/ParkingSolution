/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package fpt.aptech.ParkingApi.dto.response;

import java.util.List;

/**
 *
 * @author CHIEN
 */
public class PageProfileRes {

    private List<ProfileRes> listProfile;
    private int currentPage;
    private int size;
    private int totalPages;

    public PageProfileRes() {
    }

    public List<ProfileRes> getListProfile() {
        return listProfile;
    }

    public void setListProfile(List<ProfileRes> listProfile) {
        this.listProfile = listProfile;
    }

    public int getCurrentPage() {
        return currentPage;
    }

    public void setCurrentPage(int currentPage) {
        this.currentPage = currentPage;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public int getTotalPages() {
        return totalPages;
    }

    public void setTotalPages(int totalPages) {
        this.totalPages = totalPages;
    }

}
