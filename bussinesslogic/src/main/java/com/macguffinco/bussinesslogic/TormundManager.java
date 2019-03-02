package com.macguffinco.bussinesslogic;

import android.content.Context;
import android.content.Intent;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public final class TormundManager {

    public static void goCameraActivity(Context context){
        Intent intent= new Intent(context, CameraActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

    public static void goMainActivity(Context context){
        Intent intent= new Intent(context, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }



    public static Date JsonStringToDate(String strDate){
        java.util.Date result;
        try{
            String target = strDate;
            DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS");
            result =  df.parse(target);
            return result;

        }catch (Exception e){

        }

        return new Date();
    }

    public static Date JsonStringToDate2(String strDate){
        java.util.Date result;
        try{
            String target = strDate;
            DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm");
            result =  df.parse(target);
            return result;

        }catch (Exception e){

        }

        return new Date();
    }

    public static String JsonDateToHours(Date date){
        String result;
        try{
            DateFormat format = new SimpleDateFormat( "HH:mm");
            result = format.format(date);

            return result;

        }catch (Exception e){

        }

        return "";
    }


    public static String FormatDate(Date date){
        String result="";
        try{
            DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
            result =  df.format(date);
            return result;

        }catch (Exception e){

        }

        return result;
    }

    public static String FormatDateTime(Date date){
        String result="";
        try{
            DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm");
            result =  df.format(date);
            return result;

        }catch (Exception e){

        }

        return result;
    }

    public static String FormatHour(Date date){
        String result="";
        try{
            DateFormat df = new SimpleDateFormat("hh:mm a");
            result =  df.format(date);
            return result;

        }catch (Exception e){

        }

        return result;
    }




}
