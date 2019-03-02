package com.macguffinco.tormundconnect.activities.DetailBarber;

import android.app.Activity;
import android.os.AsyncTask;
import android.widget.GridView;

import com.macguffinco.tormundconnect.Logic.PictureManager;
import com.macguffinco.tormundconnect.activities.DetailBarber.GridGalleryAdapter;
import com.macguffinco.model.appointments.AppointmentDC;

import java.util.ArrayList;

public class TaskDetailCustomerPicture extends AsyncTask<String, Integer, Long> {

    Activity _detailCustomerActivity;
    AppointmentDC _appointmentDC;
    GridView _gridView;
    ArrayList<String> _listOfPictures;

    public TaskDetailCustomerPicture(Activity detailCustomerActivity, AppointmentDC appointmentDC,GridView gridView){

        _detailCustomerActivity=detailCustomerActivity;
        _appointmentDC=appointmentDC;
        _gridView=gridView;

    }
    protected Long doInBackground(String... data) {


        try {
            ArrayList<String> customer_pictures= PictureManager.GetCustomerImagesFromStorage(_appointmentDC);
            if(!customer_pictures.isEmpty()){
                _listOfPictures=new ArrayList<>();
                _listOfPictures.addAll(customer_pictures);

                for (String path:customer_pictures) {
                    if(path.equals("")) continue;

//                    Picasso.with(_detailCustomerActivity)
//                            .load("https://graph.facebook.com/v2.2/" + TormundManager.facebookId + "/picture?height=250&type=normal") //extract as User instance method
//                            .transform(new CropCircleTransformation())
//                            .resize(250, 250)
//                            .into(profilePic);

//
//



                }




            }
            //_detailCustomerActivity

        }catch (Exception ex){
            ex.printStackTrace();

        }
        return 0L;

    }

    protected void onProgressUpdate(Integer... progress) {
        String hola="";
    }

    protected void onPostExecute(Long result) {




        GridGalleryAdapter gridAdapter=new GridGalleryAdapter(_detailCustomerActivity,_listOfPictures);
        _gridView.setAdapter(gridAdapter);

    }

}
