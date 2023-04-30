package com.example.catdog;


import android.graphics.Bitmap;
import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class Animal implements Parcelable {
    private String id;
    private String name;
    private String type;
    private int age;
    private double weight;
    // private String photoUrl;
    private String imageUri;
    private Bitmap image;

    public Animal() {}
    public Animal(String id, String name, String type, int age, double weight, String imageUri) {
        this.id = id;
        this.name = name;
        this.type = type;
        this.age = age;
        this.weight = weight;
        this.imageUri = imageUri;
    }

    protected Animal(Parcel in) {
        id = in.readString();
        name = in.readString();
        type = in.readString();
        age = in.readInt();
        weight = in.readDouble();
        imageUri = in.readString();
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

    public String getImageUrl() {
        return imageUri;
    }

    public void setImageUri(String imageUri) {
        this.imageUri = imageUri;
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
    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(id);
        parcel.writeString(name);
        parcel.writeString(type);
        parcel.writeInt(age);
        parcel.writeDouble(weight);
        parcel.writeString(imageUri);
    }
    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("id", id);
        result.put("name", name);
        result.put("type", type);
        result.put("age", age);
        result.put("weight", weight);
        result.put("imageUrl", imageUri);

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


