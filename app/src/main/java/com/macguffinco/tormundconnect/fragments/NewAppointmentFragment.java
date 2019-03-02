package com.macguffinco.tormundconnect.fragments;


import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.Toast;

import com.macguffinco.tormundconnect.Logic.AppointmentManager;
import com.macguffinco.tormundconnect.Logic.TormundManager;
import com.macguffinco.tormundconnect.R;
import com.macguffinco.tormundconnect.Tasks.LoadHoraryTask;
import com.macguffinco.tormundconnect.Tasks.SaveNewAppointmentTask;
import com.macguffinco.model.appointments.AppointmentDC;
import com.macguffinco.model.comercial.CustomerDC;
import com.macguffinco.model.comercial.EmployeeDC;
import com.macguffinco.model.comercial.ServiceDC;
import com.weiwangcn.betterspinner.library.material.MaterialBetterSpinner;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;

import devs.mulham.horizontalcalendar.HorizontalCalendar;
import devs.mulham.horizontalcalendar.HorizontalCalendarView;
import devs.mulham.horizontalcalendar.utils.HorizontalCalendarListener;

/**
 * A simple {@link Fragment} subclass.
 */
public class NewAppointmentFragment extends Fragment {

    private View mProgressView;
    private View mViewToRemplaced;
    NewAppointmentFragment fragment;
    public MaterialBetterSpinner spinnerEmployee;
    public MaterialBetterSpinner spinnerService;
    public MaterialBetterSpinner spinnerCustomers;
    public MaterialBetterSpinner horarySpinner;
    public Button buttonSaveAppointment;

    public CustomerDC CustomerSelected;
    public EmployeeDC EmployeeSelected;
    public ServiceDC ServiceSelected;
    public String  dateSelected="";
    public AppointmentDC horarySelected;
    public ArrayList<AppointmentDC> enabledHoursList;
    public HorizontalCalendar horizontalCalendar;
    public boolean firstTime=false;

    public NewAppointmentFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {



        firstTime=false;
        View view = inflater.inflate(R.layout.fragment_new_appointment, container, false);
        fragment=this;
        mViewToRemplaced = view.findViewById(R.id.watchIcon);
        mProgressView = view.findViewById(R.id.horary_progress);
        buttonSaveAppointment=view.findViewById(R.id.btnSaveNewAppointment);
        buttonSaveAppointment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SavedAppointment();
            }
        });
        HorarySpinner(view);
        showProgress(false);
        CalendarHorizontal(view);
        EmployeeSpinner(view);
        ServiceSpinner(view);

        CustomerSpinner(view);

        //CleanForm();

        Calendar calendar = Calendar.getInstance();
        //calendar.add(Calendar.DAY_OF_MONTH,-1);
        //horizontalCalendar.goToday(true);



        return view;


    }

    private void HorarySpinner(View view) {
        horarySpinner= view.findViewById(R.id.spinnertime);

        horarySpinner.setDropDownHeight(0);
        String[] mApps = {"" };
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getContext(),android.R.layout.simple_list_item_1,mApps);
        horarySpinner.setAdapter(adapter);
        horarySpinner.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String value=horarySpinner.getText().toString();
                if(value.equals("")) return;

                horarySelected=null;
                if(enabledHoursList!=null && !enabledHoursList.isEmpty()){
                    for (Iterator<AppointmentDC> it = enabledHoursList.iterator(); it.hasNext();) {
                        AppointmentDC item=it.next();
                        String comparar=TormundManager.JsonDateToHours(item.AppointmentDate)+"-"+TormundManager.JsonDateToHours(item.DueDate);;
                        if (comparar.trim().equals(value.trim()))
                        {   horarySelected=item;
                            break;
                        }
                    }
                }






            }
        });


    }

    public void CalendarHorizontal(View view){



        Calendar startDate=Calendar.getInstance();
        //startDate.add(Calendar.DAY_OF_MONTH,-1);
        //startDate.set(Calendar.HOUR_OF_DAY, 0);
        Calendar endDate=Calendar.getInstance();
        endDate.add(Calendar.MONTH, 1);
        Calendar date=Calendar.getInstance();
        //date.add(Calendar.DAY_OF_MONTH,1);

         horizontalCalendar = new HorizontalCalendar.Builder(view, R.id.calendarHorizontal)
                .range( startDate,  endDate)
                .datesNumberOnScreen(5)   // Number of Dates cells shown on screen (default to 5).
                .configure()    // starts configuration.
               // .formatTopText(String dateFormat)       // default to "MMM".
               // .formatMiddleText(String dateFormat)    // default to "dd".
                //.formatBottomText(String dateFormat)    // default to "EEE".
                .showTopText(true)              // show or hide TopText (default to true).
                    .showBottomText(true)           // show or hide BottomText (default to true).
              //      .textColor(Color.BLUE, Color.WHITE)    // default to (Color.LTGRAY, Color.WHITE).
                  //  .selectedDateBackground(Drawable background)      // set selected date cell background.
              //  .selectorColor(Color.YELLOW)               // set selection indicator bar's color (default to colorAccent).
                .end()          // ends configuration.
                .defaultSelectedDate( date)    // Date to be selected at start (default to current day `Calendar.getInstance()`).
                .build();

        horizontalCalendar.setCalendarListener(new HorizontalCalendarListener() {
            @Override
            public void onCalendarScroll(HorizontalCalendarView calendarView, int dx, int dy) {
                super.onCalendarScroll(calendarView, dx, dy);
            }

            @Override
            public void onDateSelected(Calendar date, int position) {

                dateSelected="";
                try {


                    dateSelected= DateFormat.format("yyyy-MM-dd",horizontalCalendar.getDateAt(position+1).getTime()).toString();

                }catch (Exception e){
                    e.printStackTrace();
                    dateSelected="";
                }



                Toast.makeText(getContext(), dateSelected + " seleccionado.", Toast.LENGTH_SHORT).show();
               // Log.i("onDateSelected", selectedDateStr + " - Position = " + position);



                UpdateHorary();


            }

        });


    }

    /**
     * Shows the progress UI and hides the login form.
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    public void showProgress(final boolean show) {
        // On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
        // for very easy animations. If available, use these APIs to fade-in
        // the progress spinner.
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
                int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

                mViewToRemplaced.setVisibility(show ? View.GONE : View.VISIBLE);
                mViewToRemplaced.animate().setDuration(shortAnimTime).alpha(
                        show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        mViewToRemplaced.setVisibility(show ? View.GONE : View.VISIBLE);
                    }
                });

                mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
                mProgressView.animate().setDuration(shortAnimTime).alpha(
                        show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
                    }
                });
            } else {
                // The ViewPropertyAnimator APIs are not available, so simply show
                // and hide the relevant UI components.
                mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
                mViewToRemplaced.setVisibility(show ? View.GONE : View.VISIBLE);
            }

        }catch (Exception e){
            e.printStackTrace();
        }


       // horarySpinner.setClickable(!show);
    }


    public void EmployeeSpinner(View view){

        String[] empleados= new String[TormundManager.GlobalEmployeesList.size()];
        int i=0;
        for (EmployeeDC employee:TormundManager.GlobalEmployeesList   ) {
            empleados[i]=employee.name;
            i++;
        }


        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(getContext(),
                android.R.layout.simple_dropdown_item_1line, empleados);

         spinnerEmployee= view.findViewById(R.id.spinnerEmployees);
        spinnerEmployee.setAdapter(arrayAdapter);
        spinnerEmployee.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String value=spinnerEmployee.getText().toString();
                EmployeeSelected=null;
                if(value.equals("")) return;

                for (Iterator<EmployeeDC> it = TormundManager.GlobalEmployeesList.iterator(); it.hasNext();) {
                    EmployeeDC item=it.next();
                    if (item.name.contains(value))
                    {   EmployeeSelected=item;
                        break;
                    }
                }



                UpdateHorary();

            }
        });

        //Set Employee
        if(TormundManager.GlobalEmployeeDC!=null && TormundManager.GlobalEmployeeDC.name!=null
                && TormundManager.GlobalEmployeeDC.name.length()>0){

            String str = TormundManager.GlobalEmployeeDC.name;
            for(int k=0; k < arrayAdapter.getCount(); k++) {
                if(str.trim().equals(arrayAdapter.getItem(k).toString())){
                    spinnerEmployee.setText(arrayAdapter.getItem(k).toString());
                    break;
                }
            }
        }





    }

    public void ServiceSpinner(View view){

        String[] servicios= new String[TormundManager.GlobalServicesList.size()];
        int k=0;
        for (ServiceDC serviceDC:TormundManager.GlobalServicesList   ) {
            servicios[k]=serviceDC.Name;
            k++;
        }


        ArrayAdapter<String> arrayAdapterServices = new ArrayAdapter<String>(getContext(),
                android.R.layout.simple_dropdown_item_1line, servicios);

         spinnerService= view.findViewById(R.id.spinnerservice);
        spinnerService.setAdapter(arrayAdapterServices);
        spinnerService.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String value=spinnerService.getText().toString();
                ServiceSelected=null;
                if(value.equals("")) return;
                for (Iterator<ServiceDC> it = TormundManager.GlobalServicesList.iterator(); it.hasNext();) {
                    ServiceDC item=it.next();
                    if (item.Name.contains(value))
                    {   ServiceSelected=item;

                        break;
                    }
                }



                UpdateHorary();

            }
        });
    }

    public void CustomerSpinner(View view){

        String[] clientes= new String[TormundManager.GlobalCustomersList.size()];
        int j=0;
        for (CustomerDC customer:TormundManager.GlobalCustomersList   ) {
            clientes[j]=customer.name;
            j++;
        }


        ArrayAdapter<String> arrayAdapterCustomers = new ArrayAdapter<String>(getContext(),
                android.R.layout.simple_dropdown_item_1line, clientes);

         spinnerCustomers= view.findViewById(R.id.spinnercustomer);
        spinnerCustomers.setAdapter(arrayAdapterCustomers);

        spinnerCustomers.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String value=spinnerCustomers.getText().toString();
                CustomerSelected=null;
                if(value.equals("")) return;
                for (Iterator<CustomerDC> it = TormundManager.GlobalCustomersList.iterator(); it.hasNext();) {
                    CustomerDC item=it.next();
                    if (item.name.contains(value))
                    {   CustomerSelected=item;
                        break;
                    }
                }





            }
        });


    }

    public void CleanForm(){

        dateSelected="";
        ServiceSelected=null;
        EmployeeSelected=null;
        CustomerSelected=null;

        horarySelected=null;





    }
    public void UpdateHorary(){


        horarySpinner.setText("");

        enabledHoursList=null;

        if(EmployeeSelected==null|| ServiceSelected==null || TormundManager.GlobalBranchDC==null
                ) return;

        if( dateSelected.equals("")){
            Calendar today= Calendar.getInstance();
            dateSelected= DateFormat.format("yyyy-MM-dd", today.getTime()).toString();
        }

        showProgress(true);

        fragment.horarySpinner.setHint("Cargando...");
        new LoadHoraryTask(getContext(),fragment).execute("", "", "");

    }

    public boolean SavedAppointment(){
        if(CustomerSelected==null || EmployeeSelected==null|| ServiceSelected==null || TormundManager.GlobalBranchDC==null
              ||horarySelected==null  ) return false;

        if( dateSelected.equals("")){
            Calendar today= Calendar.getInstance();
            dateSelected= DateFormat.format("yyyy-MM-dd", today.getTime()).toString();
        }

        AppointmentDC appointmentDC= new AppointmentDC();
        appointmentDC.status_list="1";
        appointmentDC.Employee=EmployeeSelected;
        appointmentDC.Branch=TormundManager.GlobalBranchDC;
        appointmentDC.Service=ServiceSelected;
        appointmentDC.Customer=CustomerSelected;
        appointmentDC.AppointmentDate=horarySelected.AppointmentDate;
        appointmentDC.DueDate=horarySelected.DueDate;

        new SaveNewAppointmentTask(getContext(),fragment,appointmentDC).execute("", "", "");



        return true;

    }


}
