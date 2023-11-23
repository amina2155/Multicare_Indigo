package com.example.multicare_indigo;

public class Contacts {
    public String blood, name, number;
    public String serial;
    public Contacts(String s, String b,String n, String num)
    {
        this.serial = s;
        this.blood = b;
        this.name = n;
        this.number = num;
    }
    public Contacts()
    {
        this.serial = null;
        this.blood = null;
        this.name = null;
        this.number = null;
    }
}
