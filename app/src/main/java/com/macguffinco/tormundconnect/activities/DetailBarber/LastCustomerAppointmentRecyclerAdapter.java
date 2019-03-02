package com.macguffinco.tormundconnect.activities.DetailBarber;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.macguffinco.tormundconnect.R;

import com.macguffinco.tormundconnect.Logic.TormundManager;

import com.macguffinco.model.comercial.CommentDC;

import java.util.List;

public class LastCustomerAppointmentRecyclerAdapter extends RecyclerView.Adapter<LastCustomerAppointmentRecyclerAdapter.ViewHolderLastAppointment> {


    public interface OnItemClickListener {
        void onItemClick(CommentDC item);
    }

    private final List<CommentDC> mValues;
    private LastCustomerAppointmentRecyclerAdapter.OnItemClickListener listener;
    //private final DetailBarberActivity.onClick mListener;

    public LastCustomerAppointmentRecyclerAdapter(List<CommentDC> items, LastCustomerAppointmentRecyclerAdapter.OnItemClickListener listener ) {

        mValues = items;
        this.listener = listener;
    }

    @Override
    public LastCustomerAppointmentRecyclerAdapter.ViewHolderLastAppointment onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.customer_comments_item, parent, false);
        return new LastCustomerAppointmentRecyclerAdapter.ViewHolderLastAppointment(view);
    }

    @Override
    public void onBindViewHolder(final LastCustomerAppointmentRecyclerAdapter.ViewHolderLastAppointment holder, int position) {
        holder.bind(mValues.get(position));

    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public static class ViewHolderLastAppointment extends RecyclerView.ViewHolder {
        public final View mView;
        public final ImageView mIdView;
        public final TextView comment_date;
      //  public final TextView comment_title;
        public final TextView comment_message;
        public final TextView comment_employee;
        public CommentDC mItem;


        public ViewHolderLastAppointment(View view) {
            super(view);
            mView = view;
           mIdView = (ImageView) view.findViewById(R.id.item_number);

           // comment_title = (TextView) view.findViewById(R.id.date_appointment);
            comment_message = (TextView) view.findViewById(R.id.comment_message);
            comment_date = (TextView) view.findViewById(R.id.comment_date);
            comment_employee = (TextView) view.findViewById(R.id.comment_employee);
        }

        @Override
        public String toString() {
            return super.toString() + " '" + comment_date.getText() + "'";
        }

        public void bind(final CommentDC item) {


            if(item.message!=null && !item.message.equals("")){
                comment_message.setText(item.message);
            }
            if(item.employee!=null && item.employee.name!=null && !item.employee.name.equals("")){
                comment_employee.setText(item.employee.name);
            }
            if(item.creation_date!=null ){

                comment_date.setText(TormundManager.getTimeInDaysOrMinutes(item.creation_date));
            }


        }

    }

}


