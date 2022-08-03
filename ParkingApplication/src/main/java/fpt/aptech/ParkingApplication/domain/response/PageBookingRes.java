/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package fpt.aptech.ParkingApplication.domain.response;

import java.util.List;

/**
 *
 * @author CHIEN
 */
public class PageBookingRes {

    private List<ItemPageBooking> listBooking;
    private int currentPage;
    private int size;
    private int totalPages;

    public PageBookingRes() {
    }

    public PageBookingRes(List<ItemPageBooking> listBooking, int currentPage, int size, int totalPages) {
        this.listBooking = listBooking;
        this.currentPage = currentPage;
        this.size = size;
        this.totalPages = totalPages;
    }

    public List<ItemPageBooking> getListBooking() {
        return listBooking;
    }

    public void setListBooking(List<ItemPageBooking> listBooking) {
        this.listBooking = listBooking;
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
