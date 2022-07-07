/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fpt.aptech.ParkingApi.dto.response;

import java.util.List;

/**
 *
 * @author PCvinhvizg
 */
public class PageTransactionRes {
    private List<TransactionRes> listTransaction;
    private int currentPage;
    private int size;
    private int totalPages;

    public PageTransactionRes() {
    }

    public List<TransactionRes> getListTransaction() {
        return listTransaction;
    }

    public void setListTransaction(List<TransactionRes> listTransaction) {
        this.listTransaction = listTransaction;
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
