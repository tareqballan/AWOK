package mvp.presenters;

import mvp.model.api.APIClient;
import mvp.views.interfaces.ICarView;

public class CarsPresenter {

    APIClient apiClient ;
    ICarView iCarView ;

    public CarsPresenter(){
        apiClient = new APIClient();
    }

    public void  setView(ICarView view){
        iCarView = view; // my reference equal to any view will be sent
    }

    public void getData(){
        iCarView.showProgress();
       String name =  apiClient.getMyName();
       iCarView.hideProgress();
       iCarView.showData(name);
    }
}
