package com.macguffinco.tormundconnect.fragments.Notifications;

import android.graphics.Color;
import android.os.AsyncTask;

import com.macguffinco.tormundconnect.Logic.NotificationManager;
import com.macguffinco.tormundconnect.Logic.TormundManager;
import com.macguffinco.model.Notifications.NotificationDC;
import com.macguffinco.model.comercial.EmployeeDC;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class TaskMarkAsReadNotifications extends AsyncTask<String, Integer, Long> {

    NotificationFragment _notificationFragment;
    NotificationDC _notificationDC;


    NotificationRecyclerAdapter _mAdapter;

    ArrayList<NotificationDC> _notifications;

    public TaskMarkAsReadNotifications(NotificationFragment notificationFragment, NotificationDC notificationDC, NotificationRecyclerAdapter mAdapter){

        _notificationFragment=notificationFragment;
        _notifications=_notificationFragment.notifications;
        _notificationDC=notificationDC;

        _mAdapter=mAdapter;


    }

    public ArrayList<NotificationDC> removeReadNotifications(ArrayList<NotificationDC> allNotifications) {

        ArrayList<NotificationDC> _notifications=allNotifications;

        Iterator<NotificationDC> NotificationIterator = _notifications.iterator();
        while (NotificationIterator.hasNext()) {
            NotificationDC c = NotificationIterator.next();
            if (c.is_read) {
                NotificationIterator.remove();
            }
        }

        return _notifications;

    }

    protected Long doInBackground(String... data) {


        try {

            ArrayList<NotificationDC> notificationsFiltered=removeReadNotifications(_notifications);



            for (NotificationDC notificationToUpdate:notificationsFiltered) {

                if(!notificationToUpdate.is_read){
                   NotificationManager.notifications_markAsRead(notificationToUpdate);
                }
            }

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
