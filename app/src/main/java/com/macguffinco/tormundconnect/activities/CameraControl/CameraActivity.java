package com.macguffinco.tormundconnect.activities.CameraControl;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.macguffinco.tormundconnect.Logic.TormundManager;
import com.macguffinco.tormundconnect.R;
import com.macguffinco.tormundconnect.fragments.CameraControl.CameraFragment;
import com.macguffinco.model.VaultFiles.VaultFileDC;
import com.macguffinco.model.appointments.AppointmentDC;
import com.macguffinco.model.comercial.BranchDC;
import com.macguffinco.model.comercial.CustomerDC;
import com.macguffinco.model.comercial.EmployeeDC;
import com.macguffinco.model.comercial.ServiceDC;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Locale;

public class CameraActivity extends AppCompatActivity {

    public final AppointmentDC AppointmentItem= new AppointmentDC();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera);

        if (null == savedInstanceState) {


          String dateDC=  getIntent().getStringExtra("Appointment");
          if(dateDC.length()>0){
              try{
                  TormundManager.ConvertStringToAppointmentDC(dateDC,AppointmentItem);


              }catch (Exception ex){

                  ex.printStackTrace();
              }



          }

            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.container, CameraFragment.newInstance(AppointmentItem))
                    .commit();
        }

    }

    @Override
    public void onBackPressed() {

        this.finish();
        TormundManager.goMainActivity(getApplicationContext());
    }
}
