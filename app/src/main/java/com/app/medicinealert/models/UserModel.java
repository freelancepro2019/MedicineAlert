package com.app.medicinealert.models;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.io.Serializable;

public class UserModel implements Serializable {
    private String user_id;
    private String first_name;
    private String last_name;
    private String phone_code;
    private String phone;
    private String email;
    private String password;

    public UserModel() {
    }

    public UserModel(String user_id, String first_name, String last_name, String phone_code, String phone, String email, String password) {
        this.user_id = user_id;
        this.first_name = first_name;
        this.last_name = last_name;
        this.phone_code = phone_code;
        this.phone = phone;
        this.email = email;
        this.password = password;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getFirst_name() {
        return first_name;
    }

    public void setFirst_name(String first_name) {
        this.first_name = first_name;
    }

    public String getLast_name() {
        return last_name;
    }

    public void setLast_name(String last_name) {
        this.last_name = last_name;
    }

    public String getPhone_code() {
        return phone_code;
    }

    public void setPhone_code(String phone_code) {
        this.phone_code = phone_code;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
