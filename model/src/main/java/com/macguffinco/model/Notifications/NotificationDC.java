package com.macguffinco.model.Notifications;

import com.macguffinco.model.Solution.UserDC;

import java.util.ArrayList;
import java.util.Date;

public class NotificationDC {

    public int id ;
    public String title ;
    public String message;
    public UserDC principal_user ;
    public boolean is_read ;
    public ArrayList<UserDC> other_users ;
    public int notification_type ;
    public Date schedule_date ;
    public Date creation_date ;
    public int register_quantity ;
    public String event_key ;
    public UserDC creation_by ;

}
