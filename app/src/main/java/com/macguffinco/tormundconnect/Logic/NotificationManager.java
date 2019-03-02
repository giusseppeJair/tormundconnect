package com.macguffinco.tormundconnect.Logic;

import android.util.Log;

import com.macguffinco.tormundconnect.Solution.ApiConnection;
import com.macguffinco.model.Notifications.NotificationDC;
import com.macguffinco.model.Solution.UserDC;
import com.macguffinco.model.comercial.CommentDC;
import com.macguffinco.model.comercial.EmployeeDC;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.DataOutputStream;
import java.net.HttpURLConnection;
import java.util.ArrayList;

public final class NotificationManager {


    public static ArrayList<NotificationDC> notifications_search(NotificationDC notificationDC) {

        if(notificationDC==null) return null;
        if(notificationDC.principal_user==null) return null;
        ArrayList<NotificationDC> notificationsReturn=new ArrayList<NotificationDC>();


        try {
            JSONObject jsonNotification=new JSONObject();
            JSONObject jsonPrincipalUser=new JSONObject();
            jsonPrincipalUser.put("id",notificationDC.principal_user.Id);
            jsonNotification.put("principal_user",jsonPrincipalUser);
            jsonNotification.put("is_read",notificationDC.is_read);
            if(notificationDC.schedule_date!=null){
                jsonNotification.put("schedule_date",TormundManager.FormatDateTime(notificationDC.schedule_date));
            }


            jsonNotification.put("event_key","search");




            HttpURLConnection conn= ApiConnection.getConnection("notifications");
            DataOutputStream os = new DataOutputStream(conn.getOutputStream());
            os.writeBytes(jsonNotification.toString());

            os.flush();
            os.close();

            int responseCode = conn.getResponseCode();
            String responseMessage = conn.getResponseMessage();

            if(responseCode == HttpURLConnection.HTTP_OK){
                String responseString = ApiConnection.readStream(conn.getInputStream());

                JSONArray array= new JSONArray(responseString);
                int top =15;
                if(array.length()>=top){
                    top=15;
                }else{
                    top=array.length();
                }
                if(array.length()>0){
                    for (int i=0 ;i<top;i++) {
                        JSONObject json =array.getJSONObject(i);

                        if(!json.isNull("id")){

                            NotificationDC notificationReturn= new NotificationDC();
                            notificationReturn.id=json.getInt("id");
                            notificationReturn.title=json.getString("title");
                            notificationReturn.message=json.getString("message");
                            notificationReturn.is_read=json.getBoolean("is_read");
                            notificationReturn.notification_type=json.getInt("notification_type");
                            if(!json.isNull("principal_user")){
                                JSONObject jsonUser=new JSONObject();
                                UserDC user= new UserDC();
                                jsonUser=json.getJSONObject("principal_user");
                                user.Id=jsonUser.getInt("id");
                                user.Name=jsonUser.getString("name");
                                notificationReturn.principal_user=user;
                            }
                            notificationReturn.schedule_date=TormundManager.JsonStringToDate2(json.getString("schedule_date"));
                            notificationReturn.creation_date=TormundManager.JsonStringToDate2(json.getString("creation_date"));

                            notificationsReturn.add(notificationReturn);
                        }

                    }

                    return notificationsReturn;
                }else {
                    return new ArrayList<NotificationDC>();
                }



            }else{
                Log.v("CatalogClient", "Response code:"+ responseCode);
                Log.v("CatalogClient", "Response message:"+ responseMessage);
            }
            Log.i("STATUS", String.valueOf(conn.getResponseCode()));
            Log.i("MSG" , conn.getResponseMessage());

            conn.disconnect();
        } catch (Exception e) {
            e.printStackTrace();
            return new ArrayList<NotificationDC>();
        }


        return new ArrayList<NotificationDC>();
    }

    public static ArrayList<NotificationDC> notifications_new(NotificationDC notificationDC) {

        if(notificationDC==null) return null;
        if(notificationDC.principal_user==null) return null;
        ArrayList<NotificationDC> notificationsReturn=new ArrayList<NotificationDC>();


        try {
            JSONObject jsonNotification=new JSONObject();
            JSONObject jsonPrincipalUser=new JSONObject();
            jsonPrincipalUser.put("id",notificationDC.principal_user.Id);
            JSONObject jsonCreationByUser=new JSONObject();
            jsonCreationByUser.put("id",notificationDC.creation_by.Id);

            jsonNotification.put("principal_user",jsonPrincipalUser);
            jsonNotification.put("creation_by",jsonCreationByUser);
            jsonNotification.put("title",notificationDC.title);
            jsonNotification.put("message",notificationDC.message);

            jsonNotification.put("is_read",notificationDC.is_read);
            if(notificationDC.schedule_date!=null){
                jsonNotification.put("schedule_date",TormundManager.FormatDateTime(notificationDC.schedule_date));
            }


            jsonNotification.put("event_key","new");




            HttpURLConnection conn= ApiConnection.getConnection("notifications");
            DataOutputStream os = new DataOutputStream(conn.getOutputStream());
            os.writeBytes(jsonNotification.toString());

            os.flush();
            os.close();

            int responseCode = conn.getResponseCode();
            String responseMessage = conn.getResponseMessage();

            if(responseCode == HttpURLConnection.HTTP_OK){
                String responseString = ApiConnection.readStream(conn.getInputStream());

                JSONArray array= new JSONArray(responseString);
                int top =15;
                if(array.length()>=top){
                    top=15;
                }else{
                    top=array.length();
                }
                if(array.length()>0){
                    for (int i=0 ;i<top;i++) {
                        JSONObject json =array.getJSONObject(i);

                        if(!json.isNull("id")){

                            NotificationDC notificationReturn= new NotificationDC();
                            notificationReturn.id=json.getInt("id");
                            notificationReturn.title=json.getString("title");
                            notificationReturn.message=json.getString("message");
                            notificationReturn.is_read=json.getBoolean("is_read");
                            notificationReturn.notification_type=json.getInt("notification_type");
                            if(!json.isNull("principal_user")){
                                JSONObject jsonUser=new JSONObject();
                                UserDC user= new UserDC();
                                jsonUser=json.getJSONObject("principal_user");
                                user.Id=jsonUser.getInt("id");
                                user.Name=jsonUser.getString("name");
                                notificationReturn.principal_user=user;
                            }
                            notificationReturn.schedule_date=TormundManager.JsonStringToDate2(json.getString("schedule_date"));
                            notificationReturn.creation_date=TormundManager.JsonStringToDate2(json.getString("creation_date"));

                            notificationsReturn.add(notificationReturn);
                        }

                    }

                    return notificationsReturn;
                }else {
                    return new ArrayList<NotificationDC>();
                }



            }else{
                Log.v("CatalogClient", "Response code:"+ responseCode);
                Log.v("CatalogClient", "Response message:"+ responseMessage);
            }
            Log.i("STATUS", String.valueOf(conn.getResponseCode()));
            Log.i("MSG" , conn.getResponseMessage());

            conn.disconnect();
        } catch (Exception e) {
            e.printStackTrace();
            return new ArrayList<NotificationDC>();
        }


        return new ArrayList<NotificationDC>();
    }


    public static ArrayList<NotificationDC> notifications_markAsRead(NotificationDC notificationDC) {

        if(notificationDC==null) return null;
        if(notificationDC.principal_user==null) return null;
        ArrayList<NotificationDC> notificationsReturn=new ArrayList<NotificationDC>();


        try {
            JSONObject jsonNotification=new JSONObject();
            JSONObject jsonPrincipalUser=new JSONObject();
            jsonPrincipalUser.put("id",notificationDC.principal_user.Id);
            jsonNotification.put("id",notificationDC.id);
            jsonNotification.put("principal_user",jsonPrincipalUser);
            jsonNotification.put("is_read",true);
            jsonNotification.put("event_key","mark_as_read");




            HttpURLConnection conn= ApiConnection.getConnection("notifications");
            DataOutputStream os = new DataOutputStream(conn.getOutputStream());
            os.writeBytes(jsonNotification.toString());

            os.flush();
            os.close();

            int responseCode = conn.getResponseCode();
            String responseMessage = conn.getResponseMessage();

            if(responseCode == HttpURLConnection.HTTP_OK){
                String responseString = ApiConnection.readStream(conn.getInputStream());

                JSONArray array= new JSONArray(responseString);
                int top =15;
                if(array.length()>=top){
                    top=15;
                }else{
                    top=array.length();
                }
                if(array.length()>0){
                    for (int i=0 ;i<top;i++) {
                        JSONObject json =array.getJSONObject(i);

                        if(!json.isNull("id")){

                            NotificationDC notificationReturn= new NotificationDC();
                            notificationReturn.id=json.getInt("id");
                            notificationReturn.title=json.getString("title");
                            notificationReturn.message=json.getString("message");
                            notificationReturn.is_read=json.getBoolean("is_read");
                            notificationReturn.notification_type=json.getInt("notification_type");
                            if(!json.isNull("principal_user")){
                                JSONObject jsonUser=new JSONObject();
                                UserDC user= new UserDC();
                                jsonUser=json.getJSONObject("principal_user");
                                user.Id=jsonUser.getInt("id");
                                user.Name=jsonUser.getString("name");
                                notificationReturn.principal_user=user;
                            }
                            notificationReturn.schedule_date=TormundManager.JsonStringToDate2(json.getString("schedule_date"));
                            notificationReturn.creation_date=TormundManager.JsonStringToDate2(json.getString("creation_date"));

                            notificationsReturn.add(notificationReturn);
                        }

                    }

                    return notificationsReturn;
                }else {
                    return new ArrayList<NotificationDC>();
                }



            }else{
                Log.v("CatalogClient", "Response code:"+ responseCode);
                Log.v("CatalogClient", "Response message:"+ responseMessage);
            }
            Log.i("STATUS", String.valueOf(conn.getResponseCode()));
            Log.i("MSG" , conn.getResponseMessage());

            conn.disconnect();
        } catch (Exception e) {
            e.printStackTrace();
            return new ArrayList<NotificationDC>();
        }


        return new ArrayList<NotificationDC>();
    }


}
