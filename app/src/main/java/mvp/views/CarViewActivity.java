package mvp.views;

import android.app.Activity;
import android.os.Bundle;
import android.widget.Toast;

import com.example.awok.R;

import mvp.presenters.CarsPresenter;
import mvp.views.interfaces.ICarView;

public class CarViewActivity extends Activity implements ICarView {

    CarsPresenter carsPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hello_view);
        carsPresenter = new CarsPresenter();
        carsPresenter.setView(this);


    }

    @Override
    public void showData(String name) {
        Toast.makeText(this,name,Toast.LENGTH_LONG).show();
    }

    @Override
    public void showProgress() {

    }

    @Override
    public void hideProgress() {

    }
}
