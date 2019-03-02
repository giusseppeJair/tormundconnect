package com.macguffinco.tormundconnect.fragments;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.macguffinco.tormundconnect.Logic.TormundManager;
import com.macguffinco.tormundconnect.R;
import com.macguffinco.tormundconnect.Utilities.CropCircleTransformation;
import com.macguffinco.tormundconnect.fragments.AppointmentFragment.OnListFragmentInteractionListener;
import com.macguffinco.tormundconnect.fragments.dummy.DummyContent.DummyItem;
import com.macguffinco.model.appointments.AppointmentDC;
import com.macguffinco.model.comercial.CustomerDC;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * {@link RecyclerView.Adapter} that can display a {@link DummyItem} and makes a call to the
 * specified {@link OnListFragmentInteractionListener}.
 * TODO: Replace the implementation with code for your data type.
 */
public class AppointmentRecyclerViewAdapter extends RecyclerView.Adapter<AppointmentRecyclerViewAdapter.ViewHolder> {

    public final List<AppointmentDC> appointments;
    private final OnListFragmentInteractionListener mListener;
    private static Context _context=null;


    public AppointmentRecyclerViewAdapter(List<AppointmentDC> appointments, OnListFragmentInteractionListener listener) {
        this.appointments = appointments;
        mListener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        this._context=parent.getContext();
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_item_appointment, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        //holder.mAppointmentItem = appointments.get(position);
        holder.bind(appointments.get(position));


        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != mListener) {
                    // Notify the active callbacks interface (the activity, if the
                    // fragment is attached to one) that an item has been selected.
                    mListener.onListFragmentInteraction(holder.mAppointmentItem);

                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return appointments.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final ImageView mIdView;

        public final TextView mCustomerName;
        public final TextView mAppointmentService;
        public final TextView mAppointmentHorary;
        public AppointmentDC mAppointmentItem;

        public ViewHolder(View view) {
            super(view);
            mView = view;

           mView.setBackgroundColor(Color.green(5));

            mIdView = (ImageView) view.findViewById(R.id.item_number);
           // mContentView = (TextView) view.findViewById(R.id.content);
            mCustomerName = (TextView) view.findViewById(R.id.appointment_customer_name);
            mAppointmentService = (TextView) view.findViewById(R.id.appointment_service);
            mAppointmentHorary = (TextView) view.findViewById(R.id.appointment_horary);
            mAppointmentItem=new AppointmentDC();

        }

        public void bind(AppointmentDC appointmentDC){

            if(appointmentDC.Customer.name!=null && appointmentDC.Customer.name.length()>0){
                mCustomerName.setText(appointmentDC.Customer.name);
            }
            if(appointmentDC.Service.Name!=null && appointmentDC.Service.Name.length()>0){
                mAppointmentService.setText(appointmentDC.Service.Name);
            }
            if(appointmentDC.Service.Name!=null && appointmentDC.Service.Name.length()>0){
                mAppointmentService.setText(appointmentDC.Service.Name);
            }
            if(appointmentDC.AppointmentDate!=null && appointmentDC.DueDate!=null){
                String startAppointmentHours=TormundManager.FormatHourAMPM(appointmentDC.AppointmentDate);
                String finishedAppointmentHours=TormundManager.FormatHourAMPM(appointmentDC.DueDate);

                mAppointmentHorary.setText(startAppointmentHours+" a "+ finishedAppointmentHours);
            }
            mAppointmentItem=appointmentDC;

            if(mAppointmentItem!=null &&
                    mAppointmentItem.Customer!=null &&
                    mAppointmentItem.Customer.user!=null &&
                    mAppointmentItem.Customer.user.TokenFacebook!=null &&
                    !mAppointmentItem.Customer.user.TokenFacebook.equals("")){

                Picasso.with(_context)

                        .load("https://graph.facebook.com/v2.2/" + mAppointmentItem.Customer.user.TokenFacebook + "/picture?height=250&type=normal")
                        .transform(new CropCircleTransformation())
                        .resize(250, 250)
                        .into(mIdView);
            }else{
                Picasso.with(_context)

                        .load("https://s3.us-east-2.amazonaws.com/tormund-repository/NOPICTURE.png")
                        .transform(new CropCircleTransformation())
                        .resize(250, 250)
                        .into(mIdView);
            }

        }

        @Override
        public String toString() {
            return super.toString() + " '" + mCustomerName.getText() + "'";
        }
    }
}
