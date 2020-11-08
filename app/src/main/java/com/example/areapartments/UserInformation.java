package com.example.areapartments;

public class UserInformation {


    public String newName;
    public  double latitude;
    public  double longitude;
    public String newNumber;
    public String bedrooms;


    public UserInformation(){

    }
    public UserInformation(String newName, double latitude, double longitude, String newNumber, String bedrooms){

        this.newName = newName;
        this.latitude = latitude;
        this.longitude = longitude;
        this.newNumber = newNumber;
        this.bedrooms = bedrooms;

    }

}
