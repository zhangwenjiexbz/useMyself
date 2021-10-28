package com.example.highlevel.pojo;

import java.io.Serializable;

/**
 * @author MLKJ
 */
public class TestPojo implements Serializable {
    
    private String name;
    private String message; 
    private Integer age;

    public TestPojo() {
    }

    public TestPojo(String name, String message, Integer age) {
        this.name = name;
        this.message = message;
        this.age = age;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    @Override
    public String toString() {
        return "TestPojo{" +
                "name='" + name + '\'' +
                ", message='" + message + '\'' +
                '}';
    }
}
