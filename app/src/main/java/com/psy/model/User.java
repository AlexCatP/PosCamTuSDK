package com.psy.model;

public class User {

    private int userID;
    private String loginName;
    private String userName;
    private String userphone;
    private String password;
    private String userPicUrl;
    private int pb;

    public User() {

    }

    public User(int userID, String loginName, String password) {
        this.userID = userID;
        this.loginName = loginName;
        this.password = password;
    }

    public User(String loginName, String password) {
        this.loginName = loginName;
        this.password = password;
    }


    public String getUserName() {
        return userName;
    }

    public String getPassword() {
        return password;
    }

    public int getUserID() {
        return userID;
    }

    public String getUserPicUrl() {
        return userPicUrl;
    }


    /**
     * @param userName userName
     */
    public void setUserName(String userName) {
        this.userName = userName;
    }

    /**
     * @param password password
     */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * @param userID userID
     */
    public void setUserID(int userID) {
        this.userID = userID;
    }

    /**
     * @param userPicUrl
     */
    public void setUserPicUrl(String userPicUrl) {
        this.userPicUrl = userPicUrl;
    }

    public int getPb() {
        return pb;
    }

    public void setPb(int pb) {
        this.pb = pb;
    }

    public String getLoginName() {
        return loginName;
    }

    public void setLoginName(String loginName) {
        this.loginName = loginName;
    }

    public String getUserphone() {
        return userphone;
    }

    public void setUserphone(String userphone) {
        this.userphone = userphone;
    }

    @Override
    public String toString() {
        return "User{" +
                "userID=" + userID +
                ", loginName='" + loginName + '\'' +
                ", userName='" + userName + '\'' +
                ", userphone='" + userphone + '\'' +
                ", password='" + password + '\'' +
                ", userPicUrl='" + userPicUrl + '\'' +
                ", pb=" + pb +
                '}';
    }
}