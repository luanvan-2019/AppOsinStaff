package com.example.coosinstaff.model;

public class ListOrderDK {

    private String orderType;
    private Integer price;
    private String location;
    private String nearby;
    private String dateStart;
    private String dateEnd;
    private String schedule;
    private String time;
    private String createAt;
    private Integer seenCount;

    public ListOrderDK(String orderType, int price, String location, String nearby, String dateStart,
                       String dateEnd, String schedule, String time, String createAt, Integer seenCount) {
        this.orderType = orderType;
        this.price = price;
        this.location = location;
        this.nearby = nearby;
        this.dateStart = dateStart;
        this.dateEnd = dateEnd;
        this.schedule = schedule;
        this.time = time;
        this.createAt = createAt;
        this.seenCount = seenCount;
    }

    public String getOrderType() {
        return orderType;
    }

    public void setOrderType(String orderType) {
        this.orderType = orderType;
    }

    public Integer getPrice() {
        return price;
    }

    public void setPrice(Integer price) {
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

    public String getDateStart() {
        return dateStart;
    }

    public void setDateStart(String dateStart) {
        this.dateStart = dateStart;
    }

    public String getDateEnd() {
        return dateEnd;
    }

    public void setDateEnd(String dateEnd) {
        this.dateEnd = dateEnd;
    }

    public String getSchedule() {
        return schedule;
    }

    public void setSchedule(String schedule) {
        this.schedule = schedule;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
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
}
