package com.example.awok.model;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class CarList {
    @SerializedName("Cars")
    private ArrayList<Car> carsList;

    @SerializedName("RefreshInterval")
    private int RefreshInterval;

    @SerializedName("Ticks")
    private String Ticks;


    public void setCarsList(ArrayList<Car> carsList) {
        this.carsList = carsList;
    }

    public void setRefreshInterval(int refreshInterval) {
        RefreshInterval = refreshInterval;
    }

    public void setTicks(String ticks) {
        Ticks = ticks;
    }

    public int getRefreshInterval() {
        return RefreshInterval;
    }

    public String getTicks() {
        return Ticks;
    }

    public ArrayList<Car> getCarsList() {
        return carsList;
    }
}
