package com.macguffinco.tormundconnect.fragments.CameraControl;

import android.content.Context;
import android.os.AsyncTask;

import com.macguffinco.tormundconnect.Logic.AppointmentManager;
import com.macguffinco.tormundconnect.Logic.PictureManager;
import com.macguffinco.tormundconnect.Logic.TormundManager;
import com.macguffinco.tormundconnect.fragments.AppointmentFragment;

import com.macguffinco.model.appointments.AppointmentDC;
import com.macguffinco.model.comercial.CustomerDC;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class TaskDownloadCustomerImagesToday extends AsyncTask<String, Integer, Long> {

    private Context context;

    private AppointmentFragment fragment;
    private AppointmentFragment.OnListFragmentInteractionListener mListener;

    private ArrayList<AppointmentDC> list;


    public TaskDownloadCustomerImagesToday(Context context){
        this.context = context;



    }
    protected Long doInBackground(String... data) {

        TormundManager.CreateDirectoryForPictures();

        AppointmentDC app=new AppointmentDC();
        app.Employee=TormundManager.GlobalEmployeeDC;
        try {
            Date now= new Date();
            Calendar cal = Calendar.getInstance();
            cal.setTime(now);
            cal.add(Calendar.HOUR, -1);
            Date StartDate= cal.getTime();

            cal.add(Calendar.DATE,1);
            Date finishDate= TormundManager.DateWithOutHours(cal.getTime());

            app.AppointmentDate = StartDate;
            app.DueDate = finishDate;
            app.status_list="1,4,5,7";

            ArrayList<CustomerDC> listCustomerDownloaded= new ArrayList<>();
            for (AppointmentDC appointment:AppointmentManager.appointments(app, "search") ) {
                if (listCustomerDownloaded.contains(appointment.Customer)) {
                  continue;
                }
                listCustomerDownloaded.add(appointment.Customer);
                PictureManager.DownloadCustomerPicturesToPhone(appointment);
            }

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
