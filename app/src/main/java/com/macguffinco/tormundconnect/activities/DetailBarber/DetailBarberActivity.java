package com.macguffinco.tormundconnect.activities.DetailBarber;

import android.content.Context;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.macguffinco.tormundconnect.Logic.TormundManager;
import com.macguffinco.tormundconnect.R;
import com.macguffinco.tormundconnect.Utilities.CropCircleTransformation;
import com.macguffinco.model.comercial.CommentDC;
import com.macguffinco.model.comercial.CustomerDC;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class DetailBarberActivity extends AppCompatActivity {


    TextView close_detail;
    TextView detail_customer_name;
    TextView detail_customer_member_since;
    TextView detail_customer_quantityAppointments;
    GridView      gridview;
    ImageView imageView;
    RecyclerView recyclerViewCustomerComments;
    public ArrayList<CommentDC> CustomerComments;
    LinearLayout mImageContainer;
    EditText editTextComment;

    DetailBarberActivity activity;
    LastCustomerAppointmentRecyclerAdapter mAdapterComments;
    FloatingActionButton mImageViewSendComment;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_detail_barber);

        close_detail= findViewById(R.id.close_detail);
        mImageContainer=findViewById(R.id.image_container);

         editTextComment=findViewById(R.id.editTextNewComment);
        mImageViewSendComment = (FloatingActionButton) findViewById(R.id.btnSaveComment);
        activity=this;
        mImageViewSendComment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String text=editTextComment.getText().toString();

                if(text.length()>0){
                    CommentDC commentDC= new CommentDC();
                    commentDC.message=text;
                    commentDC.employee=TormundManager.GlobalEmployeeDC;
                    commentDC.customer=TormundManager.GlobalAppointmentSelected.Customer;
                    commentDC.customer.comments=new ArrayList<>();
                    mImageViewSendComment.setEnabled(false);
                    new TaskSaveComment(activity,commentDC.customer,commentDC,mAdapterComments).execute("", "", "");
                }
                InputMethodManager inputManager = (InputMethodManager)
                        getSystemService(Context.INPUT_METHOD_SERVICE);

                inputManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(),
                        InputMethodManager.HIDE_NOT_ALWAYS);
            }
        });


                if(TormundManager.GlobalAppointmentSelected!=null && TormundManager.GlobalAppointmentSelected.Customer!=null){

            detail_customer_name=findViewById(R.id.detail_customer_name);
            detail_customer_member_since=findViewById(R.id.detail_customer_member_since);
            detail_customer_quantityAppointments=findViewById(R.id.detail_customer_quantityAppointments);

            if(detail_customer_name!=null ){
                detail_customer_name.setText(TormundManager.GlobalAppointmentSelected.Customer.name);
            }
            if(detail_customer_member_since!=null && TormundManager.GlobalAppointmentSelected.Customer.creationDate!=null){
                detail_customer_member_since.setText("Miembro desde: "+TormundManager.FormatDate(TormundManager.GlobalAppointmentSelected.Customer.creationDate));
            }
            if(detail_customer_quantityAppointments!=null ){
                detail_customer_quantityAppointments.setText("Cantidad Citas: "+TormundManager.GlobalAppointmentSelected.Customer.cantidadCitas);
            }
            recyclerViewCustomerComments=findViewById(R.id.customer_comments);
            imageView=findViewById(R.id.profile_Detail_Customer);

            if(   TormundManager.GlobalAppointmentSelected.Customer.user!=null &&
                    TormundManager.GlobalAppointmentSelected.Customer.user.TokenFacebook!=null &&
                    !TormundManager.GlobalAppointmentSelected.Customer.user.TokenFacebook.equals("")){

                Picasso.with(this)

                        .load("https://graph.facebook.com/v2.2/" + TormundManager.GlobalAppointmentSelected.Customer.user.TokenFacebook + "/picture?height=250&type=normal")
                        .transform(new CropCircleTransformation())
                        .resize(150, 150)

                        .into(imageView);
            }else{
                Picasso.with(this)

                        .load("https://s3.us-east-2.amazonaws.com/tormund-repository/NOPICTURE.png")
                        .transform(new CropCircleTransformation())
                        .resize(150, 150)

                        .into(imageView);
            }

        }
         gridview= this.findViewById(R.id.gridGallery);

        FillCustomerPictures();
        FillComments();


        recyclerViewCustomerComments.setLayoutManager(new LinearLayoutManager(this));
        mAdapterComments= new LastCustomerAppointmentRecyclerAdapter(CustomerComments, new LastCustomerAppointmentRecyclerAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(CommentDC item) {
                return;
            }
        });
        recyclerViewCustomerComments.setAdapter(mAdapterComments);
        recyclerViewCustomerComments.addItemDecoration(new DividerItemDecoration(recyclerViewCustomerComments.getContext(), DividerItemDecoration.VERTICAL));


        close_detail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               finish();
            }
        });
    }


    private void FillCustomerPictures(){

        if(TormundManager.GlobalAppointmentSelected==null) return;



            new TaskDetailCustomerPicture(this,TormundManager.GlobalAppointmentSelected,gridview).execute("","","");

    }

    private void FillComments(){
        if(TormundManager.GlobalAppointmentSelected==null) return;

        CustomerDC customer=TormundManager.GlobalAppointmentSelected.Customer;

        CustomerComments=new ArrayList<CommentDC>();
        if(customer.comments!=null && !customer.comments.isEmpty()){
            CustomerComments.addAll(customer.comments);
        }else{

            CustomerComments= new ArrayList<>();
            CommentDC commentDC= new CommentDC();
            commentDC.title="";
            commentDC.message=TormundManager.NotComments;
            CustomerComments.add(commentDC);
        }



    }

}
