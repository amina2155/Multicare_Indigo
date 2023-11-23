package com.example.multicare_indigo;

public class UserInfo {
    private String name, email, phone_no, blood, gender, password, emergency_count;

    public UserInfo(String name, String email, String password, String phone_no, String blood, String gender, String em) {
        this.name = name;
        this.email = email;
        this.password = password;
        this.phone_no = phone_no;
        this.blood = blood;
        this.gender = gender;
        this.emergency_count = em;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setEmergency_count(String emergency_count)
    {
        this.emergency_count = emergency_count;
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

    public String getPhone_no() {
        return phone_no;
    }

    public void setPhone_no(String phone_no) {
        this.phone_no = phone_no;
    }

    public String getBlood() {
        return blood;
    }

    public void setBlood(String blood) {
        this.blood = blood;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }
}
