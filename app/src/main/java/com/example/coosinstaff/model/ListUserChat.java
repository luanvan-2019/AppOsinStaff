package com.example.coosinstaff.model;

public class ListUserChat {

    private String name;
    private String phone;
    private Integer id;

    public ListUserChat(String name, String phone,Integer id) {
        this.name = name;
        this.phone = phone;
        this.id = id;
    }

    public ListUserChat() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }
}
