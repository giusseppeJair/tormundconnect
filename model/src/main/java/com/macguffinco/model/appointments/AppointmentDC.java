package com.macguffinco.model.appointments;

import com.macguffinco.model.Solution.UserDC;
import com.macguffinco.model.comercial.BranchDC;
import com.macguffinco.model.comercial.CustomerDC;
import com.macguffinco.model.comercial.EmployeeDC;
import com.macguffinco.model.comercial.ServiceDC;

import java.util.Date;

public class AppointmentDC {

    public int Id ;
    public String Description ;
    public BranchDC Branch ;
    public ServiceDC Service ;
    public EmployeeDC Employee ;
    public int Employee_id;
    public CustomerDC Customer ;
    public Date AppointmentDate ;
    public int Customer_id;
    public Date DueDate ;
    public Date CreationDate ;
    public Date LastModified ;
    public UserDC CreatedBy ;
    public UserDC ModifiedBy ;
    public byte[] Photo ;
    public int Status ;
    public String status_list;
    public int SubStatus ;
    public String FromApp;
    public String event_key;
    public int tormund_app ;

}
