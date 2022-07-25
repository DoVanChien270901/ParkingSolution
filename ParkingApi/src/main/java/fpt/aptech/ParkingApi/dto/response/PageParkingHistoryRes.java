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
public class PageParkingHistoryRes {
    private List<ParkingHistoryRes> listParkingHistory;
    private int currentPage;
    private int size;
    private int totalPages;

    public PageParkingHistoryRes() {
    }

    public PageParkingHistoryRes(List<ParkingHistoryRes> listParkingHistory, int currentPage, int size, int totalPages) {
        this.listParkingHistory = listParkingHistory;
        this.currentPage = currentPage;
        this.size = size;
        this.totalPages = totalPages;
    }

    public List<ParkingHistoryRes> getListParkingHistory() {
        return listParkingHistory;
    }

    public void setListParkingHistory(List<ParkingHistoryRes> listParkingHistory) {
        this.listParkingHistory = listParkingHistory;
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
