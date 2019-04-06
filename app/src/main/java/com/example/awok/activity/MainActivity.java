package com.example.awok.activity;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;
import com.example.awok.R;
import com.example.awok.adapter.CarAdapter;
import com.example.awok.interfaces.ICar;
import com.example.awok.model.Car;
import com.google.android.material.button.MaterialButton;
import java.util.ArrayList;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

public class MainActivity extends AppCompatActivity implements ICar.MainView {

    private ProgressBar progressBar;
    private RecyclerView recyclerView;
    private CarAdapter adapter;
    Toolbar toolbar;
    private SwipeRefreshLayout swipeContainer;
    private MaterialButton btn_grid;
    private Context context;
    private ICar.presenter presenter;
    public static int viewType=1;
    Handler handler=null;
    Runnable runnable=null;
    public static final String MY_PREFS_NAME = "MyPrefFile";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        context = this;
        adapter = new CarAdapter();
        btn_grid = (MaterialButton) findViewById(R.id.btn_gridview);
        setupToolbarAndRecyclerView();
        setupSwipeRefreshLayout();
        setupProgressBar();

        presenter = new CarPresenter(this,new GetCarsDataImp());
        presenter.requestDataFromServer();

        btn_grid.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                presenter.onFlipGridList();

            }
        });
        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                presenter.onRefresh();
                handler.removeCallbacks(runnable);
            }
        });

    }

    private void setupToolbarAndRecyclerView(){

        toolbar = (Toolbar) findViewById(R.id.tool_bar);
        toolbar.setTitle(R.string.app_name);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        try{
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setDisplayShowHomeEnabled(true);
        }catch (NullPointerException ex){
            Log.e("Äction Bar Exception",ex.getMessage());
        }

        recyclerView = (RecyclerView) findViewById(R.id.recycler_view_car_list);
        if(viewType==2)//Grid View
        {
            btn_grid.setIconResource(R.drawable.ic_list_icon);
            btn_grid.setText(R.string.list);
            RecyclerView.LayoutManager layoutManager =new GridLayoutManager(this, 2);
            recyclerView.setLayoutManager(layoutManager);
        }else{ // Normal View
            btn_grid.setIconResource(R.drawable.ic_grid);
            btn_grid.setText(R.string.grid);
            RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(MainActivity.this);
            recyclerView.setLayoutManager(layoutManager);
        }

    }

    private void setupSwipeRefreshLayout(){
        swipeContainer = (SwipeRefreshLayout) findViewById(R.id.swipeContainer);
        swipeContainer.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);
    }

   public void setupProgressBar(){
        progressBar = (ProgressBar) findViewById(R.id.progress_circular);
   }
    @Override
    public void setDataToRecyclerView(ArrayList<Car> cars, long ticks) {
        adapter = new CarAdapter(cars,ticks);
        recyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }

    @Override
    public void showProgress() {
        if(progressBar.getVisibility() == View.INVISIBLE)
            progressBar.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideProgress() {
        if(progressBar.getVisibility() == View.VISIBLE)
            progressBar.setVisibility(View.INVISIBLE);
    }

    @Override
    public void onResponseFailure(Throwable throwable) {
        Toast.makeText(MainActivity.this,
                "Something went wrong...Error message: " + throwable.getMessage(),
                Toast.LENGTH_LONG).show();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        presenter.onDestroy();
    }

    @Override
    public void hideSwipeRefresh() {
        swipeContainer.setRefreshing(false);
    }


    @Override
    public void flipGridList() {
        if(MainActivity.viewType==1)
            MainActivity.viewType=2;
        else
            MainActivity.viewType=1;
        recreate();
    }

    @Override
    public void updateScreenTimer() {
        if(handler!=null){
            handler.removeCallbacks(runnable);
        }
        if(handler==null){
            handler = new Handler();
            runnable = new Runnable() {
                @Override
                public void run() {
                    DateCalculator dateCalculator=new DateCalculator();
                    SharedPreferences prefs = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
                    long ticks_difference = prefs.getLong("ticks_difference", 0L);

                    if(ticks_difference == 0L ){ // First time
                        long difference = dateCalculator.getDateDifference(adapter.getTicksValue());
                        adapter.setTicks(dateCalculator.getDateDifference(difference)-1000L);
                        SharedPreferences.Editor editor = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE).edit();
                        editor.putLong("ticks_difference", difference-1000L);
                        editor.apply();
                        adapter.notifyDataSetChanged();
                    }else if(ticks_difference <= 1000L){

                        presenter.updateCarsListOnTimerFinished();
                        long difference = dateCalculator.getDateDifference(adapter.getTicksValue());
                        SharedPreferences.Editor editor = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE).edit();
                        editor.putLong("ticks_difference",difference);
                        editor.apply();
                        adapter.notifyDataSetChanged();
                    } else{
                        Log.e("ticks_difference",ticks_difference+"");
                        adapter.setTicks(ticks_difference-1000L);
                        SharedPreferences.Editor editor = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE).edit();
                        editor.putLong("ticks_difference", ticks_difference-1000L);
                        editor.apply();
                        adapter.notifyDataSetChanged();
                    }
                    handler.postDelayed(this, 1000);
                }
            };
        }
        handler.post(runnable);
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onStop() {
        super.onStop();
        handler.removeCallbacks(runnable);
    }

    @Override
    protected void onPostResume(){
        super.onPostResume();
        presenter.updateScreenTimer();
    }

    @Override
    public boolean onSupportNavigateUp() {
        return super.onSupportNavigateUp();
    }
}
