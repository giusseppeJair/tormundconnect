package com.macguffinco.tormundconnect.fragments.Notifications;

import android.os.AsyncTask;

import com.macguffinco.tormundconnect.Logic.NotificationManager;
import com.macguffinco.model.Notifications.NotificationDC;

import java.util.ArrayList;

public class TaskGetNotifications extends AsyncTask<String, Integer, Long> {
    NotificationFragment _notificationFragment;
    NotificationDC _notificationDC;


    NotificationRecyclerAdapter _mAdapter;

    ArrayList<NotificationDC> _notifications;

    public TaskGetNotifications(NotificationFragment notificationFragment,NotificationDC notificationDC, NotificationRecyclerAdapter mAdapter){

        _notificationFragment=notificationFragment;

        _notificationDC=notificationDC;

        _mAdapter=mAdapter;


    }
    protected Long doInBackground(String... data) {


        try {

            _notifications= NotificationManager.notifications_search(_notificationDC);


        }catch (Exception ex){
            ex.printStackTrace();

        }
        return 0L;

    }

    protected void onProgressUpdate(Integer... progress) {
        String hola="";
    }

    protected void onPostExecute(Long result) {


        try {
            if((_notifications.size()>0 ) ){

                _notificationFragment.notifications.clear();
                _notificationFragment.notifications.addAll(_notifications);
                _notificationFragment.notificationRecyclerAdapter.notifyDataSetChanged();
                _notificationFragment.recyclerView.scrollToPosition(0);


            }

        }catch(Exception ex){

        }




    }
}
