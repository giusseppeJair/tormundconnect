package com.macguffinco.tormundconnect.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;

import com.macguffinco.tormundconnect.R;
import com.squareup.picasso.Picasso;

import java.io.File;

public class VisorPictureActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_visor_picture);

        String image=getIntent().getStringExtra("imageSelected");

        ImageView imageView=  findViewById(R.id.pictureOpen);

        if(imageView!=null &&  image!=null && image!=""){

            File file= new File(image);
            Picasso.with(this)
                    .load(file) //extract as User instance method
                    //  .transform(new CropCircleTransformation())
                    //.resize(250, 250)
                    .into(imageView);
        }
    }


}
