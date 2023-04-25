package com.example.catdog;


import android.graphics.Bitmap;
import android.os.Parcel;
import android.os.Parcelable;

public class Animal implements Parcelable {

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

    protected Animal(Parcel in) {
        name = in.readString();
        type =in.readString();
        age = in.readInt();
        weight=in.readDouble();
        image=in.readParcelable(Bitmap.class.getClassLoader());

    }
    public static final Creator<Animal> CREATOR = new Creator<Animal>() {
        @Override
        public Animal createFromParcel(Parcel in) {
            return new Animal(in);
        }
        public Animal[] newArray(int size) {
            return new Animal[size];
        }
    };
    public String getName() {
        return name;
    }


    public String getType() {
        return type;
    }


    public int getAge() {
        return age;
    }

    public double getWeight() {
        return weight;
    }

    public Bitmap getImage() {
        return image;}
    public int describeContents() {
        return 0;
    }
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(name);
        parcel.writeString(type);
        parcel.writeInt(age);
        parcel.writeDouble(weight);
        parcel.writeParcelable(image, i);
    }
/*
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

*/

}