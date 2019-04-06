package com.example.awok.adapter;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import com.example.awok.R;
import com.example.awok.activity.MainActivity;
import com.example.awok.model.Car;
import com.squareup.picasso.Picasso;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;

import androidx.recyclerview.widget.RecyclerView;

import static android.content.Context.MODE_PRIVATE;

public class CarAdapter extends RecyclerView.Adapter<CarAdapter.CarViewHolder> {
    private ArrayList<Car> dataList;
    private long ticks;
    Context context;

    public CarAdapter(){
        super();
        ticks = 0L;
    }
    public CarAdapter(ArrayList<Car> dataList, long ticks) {
        this.dataList = dataList;
        this.ticks = ticks;
    }
    public ArrayList<Car> getDataList(){
        return dataList;
    }
    public void setTicks(long ticks){
        this.ticks = ticks;
    }
    public long getTicksValue(){
        return this.ticks;
    }

    @Override
    public CarViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view;
        if(MainActivity.viewType==2){
             view = layoutInflater.inflate(R.layout.car_card_item_grid, parent, false);
        }else{
             view = layoutInflater.inflate(R.layout.car_card_item, parent, false);
        }

        context = view.getContext();
        return new CarViewHolder(view);
    }

    @Override
    public void onBindViewHolder(CarViewHolder holder, int position) {

        /* Dealing with different languages */
        holder.txtLotValue.setText(dataList.get(position).getAuctionInfo().getLot());
        holder.txtBidsValue.setText(dataList.get(position).getAuctionInfo().getBids());
        holder.txtPrice.setText(dataList.get(position).getAuctionInfo().getCurrentPrice());

       if(Locale.getDefault().getLanguage().equals(new Locale("en").getLanguage())){
           holder.txtCarModel.setText(dataList.get(position).getEnglishCardTitle());
           holder.txtCurrency.setText(dataList.get(position).getAuctionInfo().getCurrencyEn());
       }else if (Locale.getDefault().getLanguage().equals(new Locale("ar").getLanguage())){
           holder.txtCarModel.setText(dataList.get(position).getArabicCardTitle());
           holder.txtCurrency.setText(dataList.get(position).getAuctionInfo().getCurrencyAr());
       }

        //TODO time value



        Date d = new Date(ticks);
        SimpleDateFormat formatTime = new SimpleDateFormat("HH:mm:ss");
        holder.txtTimeValue.setText(formatTime.format(d.getTime()));



        SimpleDateFormat formatDate = new SimpleDateFormat("yyyy-MM-dd");
        //SimpleDateFormat formatTime = new SimpleDateFormat("HH:mm:ss");
        Date currentDate = new Date(System.currentTimeMillis());
        Date serverTime;
        SharedPreferences prefs = context.getSharedPreferences(MainActivity.MY_PREFS_NAME, MODE_PRIVATE);
        long ticks_difference = prefs.getLong("ticks_difference", 0L);
        if(ticks_difference==0L){ /// First time
            serverTime = new Date(ticks);
            String temp =  formatDate.format(currentDate) +" "+ formatTime.format(serverTime);
            Log.e("combination in first",temp);
            Date synchDate = new Date();
            String diff = "";
            try {
                synchDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(temp);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            diff = formatTime.format(synchDate.getTime()-currentDate.getTime());
            holder.txtTimeValue.setText(diff);
            SharedPreferences.Editor editor = context.getSharedPreferences(MainActivity.MY_PREFS_NAME, MODE_PRIVATE).edit();
            editor.putLong("ticks_difference", ticks - 1000L);
            editor.apply();
        }else{
            long diff = ticks_difference - 1000L;
            Log.e("difffff",diff+"");
            //Date d = new Date(diff);
            holder.txtTimeValue.setText(formatTime.format(d.getTime()));
            Log.e("second",formatTime.format(d.getTime()));
            SharedPreferences.Editor editor = context.getSharedPreferences(MainActivity.MY_PREFS_NAME, MODE_PRIVATE).edit();
            editor.putLong("ticks_difference", ticks_difference - 1000L);
            editor.apply();
        }
//
//        String temp =  formatDate.format(currentDate) +" "+ formatTime.format(serverTime);
//        Log.e("combination",temp);
//        String diff="";
//        Date serverDate = new Date();
//
//        try {
//            serverDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(temp);
////             diff = formatTime.format
////                     (new Date(ticks).getTime()- serverDate.getTime());
//
//            diff = formatTime.format
//                    (serverDate.getTime()-currentDate.getTime());
//        } catch (ParseException e) {
//            e.printStackTrace();
//        }

//        holder.txtTimeValue.setText(diff);


//        if(ticks_difference==0L)
//            holder.txtTimeValue.setText(diff);
//        else
//            holder.txtTimeValue.setText(formatTime.format(ticks_difference));
//        SharedPreferences.Editor editor = context.getSharedPreferences(MainActivity.MY_PREFS_NAME, MODE_PRIVATE).edit();
//        editor.putLong("ticks_difference", ticks_difference);
//        editor.apply();



        /* Replacing Image Width & Height */
        try{
            HashMap<String,Integer> screenMetrics = getScreenMetrics(context);
            if ((screenMetrics.get("width") > 1023) || (screenMetrics.get("height") > 1023)){
                String url = dataList.get(position).getImage().replace("[w]","300");
                url = url.replace("[h]","300");
                Log.d("image ",url);
                Picasso.get().load(url).into(holder.carImage);
            }else{
                String url = dataList.get(position).getImage().replace("[w]","150");
                url = dataList.get(position).getImage().replace("[h]","150");
                Picasso.get().load(url).into(holder.carImage);
            }
        }catch (NullPointerException ex){
            Log.e("HashMap Exception", ex.getMessage());
        }
    }
    @Override
    public int getItemCount() {
        return dataList.size();
    }

    class CarViewHolder extends RecyclerView.ViewHolder{
        ImageView carImage;
        TextView txtCarModel, txtPrice, txtCurrency, txtLotValue, txtBidsValue, txtTimeValue;
        CarViewHolder(View itemView){
            super(itemView);
            carImage = (ImageView) itemView.findViewById(R.id.car_image);
            txtCarModel = (TextView) itemView.findViewById(R.id.txt_car_model);
            txtPrice = (TextView) itemView.findViewById(R.id.txt_price);
            txtCurrency = (TextView) itemView.findViewById(R.id.txt_currency);
            txtLotValue = (TextView) itemView.findViewById(R.id.txt_lot_value);
            txtBidsValue = (TextView) itemView.findViewById(R.id.txt_bids_value);
            txtTimeValue = (TextView) itemView.findViewById(R.id.txt_time_value);
        }
    }

    /* Get screen metrics */
    public HashMap<String,Integer> getScreenMetrics(Context context){

        DisplayMetrics metrics = new DisplayMetrics();
        WindowManager windowManager = (WindowManager) context
                .getSystemService(Context.WINDOW_SERVICE);
        try {
            windowManager.getDefaultDisplay().getMetrics(metrics);
        }catch (NullPointerException ex){
            Log.e("Metrics Exception", ex.getMessage());
        }
        int width = metrics.widthPixels;
        int height = metrics.heightPixels;
        HashMap<String,Integer> result = new HashMap<String,Integer>();
        result.put("width",width);
        result.put("height",height);
        return result;
    }
}
