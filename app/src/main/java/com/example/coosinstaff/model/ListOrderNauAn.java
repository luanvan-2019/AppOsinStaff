package com.example.coosinstaff.model;

public class ListOrderNauAn {

    private String orderType;
    private Integer price;
    private String location;
    private String nearby;
    private String date;
    private String time;
    private String peopleAmount;
    private String dishAmount;
    private String taste;
    private String fruit;
    private String market;
    private Integer marketPrice;
    private String createAt;
    private Integer seenCount;

    public ListOrderNauAn(String orderType, Integer price, String location, String nearby, String date, String time,
                          String peopleAmount, String dishAmount, String taste, String fruit, String market,
                          Integer marketPrice, String createAt, Integer seenCount) {
        this.orderType = orderType;
        this.price = price;
        this.location = location;
        this.nearby = nearby;
        this.date = date;
        this.time = time;
        this.peopleAmount = peopleAmount;
        this.dishAmount = dishAmount;
        this.taste = taste;
        this.fruit = fruit;
        this.market = market;
        this.marketPrice = marketPrice;
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

    public String getPeopleAmount() {
        return peopleAmount;
    }

    public void setPeopleAmount(String peopleAmount) {
        this.peopleAmount = peopleAmount;
    }

    public String getDishAmount() {
        return dishAmount;
    }

    public void setDishAmount(String dishAmount) {
        this.dishAmount = dishAmount;
    }

    public String getTaste() {
        return taste;
    }

    public void setTaste(String taste) {
        this.taste = taste;
    }

    public String getFruit() {
        return fruit;
    }

    public void setFruit(String fruit) {
        this.fruit = fruit;
    }

    public String getMarket() {
        return market;
    }

    public void setMarket(String market) {
        this.market = market;
    }

    public Integer getMarketPrice() {
        return marketPrice;
    }

    public void setMarketPrice(Integer marketPrice) {
        this.marketPrice = marketPrice;
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
