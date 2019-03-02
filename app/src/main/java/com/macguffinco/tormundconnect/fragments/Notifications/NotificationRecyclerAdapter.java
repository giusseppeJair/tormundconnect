package com.macguffinco.tormundconnect.fragments.Notifications;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.support.v7.widget.RecyclerView;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.text.style.StyleSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.macguffinco.tormundconnect.Logic.TormundManager;
import com.macguffinco.tormundconnect.R;
import com.macguffinco.model.Notifications.NotificationDC;

import java.util.ArrayList;
import java.util.List;

public class NotificationRecyclerAdapter extends RecyclerView.Adapter<NotificationRecyclerAdapter.ViewHolder> {


    public Context _context;
public List<NotificationDC> _notifications;

public TextView mNotificationMessage;
public TextView mNotificationDate;
public LinearLayout mLinear_layout;

    public NotificationRecyclerAdapter(ArrayList<NotificationDC> notifications){

        _notifications=notifications;

    }

    @Override
    public NotificationRecyclerAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        this._context=parent.getContext();
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.notificacion_list, parent, false);
        return new NotificationRecyclerAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final NotificationRecyclerAdapter.ViewHolder holder, int position) {
        //holder.mAppointmentItem = appointments.get(position);
        holder.bind(_notifications.get(position));


        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }

    @Override
    public int getItemCount() {
        return _notifications.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final ImageView mIdView;



        public ViewHolder(View view) {
            super(view);
            mView = view;

            mView.setBackgroundColor(Color.green(5));

            mIdView = (ImageView) view.findViewById(R.id.item_number);

            mNotificationMessage = (TextView) view.findViewById(R.id.notification_message);
            mNotificationDate=view.findViewById(R.id.date_notification);
            mLinear_layout=view.findViewById(R.id.linear_layout);
        }

        public void bind(NotificationDC notificationMessage){

            if(notificationMessage.title==null) notificationMessage.title="";
            if(notificationMessage.message==null) notificationMessage.message="";

            final SpannableString titleBoldText = new SpannableString(notificationMessage.title);
            final SpannableString message = new SpannableString(notificationMessage.message);
            titleBoldText.setSpan(new StyleSpan(Typeface.BOLD), 0, notificationMessage.title.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);        // texto en bold
            titleBoldText.setSpan(new ForegroundColorSpan(Color.BLACK), 0, notificationMessage.title.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);   // texto color negro
            //titleBoldText.setSpan(new RelativeSizeSpan(1.5f), 0, notificationMessage.Title.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);   // tamaño 1.5f
            message.setSpan(new StyleSpan(Typeface.ITALIC), 0, notificationMessage.message.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);   // en italica

            mNotificationMessage.setText(TextUtils.concat(titleBoldText," " ,message));


            mNotificationDate.setText(TormundManager.getTimeInDaysOrMinutes(notificationMessage.creation_date));

            if(!notificationMessage.is_read){

                mLinear_layout.setBackgroundResource(R.color.notification_unread);
            }else{
                mLinear_layout.setBackgroundResource(R.color.white);
            }

        }

        @Override
        public String toString() {
            return super.toString() + " '" + mNotificationMessage.getText() + "'";
        }
    }

}


