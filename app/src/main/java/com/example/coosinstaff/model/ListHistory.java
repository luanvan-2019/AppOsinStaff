package com.example.coosinstaff.model;

public class ListHistory {

    private String ordertype,date, ca, diadiem,cusName,dateEnd,userOrder;
    private int gia,mahoadon;

    public ListHistory(String ordertype, String date, String ca, String diadiem, int mahoadon, int gia, String cusName, String dateEnd,String userOrder) {
        this.ordertype = ordertype;
        this.date = date;
        this.ca = ca;
        this.diadiem = diadiem;
        this.mahoadon = mahoadon;
        this.gia = gia;
        this.cusName = cusName;
        this.dateEnd = dateEnd;
        this.userOrder=userOrder;
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

    public String getEmpName() {
        return cusName;
    }

    public void setEmpName(String empName) {
        this.cusName = empName;
    }

    public String getDateEnd() {
        return dateEnd;
    }

    public void setDateEnd(String dateEnd) {
        this.dateEnd = dateEnd;
    }

    public String getUserOrder() {
        return userOrder;
    }

    public void setUserOrder(String userOrder) {
        this.userOrder = userOrder;
    }
}