package com.example.catdog;


import android.graphics.Bitmap;
import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;


public class Animal implements Parcelable {
    private String key;
    private String id;
    private String name;
    private String type;
    private int age;
    private double weight;
    // private String photoUrl;
    private String  imageUrl;
    private Bitmap image;

    public Animal() {}
    public Animal(String id, String name, String type, int age, double weight, String  imageUrl) {
        this.id = id;
        this.name = name;
        this.type = type;
        this.age = age;
        this.weight = weight;
        this. imageUrl =  imageUrl;
    }

    protected Animal(Parcel in) {
        id = in.readString();
        name = in.readString();
        type = in.readString();
        age = in.readInt();
        weight = in.readDouble();
        imageUrl = in.readString();
    }

    public static final Creator<Animal> CREATOR = new Creator<Animal>() {
        @Override
        public Animal createFromParcel(Parcel in) {
            return new Animal(in);
        }

        @Override
        public Animal[] newArray(int size) {
            return new Animal[size];
        }
    };

    public String getId() {
        return id;
    }

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
    public String getWeightFormatted() {
        return String.format(Locale.getDefault(), "%.2f", weight);
    }

    public String getImageUrl() {
        return  imageUrl;
    }

    public void setImageUri(String imageUri) {
        this. imageUrl =  imageUrl;
    }

    @Override
    public int describeContents() {
        return 0;
    }
    public Bitmap getImage() {
        return image;
    }

    public void setImage(Bitmap image) {

        this.image = image;
    }
    public String getKey() {
        return key;

    }
    public void setKey(String key) {
        this.key = key;
    }
    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(id);
        parcel.writeString(name);
        parcel.writeString(type);
        parcel.writeInt(age);
        parcel.writeDouble(weight);
        parcel.writeString( imageUrl);
    }
    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("id", id);
        result.put("name", name);
        result.put("type", type);
        result.put("age", age);
        result.put("weight", weight);
        result.put("imageUrl",  imageUrl);

        return result;
    }
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


