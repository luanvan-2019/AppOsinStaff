package com.example.coosinstaff.model;

public class ListSubmited {

    private String ordertype,date, ca, diadiem;
    private int gia,mahoadon;

    public ListSubmited(String ordertype, String date, String ca, String diadiem, int mahoadon, int gia) {
        this.ordertype = ordertype;
        this.date = date;
        this.ca = ca;
        this.diadiem = diadiem;
        this.mahoadon = mahoadon;
        this.gia = gia;
    }

    public String getOrdertype() {
        return ordertype;
    }

    public void setOrdertype (String ordertype) {
        this.ordertype = ordertype;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getCa() {
        return ca;
    }

    public void setCa(String ca) {
        this.ca = ca;
    }

    public String getDiadiem() {
        return diadiem;
    }

    public void setDiadiem(String diadiem) {
        this.diadiem = diadiem;
    }

    public int getMahoadon() {
        return mahoadon;
    }

    public void setMahoadon(int mahoadon) {
        this.mahoadon = mahoadon;
    }

    public int getGia() {
        return gia;
    }

    public void setGia(int gia) {
        this.gia = gia;
    }
}