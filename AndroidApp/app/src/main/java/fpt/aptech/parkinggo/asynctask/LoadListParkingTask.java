package fpt.aptech.parkinggo.asynctask;

import android.app.Activity;
import android.os.AsyncTask;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;

import java.util.List;

import fpt.aptech.parkinggo.R;
import fpt.aptech.parkinggo.activity.MapsActivity;
import fpt.aptech.parkinggo.callback.CallBack;
import fpt.aptech.parkinggo.callback.CustomProgressDialog;
import fpt.aptech.parkinggo.configuration.RestTemplateConfiguration;
import fpt.aptech.parkinggo.domain.response.LoginRes;
import fpt.aptech.parkinggo.domain.response.ParkingRes;
import fpt.aptech.parkinggo.statics.Session;

public class LoadListParkingTask extends AsyncTask<Void, Void, ResponseEntity<?>> {

    //private CallBack callBack;
    private Activity activity;
    public LoadListParkingTask(Activity activity) {
        this.activity = activity;
        //this.callBack = callBack;
    }

    @Override
    protected void onPreExecute() {
    }

    @Override
    protected ResponseEntity<?> doInBackground(Void... voids) {
        HttpEntity request = RestTemplateConfiguration.setRequest();
        String uri = activity.getString(R.string.URL_BASE);
        ResponseEntity<?> response = RestTemplateConfiguration
                .excuteRequest(uri+"list-parking", HttpMethod.GET, request, ParkingRes[].class);
        return response;
    }
    @Override
    protected void onPostExecute(ResponseEntity<?> responseEntity) {
    }
}
