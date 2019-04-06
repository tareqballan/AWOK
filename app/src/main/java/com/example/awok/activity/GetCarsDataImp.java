package com.example.awok.activity;

import android.util.Log;
import com.example.awok.interfaces.ICar;
import com.example.awok.model.CarList;
import com.example.awok.network.GetCarDataService;
import com.example.awok.network.RetrofitInstance;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/*
 * This class is responsible for fetching data using Retrofit, and passing the data through onFinishedListener callback.
*/
public class GetCarsDataImp implements ICar.GetCarsData {
        @Override
        public void getCarsArrayList(final OnFinishedListener onFinishedListener) {
            /*Create handle for the RetrofitInstance interface*/
            GetCarDataService service = RetrofitInstance.getRetrofitInstance().create(GetCarDataService.class);
            /*Call the method with parameter in the interface to get the cars data*/
            Call<CarList> call = service.getCarData();
            /*Log the URL called*/
            Log.wtf("URL Called", call.request().url() + "");

            call.enqueue(new Callback<CarList>() {
                @Override
                public void onResponse(Call<CarList> call, Response<CarList> response) {
                    onFinishedListener.onFinished(response.body().getCarsList(),Long.parseLong(response.body().getTicks()));
                }
                @Override
                public void onFailure(Call<CarList> call, Throwable t) {
                    onFinishedListener.onFailure(t);
                }
            });
        }

}
