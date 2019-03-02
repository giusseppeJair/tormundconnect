package com.macguffinco.tormundconnect.fragments;

import android.content.Context;
import android.os.AsyncTask;

import com.macguffinco.tormundconnect.Logic.AppointmentManager;
import com.macguffinco.tormundconnect.Logic.TormundManager;
import com.macguffinco.model.appointments.AppointmentDC;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;

public class TaskAppointmentFragment extends AsyncTask<String, Integer, Long> {

    private Context context;

    private AppointmentFragment fragment;
    private AppointmentFragment.OnListFragmentInteractionListener mListener;

    private ArrayList<AppointmentDC> list;


    public TaskAppointmentFragment(Context context, AppointmentFragment fragment,  AppointmentFragment.OnListFragmentInteractionListener Listener){
        this.context = context;

        this.fragment=fragment;
        this.mListener=Listener;
    }
    protected Long doInBackground(String... data) {

        TormundManager.CreateDirectoryForPictures();

        AppointmentDC app=new AppointmentDC();
        app.Employee=TormundManager.GlobalEmployeeDC;
        try {
            Date now= new Date();
            Calendar cal = Calendar.getInstance();
            cal.setTime(now);
            cal.add(Calendar.MINUTE, -20);
            Date StartDate= cal.getTime();

            cal.add(Calendar.DATE,1);
            Date finishDate= TormundManager.DateWithOutHours(cal.getTime());

            app.AppointmentDate = StartDate;
            app.DueDate = finishDate;
            app.status_list="1,4,5,7";


            list = AppointmentManager.appointments(app, "search");


        }catch (Exception ex){
            ex.printStackTrace();

        }
        return 0L;

    }

    protected void onProgressUpdate(Integer... progress) {
        String hola="";
    }

    protected void onPostExecute(Long result) {

        Collections.sort(list, new Comparator<AppointmentDC>() {
            public int compare(AppointmentDC obj1, AppointmentDC obj2) {
                return obj1.AppointmentDate.compareTo(obj2.AppointmentDate);
            }
        });
        fragment.lista=list;
        fragment.adapter = new AppointmentRecyclerViewAdapter(list, mListener);
        fragment.recyclerView.setAdapter(fragment.adapter);
    }









}
