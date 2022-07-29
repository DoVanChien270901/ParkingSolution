/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package fpt.aptech.ParkingApplication.domain.response;

import java.util.Random;

/**
 *
 * @author CHIEN
 */
public class DataRevenueMonthRes {

    private String label;
    private String backgroundColor = randomColor();
    private String borderColor = backgroundColor;
    private int[] data = new int[12];

    public DataRevenueMonthRes() {
    }
    public DataRevenueMonthRes(String label) {
        this.label = label;
    }
    
    public DataRevenueMonthRes(String label, String backgroundColor, String borderColor) {
        this.label = label;
        this.backgroundColor = backgroundColor;
        this.borderColor = borderColor;
    }
    
    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getBackgroundColor() {
        return backgroundColor;
    }

    public void setBackgroundColor(String backgroundColor) {
        this.backgroundColor = backgroundColor;
    }

    public String getBorderColor() {
        return borderColor;
    }

    public void setBorderColor(String borderColor) {
        this.borderColor = borderColor;
    }

    public int[] getData() {
        return data;
    }

    public void setData(int index, int date) {
        this.data[index] = date;
    }
    private String randomColor(){
        Random random = new Random();
        int R = random.nextInt(200);
        int G = random.nextInt(200);
        int B = random.nextInt(200);
        String RGB = "rgb("+R+","+G+","+B+")";
        return RGB;
    }
}
