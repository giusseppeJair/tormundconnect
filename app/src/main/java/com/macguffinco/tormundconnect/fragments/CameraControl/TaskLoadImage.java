package com.macguffinco.tormundconnect.fragments.CameraControl;

import android.os.AsyncTask;


import com.macguffinco.tormundconnect.Logic.TormundManager;

import com.macguffinco.model.appointments.AppointmentDC;



public class TaskLoadImage extends AsyncTask<String, Integer, Long> {

    final AppointmentDC _appointmentDC;
    public TaskLoadImage(AppointmentDC appointmentDC){
        _appointmentDC=appointmentDC;

    }
    protected Long doInBackground(String... data) {


        try {

            TormundManager.SaveCustomerImage(_appointmentDC);

        }catch (Exception ex){
            ex.printStackTrace();

        }
        return 0L;

    }

    protected void onProgressUpdate(Integer... progress) {
        String hola="";
    }

    protected void onPostExecute(Long result) {


    }
}
