package com.shuzijun.lc.model;

public class User {

    private String username;


    private String userSlug;

    private boolean isPremium;

    private boolean isSignedIn = Boolean.FALSE;

    private boolean isVerified;

    private boolean isPhoneVerified;

    private String realName;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getUserSlug() {
        return userSlug;
    }

    public void setUserSlug(String userSlug) {
        this.userSlug = userSlug;
    }

    public boolean isPremium() {
        return isPremium;
    }

    public void setPremium(Boolean premium) {
        isPremium = premium != null && premium;
    }

    public boolean isVerified() {
        return isVerified;
    }

    public void setVerified(Boolean verified) {
        isVerified = verified != null && verified;
    }

    public boolean isPhoneVerified() {
        return isPhoneVerified;
    }

    public void setPhoneVerified(Boolean phoneVerified) {
        isPhoneVerified = phoneVerified != null && phoneVerified;
    }

    public boolean isSignedIn() {
        return isSignedIn;
    }

    public void setSignedIn(Boolean signedIn) {
        isSignedIn = signedIn != null && signedIn;
    }

    public String getRealName() {
        return realName;
    }

    public void setRealName(String realName) {
        this.realName = realName;
    }
}
