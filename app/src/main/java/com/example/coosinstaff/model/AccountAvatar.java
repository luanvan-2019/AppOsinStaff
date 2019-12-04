package com.example.coosinstaff.model;

import com.google.firebase.database.Exclude;

public class AccountAvatar {

    private String acountPhone;
    private String imageUrl;
    private String mKey;

    public AccountAvatar() {
    }

    public AccountAvatar(String acountPhone, String imageUrl) {
        this.acountPhone = acountPhone;
        this.imageUrl = imageUrl;
    }

    public String getAcountPhone() {
        return acountPhone;
    }

    public void setAcountPhone(String acountPhone) {
        this.acountPhone = acountPhone;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    @Exclude
    public String getKey() {
        return mKey;
    }

    @Exclude
    public void setKey(String key) {
        mKey = key;
    }
}
