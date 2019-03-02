package com.macguffinco.tormundconnect.activities.DetailBarber;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.macguffinco.tormundconnect.R;
import com.macguffinco.tormundconnect.Utilities.CropCircleTransformation;
import com.macguffinco.tormundconnect.activities.CameraControl.CameraActivity;
import com.macguffinco.tormundconnect.activities.VisorPictureActivity;
import com.macguffinco.tormundconnect.fragments.dummy.DummyContent;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.ArrayList;


public class GridGalleryAdapter extends BaseAdapter{

    Context context;




    private final ArrayList<String> images;
    View view;
    LayoutInflater layoutInflater;

    public GridGalleryAdapter(Context context, ArrayList<String> images){
        super();
        this.context=context;
        this.images=images;


    }

    @Override
    public int getCount() {
       return images.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        final String imageSelected=images.get(position);

        layoutInflater=(LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if(convertView==null){

            view=new View(context);
            view= layoutInflater.inflate(R.layout.single_picture,null);
            ImageView imageview=(ImageView)view.findViewById(R.id.image_gallery);


            if(images.get(position).contains("http")){

                Picasso.with(context)

                        .load("https://s3.us-east-2.amazonaws.com/tormund-repository/NOPICTURE.png")
                        //.transform(new CropCircleTransformation())
                        .resize(250, 250)
                        .into(imageview);

            }else{
                File file= new File(images.get(position));
                Picasso.with(context)
                        .load(file) //extract as User instance method
                      //  .transform(new CropCircleTransformation())
                        .resize(250, 250)
                        .into(imageview);

                imageview.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent= new Intent(context, VisorPictureActivity.class);
                        try {
                            intent.putExtra("imageSelected",imageSelected);
                            //intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
                            context.startActivity(intent);


                        }catch (Exception ex){

                            ex.printStackTrace();
                        }
                    }
                });


            }


        }

        return view;
    }
}
