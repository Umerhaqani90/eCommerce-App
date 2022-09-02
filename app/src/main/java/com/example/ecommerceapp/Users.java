package com.example.ecommerceapp;

public class Users {
    private String phone,name,password,email;

    public Users() {

    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Users(String phone, String name, String password, String email) {
        this.phone = phone;
        this.name = name;
        this.password = password;
        this.email = email;

    }
}
