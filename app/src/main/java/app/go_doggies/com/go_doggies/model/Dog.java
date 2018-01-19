package app.go_doggies.com.go_doggies.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by anto004 on 1/18/18.
 */

public class Dog {

    @SerializedName("dog_id")
    private String dogId;
    @SerializedName("dog_name")
    private String dogName;
    @SerializedName("dog_img")
    private String dogImg;
    @SerializedName("dog_size")
    private String dogSize;
    @SerializedName("dog_hair_type")
    private String dogHairType;

    public String getDogId() {
        return dogId;
    }

    public void setDogId(String dogId) {
        this.dogId = dogId;
    }

    public String getDogName() {
        return dogName;
    }

    public void setDogName(String dogName) {
        this.dogName = dogName;
    }

    public String getDogImg() {
        return dogImg;
    }

    public void setDogImg(String dogImg) {
        this.dogImg = dogImg;
    }

    public String getDogSize() {
        return dogSize;
    }

    public void setDogSize(String dogSize) {
        this.dogSize = dogSize;
    }

    public String getDogHairType() {
        return dogHairType;
    }

    public void setDogHairType(String dogHairType) {
        this.dogHairType = dogHairType;
    }

    @Override
    public String toString() {
        return "Dog{" +
                "dogId='" + dogId + '\'' +
                ", dogName='" + dogName + '\'' +
                ", dogImg='" + dogImg + '\'' +
                ", dogSize='" + dogSize + '\'' +
                ", dogHairType='" + dogHairType + '\'' +
                '}';
    }
}
