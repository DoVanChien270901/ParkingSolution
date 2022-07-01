/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package fpt.aptech.ParkingApi.dto.request;

import fpt.aptech.ParkingApi.dto.enumm.TitleQrCode;

/**
 *
 * @author vantu
 */
public class AddQrReq {
    private Object content;
    private TitleQrCode title;

    public AddQrReq() {
    }

    public Object getContent() {
        return content;
    }

    public void setContent(Object content) {
        this.content = content;
    }
    public String getTitle() {
        return title.toString();
    }

    public void setTitle(TitleQrCode title) {
        this.title = title;
    }
}
