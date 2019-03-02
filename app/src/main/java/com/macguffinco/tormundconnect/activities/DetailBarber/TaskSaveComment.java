package com.macguffinco.tormundconnect.activities.DetailBarber;

import android.os.AsyncTask;
import android.widget.EditText;

import com.macguffinco.tormundconnect.Logic.CustomerManager;
import com.macguffinco.tormundconnect.Logic.TormundManager;
import com.macguffinco.tormundconnect.R;
import com.macguffinco.model.comercial.CommentDC;
import com.macguffinco.model.comercial.CustomerDC;

import java.util.ArrayList;

public class TaskSaveComment extends AsyncTask<String, Integer, Long> {
    DetailBarberActivity _detailCustomerActivity;
    CustomerDC _customerDC;
    CustomerDC customerReturn;
    CommentDC _commentDC;
    LastCustomerAppointmentRecyclerAdapter _mAdapterComments;

    ArrayList<CustomerDC> _listOfPictures;

    public TaskSaveComment(DetailBarberActivity detailCustomerActivity, CustomerDC customerDC, CommentDC commentDC, LastCustomerAppointmentRecyclerAdapter mAdapterComments){

        _detailCustomerActivity=detailCustomerActivity;
        _customerDC=customerDC;
        _customerDC.comments=new ArrayList<>();
        _customerDC.comments.add(commentDC);
        _mAdapterComments=mAdapterComments;


    }
    protected Long doInBackground(String... data) {


        try {

              customerReturn=CustomerManager.AddCustomerComment(_customerDC);


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
        if((customerReturn.comments.size()>0 && !customerReturn.comments.get(0).message.equals(TormundManager.NotComments)) ){

            TormundManager.GlobalAppointmentSelected.Customer.comments=customerReturn.comments;
            EditText editTextComment= _detailCustomerActivity.findViewById(R.id.editTextNewComment);
            if(editTextComment!=null){
                editTextComment.setText("");
                editTextComment.clearFocus();
            }

            _detailCustomerActivity.CustomerComments.clear();
            _detailCustomerActivity.CustomerComments.addAll(customerReturn.comments);
            _detailCustomerActivity.mAdapterComments.notifyDataSetChanged();
            _detailCustomerActivity.recyclerViewCustomerComments.scrollToPosition(0);

            _detailCustomerActivity.mImageViewSendComment.setEnabled(true);
        }

    }catch(Exception ex){

    }




    }

}
