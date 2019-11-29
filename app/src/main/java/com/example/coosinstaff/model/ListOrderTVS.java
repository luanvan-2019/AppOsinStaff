package com.example.coosinstaff.model;

public class ListOrderTVS {

    private String orderType;
    private Integer price;
    private String location;
    private String nearby;
    private String date;
    private String time;
    private String area;
    private String createAt;
    private Integer seenCount;
    private String userSubmitAmount;

    public ListOrderTVS(String orderType, int price, String location, String nearby, String date, String time,String area, String createAt, Integer seenCount,
                        String userSubmitAmount) {
        this.orderType = orderType;
        this.price = price;
        this.location = location;
        this.nearby = nearby;
        this.date = date;
        this.time = time;
        this.area = area;
        this.createAt = createAt;
        this.seenCount = seenCount;
        this.userSubmitAmount = userSubmitAmount;
    }

    public String getOrderType() {
        return orderType;
    }

    public void setOrderType(String orderType) {
        this.orderType = orderType;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getNearby() {
        return nearby;
    }

    public void setNearby(String nearby) {
        this.nearby = nearby;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
            this.area = area;
    }

    public String getCreateAt() {
        return createAt;
    }

    public void setCreateAt(String createAt) {
        this.createAt = createAt;
    }

    public Integer getSeenCount() {
        return seenCount;
    }

    public void setSeenCount(Integer seenCount) {
        this.seenCount = seenCount;
    }

    public String getUserSubmitAmount() {
        return userSubmitAmount;
    }

    public void setUserSubmitAmount(String userSubmitAmount) {
        this.userSubmitAmount = userSubmitAmount;
    }
}
