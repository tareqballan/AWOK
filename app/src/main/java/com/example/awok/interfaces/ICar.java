package com.example.awok.interfaces;

import com.example.awok.model.Car;

import java.util.ArrayList;

public interface ICar {

    /*
    *   Call When The User Interacts With The View, And When View onDestroy()
    */
     interface presenter{
         void onDestroy();
         void onRefresh();
         void requestDataFromServer();
         void updateCarsListOnTimerFinished();
         void updateScreenTimer();
         void onFlipGridList();
    }

    /*
     * showProgress() and hideProgress() would be used for displaying and hiding the progressBar
     * while the setDataToRecyclerView and onResponseFailure is fetched from the GetCarsDataImp class
    */
    interface MainView{
         void showProgress();
         void hideProgress();
         void hideSwipeRefresh();
         void setDataToRecyclerView(ArrayList<Car> cars,long ticks);
         void updateScreenTimer();
         void flipGridList();
         void onResponseFailure(Throwable throwable);
    }

    /*
    * Fetching Data
    */
    interface GetCarsData{
        interface OnFinishedListener{
            void onFinished(ArrayList<Car> cars, long ticks);
            void onFailure(Throwable throwable);
        }
        void getCarsArrayList(OnFinishedListener onFinishedListener);
    }
}
