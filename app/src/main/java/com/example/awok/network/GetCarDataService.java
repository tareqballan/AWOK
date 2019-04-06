package com.example.awok.network;

import com.example.awok.model.CarList;

import retrofit2.Call;
import retrofit2.http.GET;

public interface GetCarDataService {
    @GET("carsonline")
    Call<CarList> getCarData();
}
