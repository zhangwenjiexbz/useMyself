package com.example.highlevel.pojo;

/**
 * @author Sebastian
 */
public class TestUser {
    private String name;
    public volatile int age;

    public TestUser() {
    }

    public TestUser(String name, int age) {
        this.name = name;
        this.age = age;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }
}
