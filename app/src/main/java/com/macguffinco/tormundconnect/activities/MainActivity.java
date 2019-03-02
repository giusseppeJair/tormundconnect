package com.macguffinco.tormundconnect.activities;

import android.Manifest;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.internal.BottomNavigationItemView;
import android.support.design.internal.BottomNavigationMenuView;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.macguffinco.tormundconnect.Logic.TormundManager;
import com.macguffinco.tormundconnect.R;
import com.macguffinco.tormundconnect.Tasks.NotificationService;
import com.macguffinco.tormundconnect.activities.DetailBarber.DetailBarberActivity;
import com.macguffinco.tormundconnect.fragments.AppointmentFragment;
import com.macguffinco.tormundconnect.fragments.Notifications.NotificationFragment;
import com.macguffinco.tormundconnect.fragments.NewAppointmentFragment;
import com.macguffinco.model.appointments.AppointmentDC;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity implements AppointmentFragment.OnListFragmentInteractionListener {

    private NewAppointmentFragment mNewAppointmentFragment;
    private AppointmentFragment mAppointmentFragment;
    private NotificationFragment mNotificationFragment;

    int cart_count=1;

    private FrameLayout mMainFrame;

    private Timer autoUpdate;


    private void setFragment(Fragment fragment) {
        try{
            if(fragment instanceof   NewAppointmentFragment){
                fragment =new NewAppointmentFragment();
            }
            FragmentTransaction fragmentTransaction=getSupportFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.main_frame,fragment,"AppointmentFragment");
            fragmentTransaction.commit();

        }catch(Exception ex){
            ex.printStackTrace();
        }

    }



    public BroadcastReceiver receiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            Bundle bundle = intent.getExtras();
            if (bundle != null) {


                int quantity_notification=bundle.getInt(NotificationService.QUANTITY_NOTIFICATION);

                if(quantity_notification>0){SetNotificationBadge(2,quantity_notification);
                }

                Toast.makeText(MainActivity.this,
                        "Tormund Connect" ,
                        Toast.LENGTH_LONG).show();


            }
        }
    };




    private  boolean checkAndRequestPermissions() {
        int cameraMessage = ContextCompat.checkSelfPermission(this,Manifest.permission.CAMERA);
        int readPermission = ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE);
        int writePermission = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE);

        ArrayList<String> listPermissionsNeeded = new ArrayList<>();
        if (cameraMessage != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.CAMERA);
        }
        if (readPermission != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.READ_EXTERNAL_STORAGE);
        }
        if (writePermission != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
        }
        if (!listPermissionsNeeded.isEmpty()) {
            ActivityCompat.requestPermissions(this, listPermissionsNeeded.toArray(new String[listPermissionsNeeded.size()]),1);
            return false;
        }
        return true;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setIcon(R.drawable.logo_connect_blanco);
        Initial();

        checkAndRequestPermissions();

        Intent intent = new Intent(this, NotificationService.class);
        // add infos for the service which file to download and where to store

        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        PendingIntent pending = PendingIntent.getService(this, 0, intent, 0);

        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP,
                Calendar.getInstance().getTimeInMillis(),60000, pending);




//        startService(intent);


        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        mMainFrame= findViewById(R.id.main_frame);


        mNewAppointmentFragment =new NewAppointmentFragment();
        mNotificationFragment=new NotificationFragment();
        mAppointmentFragment=new AppointmentFragment();
        setFragment(mAppointmentFragment );

//        NotificationRecyclerAdapter notificationRecyclerAdapter= new NotificationRecyclerAdapter(notifications);
//        mNotificationFragment.recyclerView.setAdapter(notificationRecyclerAdapter);
//        NotificationDC notification= new NotificationDC();
//        notification.principal_user= TormundManager.GlobalEmployeeDC.user;
//        new TaskGetNotifications(mNotificationFragment,notification,notificationRecyclerAdapter).execute("","","");



        navigation.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {


                switch (item.getItemId()) {
                    case R.id.navigation_createdate:
                        setFragment(mNewAppointmentFragment);

                        return true;
                    case R.id.navigation_date:
                        setFragment(mAppointmentFragment );

                        return true;
                    case R.id.navigation_notification:
                        setFragment(mNotificationFragment );
                        return true;
                }
                return false;
            }
        });
    }

    @Override
    public void onListFragmentInteraction(AppointmentDC appointmentDC) {

        Intent intent= new Intent(this, DetailBarberActivity.class);
        TormundManager.GlobalAppointmentSelected=appointmentDC;
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }

    public void Initial(){


    }


public void SetNotificationBadge(int itemPosition,int counter){


    try{
        BottomNavigationView navigationView=findViewById(R.id.navigation);
        BottomNavigationMenuView bottomNavigationMenuView =
                (BottomNavigationMenuView) navigationView.getChildAt(0);
        View v = bottomNavigationMenuView.getChildAt(itemPosition);
        BottomNavigationItemView itemView = (BottomNavigationItemView) v;



        View badge = LayoutInflater.from(this)
                .inflate(R.layout.notification_badge, bottomNavigationMenuView, false);

        if(counter>0){

            TextView textView= badge.findViewById(R.id.notifications_counter);
            textView.setVisibility(View.VISIBLE);
            textView.setText(counter+"+");
            itemView.addView(badge);

        }else{


            itemView.removeViewAt(2);
            //itemView.removeAllViews();
        }

    }catch(Exception ex){
        ex.printStackTrace();
    }



}


    @Override
    public void onResume() {
        super.onResume();
        registerReceiver(receiver, new IntentFilter(
                NotificationService.NOTIFICATION));

        autoUpdate = new Timer();
        autoUpdate.schedule(new TimerTask() {
            @Override
            public void run() {
                runOnUiThread(new Runnable() {
                    public void run() {
                        updateHTML();
                    }
                });
            }
        }, 0, 10000); // updates each 40 secs
    }

    private void updateHTML(){


//        AppointmentFragment myFragment = (AppointmentFragment)getSupportFragmentManager().findFragmentByTag("AppointmentFragment");
//        if (myFragment != null && myFragment.isVisible()) {
//            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
//            ft.detach(myFragment).attach(myFragment).commit();
//        }




    }

    @Override
    public void onPause() {
        autoUpdate.cancel();
        super.onPause();
        unregisterReceiver(receiver);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);

        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.log_out:
                LogOutApplication();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

public void LogOutApplication(){

    SharedPreferences preferences= getSharedPreferences("TormundStylist", Context.MODE_PRIVATE);
    SharedPreferences.Editor editor = preferences.edit();
    editor.clear();
    editor.commit();

    finish();
    TormundManager.goWelcomeActivity(this);

}

}
