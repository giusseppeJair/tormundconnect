package com.macguffinco.tormundconnect.Tasks;

import android.content.Context;
import android.os.AsyncTask;
import android.view.WindowManager;
import android.widget.ArrayAdapter;

import com.google.android.gms.tasks.Task;
import com.macguffinco.tormundconnect.Logic.AppointmentManager;
import com.macguffinco.tormundconnect.Logic.TormundManager;
import com.macguffinco.tormundconnect.fragments.AppointmentFragment;
import com.macguffinco.tormundconnect.fragments.AppointmentRecyclerViewAdapter;
import com.macguffinco.tormundconnect.fragments.NewAppointmentFragment;
import com.macguffinco.model.appointments.AppointmentDC;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class LoadHoraryTask extends AsyncTask<String, Integer, Long> {

    private Context context;
    ArrayAdapter<String> arrayAdapter;


    private NewAppointmentFragment fragment;
    private AppointmentFragment.OnListFragmentInteractionListener mListener;

    private ArrayList<AppointmentDC> list;

    public LoadHoraryTask(Context context, NewAppointmentFragment fragment){
        this.context = context;

        this.fragment=fragment;

    }
    @Override
    protected Long doInBackground(String... strings) {

try {
    Calendar now= Calendar.getInstance();
    now.add(Calendar.MINUTE,5);
    Date startDate=Timestamp.valueOf(fragment.dateSelected+" 00:00:00");
    Date today=now.getTime();
    if(TormundManager.DateWithOutHours(today).compareTo(TormundManager.DateWithOutHours(startDate))==0)
    {
        String hours=TormundManager.FormatHourMinutesSeconds(now.getTime())+".000";
        startDate=Timestamp.valueOf(fragment.dateSelected+" "+hours);
    }
    AppointmentDC appointmentDC= new AppointmentDC();
    appointmentDC.AppointmentDate= startDate;
    appointmentDC.Customer= fragment.CustomerSelected;
    appointmentDC.Branch=TormundManager.GlobalBranchDC;
    appointmentDC.Employee=fragment.EmployeeSelected;
    appointmentDC.Service=fragment.ServiceSelected;
    fragment.enabledHoursList=AppointmentManager.getEnabledDates(appointmentDC);

    String[] hoursList=new String[fragment.enabledHoursList.size()];
    int i=0;
    for (AppointmentDC appointment:fragment.enabledHoursList ) {
        hoursList[i]=TormundManager.JsonDateToHours(appointment.AppointmentDate)+"-"+TormundManager.JsonDateToHours(appointment.DueDate);
        i++;
    }

    arrayAdapter = new ArrayAdapter<String>(context,
            android.R.layout.simple_dropdown_item_1line, hoursList);



}catch (Exception e){

    e.printStackTrace();
}




        return null;
    }

    protected void onPostExecute(Long result) {


        fragment.horarySpinner.setAdapter(arrayAdapter);
        fragment.horarySpinner.setHint("Hora");
        fragment.showProgress(false);
        fragment.horarySpinner.setDropDownHeight(WindowManager.LayoutParams.WRAP_CONTENT);

//        fragment.lista=list;
//        fragment.adapter = new AppointmentRecyclerViewAdapter(list, mListener);
//        fragment.recyclerView.setAdapter(fragment.adapter);
    }
}
