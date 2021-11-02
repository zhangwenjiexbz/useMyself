package com.example.highlevel.pojo;

import cn.afterturn.easypoi.excel.annotation.Excel;
import org.springframework.stereotype.Component;

import java.io.Serializable;

/**
 * @author Sebastian
 */
@Component
public class EasyPoiCustomerInfo implements Serializable {

    @Excel(name = "id",width = 25,orderNum = "0")
    private int id;
    
    @Excel(name = "name",width = 25,orderNum = "0")
    private String name;

    @Excel(name = "phone",width = 25,orderNum = "0")
    private String phone;

    @Excel(name = "age",width = 25,orderNum = "0")
    private int age;

    @Excel(name = "address",width = 25,orderNum = "0")
    private String address;

    public EasyPoiCustomerInfo() {
    }

    public EasyPoiCustomerInfo(int id, String name, String phone, int age, String address) {
        this.id = id;
        this.name = name;
        this.phone = phone;
        this.age = age;
        this.address = address;
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

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "EasyPoiCustomerInfo{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", phone='" + phone + '\'' +
                ", age=" + age +
                ", address='" + address + '\'' +
                '}';
    }
}
