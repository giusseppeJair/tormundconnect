package com.macguffinco.tormundconnect.fragments.Notifications;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.macguffinco.tormundconnect.Logic.TormundManager;
import com.macguffinco.tormundconnect.activities.MainActivity;
import com.macguffinco.tormundconnect.R;
import com.macguffinco.tormundconnect.fragments.AppointmentFragment;
import com.macguffinco.model.Notifications.NotificationDC;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

/**
 * A simple {@link Fragment} subclass.
 */
public class NotificationFragment extends Fragment {

 public RecyclerView recyclerView;
 NotificationRecyclerAdapter notificationRecyclerAdapter;
 ArrayList<NotificationDC> notifications= new ArrayList<NotificationDC>();
 NotificationFragment mThisNotificationFragment;

    // TODO: Customize parameters
    private int mColumnCount = 1;
    private AppointmentFragment.OnListFragmentInteractionListener mListener;

    public NotificationFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {



        View view = inflater.inflate(R.layout.fragment_notifications, container, false);
        recyclerView= view.findViewById(R.id.listsearch);


        mThisNotificationFragment=this;
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        notificationRecyclerAdapter= new NotificationRecyclerAdapter(notifications);
        recyclerView.setAdapter(notificationRecyclerAdapter);
        recyclerView.addItemDecoration(new DividerItemDecoration(recyclerView.getContext(), DividerItemDecoration.VERTICAL));

        NotificationDC notification= new NotificationDC();
        notification.principal_user= TormundManager.GlobalEmployeeDC.user;
        notification.is_read=true;
        new TaskGetNotifications(this,notification,notificationRecyclerAdapter).execute("","","");

        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                NotificationDC notification2= new NotificationDC();
                notification2.principal_user= TormundManager.GlobalEmployeeDC.user;
                notification2.is_read=true;
                new TaskMarkAsReadNotifications(mThisNotificationFragment,notification2,notificationRecyclerAdapter).execute("","","");
            }
        }, 4000);



        MainActivity activity=(MainActivity) getActivity();
        activity.SetNotificationBadge(2,0);





        return view;

    }

}
