package com.macguffinco.tormundconnect.Tasks;

import android.app.Activity;
import android.app.IntentService;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.v4.app.NotificationCompat;


import com.macguffinco.tormundconnect.Logic.AppointmentManager;
import com.macguffinco.tormundconnect.Logic.TormundManager;
import com.macguffinco.tormundconnect.R;
import com.macguffinco.tormundconnect.activities.MainActivity;
import com.macguffinco.model.Notifications.NotificationDC;
import com.macguffinco.model.appointments.AppointmentDC;
import com.macguffinco.tormundconnect.fragments.CameraControl.TaskDownloadCustomerImagesToday;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class NotificationService extends IntentService {

    private int result = Activity.RESULT_CANCELED;
    public static final String URL = "urlpath";
    public static final String FILENAME = "filename";
    public static final String FILEPATH = "filepath";
    public static final String QUANTITY_NOTIFICATION = "quantity_notification";
    public static final String RESULT = "result";
    public static final String NOTIFICATION = "com.macguffinco.tormundconnect.Tasks";


    public NotificationService() {
        super("DownloadService");
    }

    // will be called asynchronously by Android
    @Override
    protected void onHandleIntent(Intent intent) {



        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.MINUTE,2);





        CheckForNotifications();
        CheckForNextAppointment();
        new TaskDownloadCustomerImagesToday(getApplicationContext()).execute("", "", "");

    }

    public void CheckForNotifications(){

        try{

            if(TormundManager.GlobalEmployeeDC.user!=null){
                NotificationDC notification= new NotificationDC();
                notification.principal_user= TormundManager.GlobalEmployeeDC.user;

                ArrayList<NotificationDC> _notifications= com.macguffinco.tormundconnect.Logic.NotificationManager.notifications_search(notification);
                if(_notifications!=null && _notifications.size()>0){
                    publishResults(_notifications.size());
                    if(_notifications.size()==1){

                        for (NotificationDC noti:_notifications ) {
                            SendNotificationAlert(noti.id,noti.title,noti.message);
                        }
                    }else{
                        String title=""+_notifications.size()+" Mensajes";
                        String Message="";
                        for (NotificationDC noti:_notifications ) {
                            Message+=""+noti.title+"  "+noti.message+"\n";
                        }
                        SendNotificationAlert(99,title,Message);
                    }


                }





            }

        }catch (Exception ex){
            ex.printStackTrace();
        }

    }

    public void CheckForNextAppointment(){

        AppointmentDC app=new AppointmentDC();
        if(TormundManager.GlobalEmployeeDC==null){return;}

        app.Employee=TormundManager.GlobalEmployeeDC;
        try {
            Date now= new Date();
            Calendar cal = Calendar.getInstance();
            cal.setTime(now);
            cal.add(Calendar.MINUTE,10);
            Date StartDate= cal.getTime();

            Calendar calFinished = Calendar.getInstance();
            calFinished.setTime(now);
            calFinished.add(Calendar.HOUR,1);
            calFinished.add(Calendar.MINUTE,10);
            Date finishDate= calFinished.getTime();

            app.AppointmentDate = StartDate;
            app.DueDate = finishDate;
            app.status_list="1,4,5,7";


            ArrayList<AppointmentDC> appointments = AppointmentManager.appointments(app, "search");

            if(appointments.size()>0){

                AppointmentDC nextAppointment=appointments.get(0);

                NotificationDC notification= new NotificationDC();
                notification.principal_user= TormundManager.GlobalEmployeeDC.user;
                notification.creation_by= TormundManager.GlobalEmployeeDC.user;
                notification.title="Proxima Cita " + TormundManager.FormatHourAMPM(nextAppointment.AppointmentDate);
                //notification.title="Proxima Cita";
                notification.message="Preparate tu cliente "+nextAppointment.Customer.name+" esta por llegar.";
                notification.is_read=false;
                com.macguffinco.tormundconnect.Logic.NotificationManager.notifications_new(notification);
            }


        }catch (Exception ex){
            ex.printStackTrace();

        }
    }


    private void SendNotificationAlert(int id,String title, String message){
        Intent intent2 = new Intent(this, MainActivity.class);
        PendingIntent contentintent= PendingIntent.getActivity(   this,
                0,intent2,0);



        NotificationCompat.Builder b=new  NotificationCompat.Builder(getApplicationContext(),"default")
                .setContentTitle(title)
                .setContentText(message)
                .setSmallIcon(R.mipmap.icon_launcher_connect)
                .setAutoCancel(true)
                .setColor(Color.BLUE)
                .setOnlyAlertOnce(true)
                //.addAction(R.mipmap.ic_launcher,"toast",actionIntent)
                //.setVibrate(new long[] {100, 250, 100, 500})
                .setContentIntent(contentintent);

        NotificationManager manager=(NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);
        manager.notify(1,b.build());

    }

    private void publishResults(int quantity_notification) {
        Intent intent = new Intent(NOTIFICATION);


        intent.putExtra(QUANTITY_NOTIFICATION,quantity_notification);
        sendBroadcast(intent);
    }


}



