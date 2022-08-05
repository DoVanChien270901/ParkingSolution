package fpt.aptech.parkinggo.asynctask;

import android.app.Activity;
import android.os.AsyncTask;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;

import fpt.aptech.parkinggo.R;
import fpt.aptech.parkinggo.configuration.RestTemplateConfiguration;
import fpt.aptech.parkinggo.domain.response.LoadStatusParking;

public class LoadStatusParkingTask extends AsyncTask<Void, Void, ResponseEntity<?>> {
    Activity activity;
    String name;

    public LoadStatusParkingTask() {
    }

    public LoadStatusParkingTask(Activity activity, String name){
        this.activity = activity;
        this.name = name;
    }

    @Override
    protected ResponseEntity<?> doInBackground(Void... voids) {
        HttpEntity request = RestTemplateConfiguration.setRequest();
        String uri = activity.getString(R.string.URL_BASE)+"load-status-parking/"+name;
        ResponseEntity<?> response = RestTemplateConfiguration
                .excuteRequest(uri, HttpMethod.GET, request, LoadStatusParking.class);
        return response;
    }
}
