package com.example.catdog;


import android.graphics.Bitmap;

public class Animal {

    private String name;
    private String type;
    private int age;
    private double  weight;
    private Bitmap image;


    public Animal(String name, String type, int age, double weight, Bitmap image) {

        this.name = name;
        this.type = type;
        this.age = age;
        this.weight=weight;
        this.image=image;

    }


    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }
    public void setType(String type) {
        this.type = type;
    }

    public int getAge() {
        return age;
    }
    public void setAge(int age) {
        this.age = age;
    }
    public double getWeight() {
        return weight;
    }
    public void setWeight(double weight) {
        this.weight = weight;
    }
    public Bitmap getImage() {
        return image;
    }



}
