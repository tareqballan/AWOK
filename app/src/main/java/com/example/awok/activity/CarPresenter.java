package com.example.awok.activity;
import android.content.SharedPreferences;
import android.os.Handler;

import com.example.awok.interfaces.ICar;
import com.example.awok.model.Car;

import java.util.ArrayList;

import static android.content.Context.MODE_PRIVATE;



/*This class is responsible to act as the middle man between View and Model.
* It retrieves data from the Model and returns it formatted to the View and
 * it also decides what happens when you interact with the View.*/

public class CarPresenter implements ICar.presenter, ICar.GetCarsData.OnFinishedListener {

    private ICar.MainView mainView;
    private ICar.GetCarsData getCarsData;

    public CarPresenter(ICar.MainView mainView, ICar.GetCarsData getCarsData) {
        this.mainView = mainView;
        this.getCarsData = getCarsData;

    }

    @Override
    public void onDestroy() {
        mainView = null;
    }

    @Override
    public void onRefresh() {
        getCarsData.getCarsArrayList(this);
        if(mainView != null){
            mainView.hideSwipeRefresh();
        }
    }

    @Override
    public void requestDataFromServer() {
        if(mainView != null){
            mainView.showProgress();
        }
        getCarsData.getCarsArrayList(this);
    }

    @Override
    public void onFinished(ArrayList<Car> cars, long ticks) {
        if(mainView != null){
            mainView.setDataToRecyclerView(cars,ticks);
            mainView.hideProgress();
            mainView.updateScreenTimer();

        }
    }

    @Override
    public void onFailure(Throwable throwable) {
        if(mainView != null){
            mainView.onResponseFailure(throwable);
            mainView.hideProgress();
        }
    }

    @Override
    public void onFlipGridList() {
        if(mainView != null)
            mainView.flipGridList();
    }

    @Override
    public void updateScreenTimer() {
        if (mainView != null){
            mainView.updateScreenTimer();
        }
    }
}
