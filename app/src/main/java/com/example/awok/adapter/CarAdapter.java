package com.example.awok.adapter;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import com.example.awok.R;
import com.example.awok.activity.DateCalculator;
import com.example.awok.activity.MainActivity;
import com.example.awok.model.Car;
import com.squareup.picasso.Picasso;
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

        holder.txtLotValue.setText(dataList.get(position).getAuctionInfo().getLot());
        holder.txtBidsValue.setText(dataList.get(position).getAuctionInfo().getBids());
        holder.txtPrice.setText(dataList.get(position).getAuctionInfo().getCurrentPrice());

        /* Dealing with different languages */
       if(Locale.getDefault().getLanguage().equals(new Locale("en").getLanguage())){
           holder.txtCarModel.setText(dataList.get(position).getEnglishCardTitle());
           holder.txtCurrency.setText(dataList.get(position).getAuctionInfo().getCurrencyEn());
       }else if (Locale.getDefault().getLanguage().equals(new Locale("ar").getLanguage())){
           holder.txtCarModel.setText(dataList.get(position).getArabicCardTitle());
           holder.txtCurrency.setText(dataList.get(position).getAuctionInfo().getCurrencyAr());
       }

        // Dealing with the timer
        HashMap<String,String> timerValue = getTimeValue();
        if(timerValue.get("colored").equals("1"))
            holder.txtTimeValue.setTextColor(Color.RED);
        holder.txtTimeValue.setText(timerValue.get("time"));

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

    /* Get screen metrics */
    private HashMap<String,Integer> getScreenMetrics(Context context){

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

    /*
    * This function get the timer value and decides if the text will appear in Red or not.
    * */
    public HashMap<String,String> getTimeValue(){
        HashMap<String,String> result = new HashMap<String,String>();
        /* The text by default will be colored in Black*/
        result.put("colored","0");
        DateCalculator dateCalculator=new DateCalculator();


        SimpleDateFormat formatTime = new SimpleDateFormat("HH:mm:ss");
        SimpleDateFormat formatDate = new SimpleDateFormat("yyyy-MM-dd");
        Date currentDate = new Date(System.currentTimeMillis());
        Date serverTime;

        SharedPreferences prefs = context.getSharedPreferences(MainActivity.MY_PREFS_NAME, MODE_PRIVATE);
        long ticks_difference = prefs.getLong("ticks_difference", 0L);

        if(ticks_difference==0L){
            long timeDifference = dateCalculator.getDateDifference(ticks);

            /* If the timer value is under 5 minutes*/
            if(timeDifference<300000)
                result.put("colored","1");
            result.put("time",dateCalculator.getFormattedTime(timeDifference));
            return result;

        }else if(ticks_difference < 0L){
            result.put("colored","1");
            result.put("time","00:00:00");
            return result;
        }else{
            long diff = ticks_difference - 1000L;
            /* If the timer value is under 5 minutes*/
            if(diff<300000)
                result.put("colored","1");
            Date d = new Date(diff);
            result.put("time",dateCalculator.getFormattedTime(diff));
            return result;
        }
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


}
