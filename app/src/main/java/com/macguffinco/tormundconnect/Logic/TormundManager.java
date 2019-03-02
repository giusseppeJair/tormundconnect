package com.macguffinco.tormundconnect.Logic;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.util.Base64;
import android.util.Log;

import com.macguffinco.tormundconnect.activities.MainActivity;
import com.macguffinco.tormundconnect.Solution.ApiConnection;
import com.macguffinco.tormundconnect.activities.CameraControl.CameraActivity;
import com.macguffinco.tormundconnect.activities.Welcome.WelcomeActivity;
import com.macguffinco.model.Solution.UserDC;
import com.macguffinco.model.VaultFiles.VaultFileDC;
import com.macguffinco.model.appointments.AppointmentDC;
import com.macguffinco.model.comercial.BranchDC;
import com.macguffinco.model.comercial.CompanyDC;
import com.macguffinco.model.comercial.CustomerDC;
import com.macguffinco.model.comercial.EmployeeDC;
import com.macguffinco.model.comercial.ServiceDC;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.DataOutputStream;
import java.io.File;
import java.net.HttpURLConnection;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.Locale;
import java.util.Random;

import static android.content.ContentValues.TAG;

public final class TormundManager {

    public static EmployeeDC GlobalEmployeeDC;
    public static BranchDC GlobalBranchDC;
    public static CompanyDC GlobalCompanyDC;
    public static String GlobalServer="dcisneros-001-site2.dtempurl.com";
    public static String GlobalPicturePath;
    public static AppointmentDC GlobalAppointmentSelected;
    public static ArrayList<EmployeeDC> GlobalEmployeesList= new ArrayList<EmployeeDC>();
    public static ArrayList<ServiceDC> GlobalServicesList= new ArrayList<ServiceDC>();;
    public static ArrayList<CustomerDC> GlobalCustomersList= new ArrayList<CustomerDC>();;

    public static String ImageDefaultUrl="https://s3.us-east-2.amazonaws.com/tormund-repository/NOPICTURE.png";
    public static String NotComments="No existen comentarios.";


    public static void goCameraActivity(Context context,AppointmentDC appointmentDC){
        Intent intent= new Intent(context, CameraActivity.class);
        try {


                intent.putExtra("Appointment",ConvertAppointmentDCToJsonString(appointmentDC));
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);


        }catch (Exception ex){

            ex.printStackTrace();
        }


    }

    public static AppointmentDC ConvertStringToAppointmentDC(String jsonString,AppointmentDC appointmentDC){

        try{
           if(appointmentDC==null) appointmentDC=new AppointmentDC();

            JSONObject json=new JSONObject(jsonString);
            if(json.getString("id")!="0"){
                DateFormat df = new SimpleDateFormat("EEE MMM dd kk:mm:ss z yyyy", Locale.ENGLISH);

                appointmentDC.Id=json.getInt("id");
                if(!json.isNull("description")){
                    appointmentDC.Description=json.getString("description");
                }
                if(!json.isNull("appointment_date")){
                    appointmentDC.AppointmentDate= TormundManager.JsonStringToDate2(json.getString("appointment_date"));
                }
                if(!json.isNull("due_date")){
                    appointmentDC.DueDate= TormundManager.JsonStringToDate2(json.getString("due_date"));
                }



                if(!json.isNull("from_app")){
                    appointmentDC.FromApp=json.getString("from_app");
                }
                if(!json.isNull("status")){
                    appointmentDC.Status=json.getInt("status");
                }
                if(!json.isNull("creation_date")){
                    appointmentDC.CreationDate= TormundManager.JsonStringToDate2(json.getString("creation_date"));
                }
                if(!json.isNull("lastModified")){
                    appointmentDC.LastModified= TormundManager.JsonStringToDate2(json.getString("lastModified"));
                }




                if(!json.isNull("customer")){
                    JSONObject jsonCustomer=json.getJSONObject("customer");
                    CustomerDC customerDC=new CustomerDC();
                    customerDC.id=jsonCustomer.getInt("id");
                    customerDC.name=jsonCustomer.getString("name");
                    customerDC.cantidadCitas=jsonCustomer.getInt("cantidadCitas");
                    if(!jsonCustomer.isNull("branch")){
                        JSONObject jsonCustomerBranch=jsonCustomer.getJSONObject("branch");
                        BranchDC branch=new BranchDC();
                        branch.Id=jsonCustomerBranch.getInt("id");
                        branch.BranchName=jsonCustomerBranch.getString("branchName");
                        appointmentDC.Customer.branch=branch;

                        if(!jsonCustomerBranch.isNull("companyDC")){
                            JSONObject jsonCustomerCompany=jsonCustomerBranch.getJSONObject("companyDC");
                            CompanyDC companyDC=new CompanyDC();
                            companyDC.Id=jsonCustomerCompany.getInt("id");
                            companyDC.Name=jsonCustomerCompany.getString("name");
                            appointmentDC.Customer.branch.CompanyDC=companyDC;
                        }

                    }


                    appointmentDC.Customer=customerDC;
                }

                if(!json.isNull("service")){
                    JSONObject jsonService=json.getJSONObject("service");
                    ServiceDC service=new ServiceDC();
                    service.service_id=jsonService.getInt("service_id");
                    service.Name=jsonService.getString("name");
                    service.Cost=jsonService.getDouble("cost");
                    service.Price=jsonService.getDouble("price");
                    service.servicetime=jsonService.getDouble("servicetime");
                    appointmentDC.Service=service;
                }
                if(!json.isNull("employee")){
                    JSONObject jsonBarber=json.getJSONObject("employee");
                    EmployeeDC barber=new EmployeeDC();
                    barber.id=jsonBarber.getInt("id");
                    barber.name=jsonBarber.getString("name");
                    if(!jsonBarber.isNull("vault_file_id")){
                        JSONObject jsonVault=jsonBarber.getJSONObject("vault_file_id");
                        VaultFileDC vault=new VaultFileDC();
                        vault.Id=jsonVault.getInt("id");
                        vault.Url=jsonVault.getString("url");
                        barber.vault_file_id=vault;
                    }
                    if(!jsonBarber.isNull("repo_files")){
                        JSONArray jsonVaultArray=jsonBarber.getJSONArray("repo_files");
                        ArrayList<VaultFileDC> pictures=new ArrayList<VaultFileDC>();
                        for(int k=0;k<jsonVaultArray.length();k++){
                            VaultFileDC vault=new VaultFileDC();
                            JSONObject obj_array =jsonVaultArray.getJSONObject(k);
                            vault.Id=obj_array.getInt("id");
                            vault.Name=obj_array.getString("name");
                            vault.Url=obj_array.getString("url");
                            pictures.add(vault);
                        }
                    }
                    if(!jsonBarber.isNull("barber_round_picture_url")){
                        barber.barber_round_picture_url=jsonBarber.getString("barber_round_picture_url");

                    }
                    appointmentDC.Employee=barber;
                }

                if(!json.isNull("branch")){
                    JSONObject jsonBranch=json.getJSONObject("branch");
                    BranchDC branch=new BranchDC();
                    branch.Id=jsonBranch.getInt("id");
                    branch.BranchName=jsonBranch.getString("branchName");
                    appointmentDC.Branch=branch;

                    if(!jsonBranch.isNull("companyDC")){
                        JSONObject jsonCompany=jsonBranch.getJSONObject("companyDC");
                        CompanyDC companyDC=new CompanyDC();
                        companyDC.Id=jsonCompany.getInt("id");
                        companyDC.Name=jsonCompany.getString("name");
                        appointmentDC.Branch.CompanyDC=companyDC;
                    }

                }


            return appointmentDC;
            }

        }catch(Exception ex){
            return null;

        }

        return null;
    }

    public static String ConvertAppointmentDCToJsonString(AppointmentDC appointmentDC) {

        JSONObject jsonAppointment = new JSONObject();

        try {

            if (appointmentDC != null) {

                jsonAppointment.put("id", appointmentDC.Id);
                if (appointmentDC.Customer != null) {
                    JSONObject jsonCustomer = new JSONObject();
                    jsonCustomer.put("id", appointmentDC.Customer.id);
                    jsonCustomer.put("name", appointmentDC.Customer.name);
                    jsonCustomer.put("cantidadCitas", appointmentDC.Customer.cantidadCitas);
                    if(appointmentDC.Customer.branch!=null){

                        JSONObject jsonCustomerBranch = new JSONObject();
                        jsonCustomerBranch.put("id", appointmentDC.Customer.branch.Id);
                        jsonCustomerBranch.put("branchName", appointmentDC.Customer.branch.BranchName);

                        if(appointmentDC.Customer.branch.CompanyDC!=null){

                            JSONObject jsonCustomerCompany = new JSONObject();
                            jsonCustomerCompany.put("id", appointmentDC.Customer.branch.CompanyDC.Id);
                            jsonCustomerCompany.put("name", appointmentDC.Customer.branch.CompanyDC.Name);

                        }

                        jsonCustomer.put("branch",jsonCustomerBranch);

                    }

                    jsonAppointment.put("customer", jsonCustomer);
                }
                if (appointmentDC.Employee != null) {
                    JSONObject jsonEmployee = new JSONObject();
                    jsonEmployee.put("id", appointmentDC.Employee.id);
                    jsonEmployee.put("name", appointmentDC.Employee.name);

                    jsonAppointment.put("employee", jsonEmployee);
                }

                if (appointmentDC.Service != null) {
                    JSONObject jsonService = new JSONObject();
                    jsonService.put("service_id", appointmentDC.Service.service_id);
                    jsonService.put("name", appointmentDC.Service.Name);
                    jsonService.put("cost", appointmentDC.Service.Cost);
                    jsonService.put("price", appointmentDC.Service.Price);
                    jsonService.put("servicetime", appointmentDC.Service.servicetime);

                    jsonAppointment.put("service", jsonService);
                }

                if (appointmentDC.Branch != null) {
                    JSONObject jsonBranch = new JSONObject();
                    jsonBranch.put("id", appointmentDC.Branch.Id);
                    jsonBranch.put("branchName", appointmentDC.Branch.BranchName);


                    if (appointmentDC.Branch.CompanyDC != null) {
                        JSONObject jsonCompany = new JSONObject();
                        jsonCompany.put("id", appointmentDC.Branch.CompanyDC.Id);
                        jsonCompany.put("name", appointmentDC.Branch.CompanyDC.Name);
                        jsonCompany.put("email", appointmentDC.Branch.CompanyDC.Email);

                        jsonBranch.put("companyDC", jsonCompany);
                    }
                    jsonAppointment.put("branch", jsonBranch);

                }

            }

        } catch (Exception ex) {
            return null;
        }

        return jsonAppointment.toString();
    }

    public static String getCustomerFileName(AppointmentDC appointmentDC){

        if(appointmentDC==null ) return null;
        if(appointmentDC.Branch==null ) return null;
        if(appointmentDC.Branch.CompanyDC==null ) return null;
        if(appointmentDC.Customer==null ) return null;

        String companyName=appointmentDC.Branch.CompanyDC.Name.trim().replace(" ","");
        String branchName=appointmentDC.Branch.BranchName.trim().replace(" ","");
        String customerId=appointmentDC.Customer.id+"".replace(" ","");

        return companyName+"_"+branchName+"_"+customerId;

    }

    public static void goMainActivity(Context context){
        Intent intent= new Intent(context, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

    public static void goWelcomeActivity(Context context){
        Intent intent= new Intent(context, WelcomeActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

    public static ArrayList<AppointmentDC> GetDatesEmployees(){

        ArrayList<AppointmentDC> appointments= new ArrayList<AppointmentDC>();

        CustomerDC customer=new CustomerDC();
        customer.name="Jair Aguirre";
        ServiceDC service=new ServiceDC();
        service.Name="Corte";

        CustomerDC customer2=new CustomerDC();
        customer2.name="Alma Hernandez";
        ServiceDC service2=new ServiceDC();
        service2.Name="Afilado";


        Date date=new Date();


        AppointmentDC pq=new AppointmentDC();
        pq.Id=1;
        pq.Customer=customer;
        pq.Service=service;
        pq.AppointmentDate=date;
        pq.DueDate=date;

        AppointmentDC p2=new AppointmentDC();
        p2.Id=2;
        p2.Customer=customer2;
        p2.Service=service2;
        p2.AppointmentDate=date;
        p2.DueDate=date;

        appointments.add(pq);
        appointments.add(p2);

        return  appointments;
    }

    public static Date JsonStringToDate(String strDate){
        java.util.Date result;
        try{
            String target = strDate;
            DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS");
            result =  df.parse(target);
            return result;

        }catch (Exception e){

        }

        return new Date();
    }

    public static Date JsonStringToDate2(String strDate){
        java.util.Date result;
        try{
            String target = strDate;
            DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm");
            result =  df.parse(target);
            return result;

        }catch (Exception e){
            e.printStackTrace();
        }

        return new Date();
    }

    public static String JsonDateToHours(Date date){
        String result;
        try{
            DateFormat format = new SimpleDateFormat( "HH:mm");
            result = format.format(date);

            return result;

        }catch (Exception e){
            e.printStackTrace();
        }

        return "";
    }


    public static String FormatDate(Date date){
        String result="";
        try{
            DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
            result =  df.format(date);
            return result;

        }catch (Exception e){

        }

        return result;
    }

    public static String getTimeInDaysOrMinutes(Date date){

        try{
            String value="";
            Calendar calendar= Calendar.getInstance();
            long diferenciaSegundos=(long) ((calendar.getTimeInMillis()-date.getTime())/1000);

            int dias=0;
            int horas=0;
            int minutos=0;
            if(diferenciaSegundos<60){
                value="hace "+diferenciaSegundos+" seg.";
            }
            if(diferenciaSegundos>=60 && diferenciaSegundos<3600){
                value="hace "+Math.round(Math.floor(diferenciaSegundos/60))+" min.";
            }
            if(diferenciaSegundos>=3600 && diferenciaSegundos<86400){
                value="hace "+Math.round(Math.floor(diferenciaSegundos/3600))+" horas.";
            }
            if(diferenciaSegundos>=86400 && diferenciaSegundos<2592000){
                value="hace "+Math.round(Math.floor(diferenciaSegundos/86400))+" días.";
            }
            if(diferenciaSegundos>2592000) {
                value=TormundManager.FormatDateTime(date);
            }

            return value;

        }catch (Exception ex){

            ex.printStackTrace();
            return "";
        }

    }

    public static String FormatDateTime(Date date){
        String result="";
        try{
            DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm");
            result =  df.format(date);
            return result;

        }catch (Exception e){

        }

        return result;
    }

    public static String FormatHourAMPM(Date date){
        String result="";
        try{
            DateFormat df = new SimpleDateFormat("hh:mm a");
            result =  df.format(date);
            return result;

        }catch (Exception e){

        }

        return result;
    }

    public static String FormatHour(Date date){
        String result="";
        try{
            DateFormat df = new SimpleDateFormat("hh:mm");
            result =  df.format(date);
            return result;

        }catch (Exception e){

        }

        return result;
    }

    public static String FormatHourMinutesSeconds(Date date){
        String result="";
        try{
            DateFormat df = new SimpleDateFormat("HH:mm:ss");
            result =  df.format(date);
            return result;

        }catch (Exception e){

        }

        return result;
    }


    public static void  FillCataloguesByBranch(){

        EmployeeDC employeeSearch=new EmployeeDC();
        employeeSearch.branch=TormundManager.GlobalBranchDC;
        employeeSearch.type_employee=1;
        TormundManager.GlobalEmployeesList= TormundManager.GetEmployeeByBranch(employeeSearch);
        Collections.sort(TormundManager.GlobalEmployeesList, new Comparator<EmployeeDC>() {
            public int compare(EmployeeDC obj1, EmployeeDC obj2) {
                return obj1.name.compareTo(obj2.name);
            }
        });
        ServiceDC serviceSearch=new ServiceDC();
        serviceSearch.Branch=TormundManager.GlobalBranchDC;
        TormundManager.GlobalServicesList= TormundManager.GetServicesByBranch(serviceSearch);
        Collections.sort(TormundManager.GlobalServicesList, new Comparator<ServiceDC>() {
            public int compare(ServiceDC obj1, ServiceDC obj2) {
                return obj1.Name.compareTo(obj2.Name);
            }
        });
        CustomerDC customerSearch=new CustomerDC();
        customerSearch.branch=TormundManager.GlobalBranchDC;
        TormundManager.GlobalCustomersList= TormundManager.GetCustomerByBranch(customerSearch);
        Collections.sort(TormundManager.GlobalCustomersList, new Comparator<CustomerDC>() {
            public int compare(CustomerDC obj1, CustomerDC obj2) {
                return obj1.name.compareTo(obj2.name);
            }
        });
    }


    public static Date DateWithOutHours(Date date){
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        c.set(Calendar.HOUR_OF_DAY, 0);
        c.set(Calendar.MINUTE, 0);
        c.set(Calendar.SECOND, 0);
        c.set(Calendar.MILLISECOND, 0);

        return c.getTime();

    }

  public static int RandomNumberBetween(int Low,int High){

      Random r = new Random();
      int Result = r.nextInt(High-Low) + Low;
      return Result;

  }





    public static ArrayList<UserDC> ValidateLogin(UserDC userDC) {

        if(userDC==null) return null;
        ArrayList<UserDC> users=new ArrayList<UserDC>();

        try {
            JSONObject jsonUser=new JSONObject();
            if(userDC.Email!=null && userDC.password!=null){
                if(!userDC.Email.equals("") && !userDC.password.equals("")){

                    jsonUser.put("email",userDC.Email);
                    jsonUser.put("password",userDC.password);
                    jsonUser.put("event_key","validate");

                }

            }

            HttpURLConnection conn= ApiConnection.getConnection("users");
            DataOutputStream os = new DataOutputStream(conn.getOutputStream());
            os.writeBytes(jsonUser.toString());

            os.flush();
            os.close();

            int responseCode = conn.getResponseCode();
            String responseMessage = conn.getResponseMessage();

            if(responseCode == HttpURLConnection.HTTP_OK){
                String responseString = ApiConnection.readStream(conn.getInputStream());

                JSONArray array= new JSONArray(responseString);
                int top =10;
                if(array.length()>=top){
                    top=10;
                }else{
                    top=array.length();
                }
                if(array.length()>0){
                    for (int i=0 ;i<top;i++) {
                        JSONObject json =array.getJSONObject(i);

                        if(json.getString("id")!="0"){

                            UserDC userReturn= new UserDC();
                            userReturn.Id=json.getInt("id");
                            userReturn.Email=json.getString("email");

                            if(!json.isNull("company")){
                                JSONObject jsonCompany=json.getJSONObject("company");
                                CompanyDC company= new CompanyDC();
                                company.Id=jsonCompany.getInt("id");
                                company.Name=jsonCompany.getString("name");
                                userReturn.company=company;
                            }





                            users.add(userReturn);
                            //regresa a la primer usuario encontrado
                            return users;
                        }
                    }


                }else {
                    return new ArrayList<UserDC>();
                }



            }else{
                Log.v("CatalogClient", "Response code:"+ responseCode);
                Log.v("CatalogClient", "Response message:"+ responseMessage);
            }
            Log.i("STATUS", String.valueOf(conn.getResponseCode()));
            Log.i("MSG" , conn.getResponseMessage());

            conn.disconnect();
        } catch (Exception e) {
            e.printStackTrace();
            return new ArrayList<UserDC>();
        }


        return  users;

    }


    public static ArrayList<EmployeeDC> GetEmployee(EmployeeDC employeeDC) {

        if(employeeDC==null) return null;
        ArrayList<EmployeeDC> employees=new ArrayList<EmployeeDC>();

        try {
            JSONObject jsonEmployee=new JSONObject();
             jsonEmployee.put("event_key","search");
            if(employeeDC.user!=null ){

                if(employeeDC.user.Id>0){
                    JSONObject jsonUser= new JSONObject();
                    jsonUser.put("id",employeeDC.user.Id);
                    jsonEmployee.put("user",jsonUser);

                }

            }



            HttpURLConnection conn= ApiConnection.getConnection("employees");
            DataOutputStream os = new DataOutputStream(conn.getOutputStream());
            os.writeBytes(jsonEmployee.toString());

            os.flush();
            os.close();

            int responseCode = conn.getResponseCode();
            String responseMessage = conn.getResponseMessage();

            if(responseCode == HttpURLConnection.HTTP_OK){
                String responseString = ApiConnection.readStream(conn.getInputStream());

                JSONArray array= new JSONArray(responseString);
                int top =10;
                if(array.length()>=top){
                    top=10;
                }else{
                    top=array.length();
                }
                if(array.length()>0){
                    for (int i=0 ;i<top;i++) {
                        JSONObject json =array.getJSONObject(i);

                        if(json.getString("id")!="0"){

                            EmployeeDC employeeReturn= new EmployeeDC();
                            employeeReturn.id=json.getInt("id");
                            employeeReturn.name=json.getString("name");

                            if(!json.isNull("branch")){
                                JSONObject jsonBranch=json.getJSONObject("branch");
                                BranchDC branch=new BranchDC();
                                branch.Id=jsonBranch.getInt("id");
                                branch.BranchName=jsonBranch.getString("branchName");
                                employeeReturn.branch=branch;
                            }

                            if(!json.isNull("user")){
                                JSONObject jsonUser=json.getJSONObject("user");
                                UserDC userDC=new UserDC();
                                userDC.Id=jsonUser.getInt("id");
                                userDC.Email=jsonUser.getString("email");
                                employeeReturn.user=userDC;
                            }



                            if(!json.isNull("repo_files")){
                                employeeReturn.repo_files=new ArrayList<>();
                                JSONArray files=json.getJSONArray("repo_files");
                                for(int k=0;k<files.length() ;k++){
                                    JSONObject jsonObjPicture=files.getJSONObject(k);
                                    VaultFileDC vault= new VaultFileDC();
                                    vault.Id=jsonObjPicture.getInt("id");
                                    //vault.Description=jsonObjPicture.getString("description");
                                    if(!jsonObjPicture.isNull("url")){
                                        vault.Url=jsonObjPicture.getString("url");
                                    }

                                    employeeReturn.repo_files.add(vault);
                                }
                            }





                            employees.add(employeeReturn);
                            //regresa a la primer usuario encontrado
                            return employees;
                        }
                    }


                }else {
                    return new ArrayList<EmployeeDC>();
                }



            }else{
                Log.v("CatalogClient", "Response code:"+ responseCode);
                Log.v("CatalogClient", "Response message:"+ responseMessage);
            }
            Log.i("STATUS", String.valueOf(conn.getResponseCode()));
            Log.i("MSG" , conn.getResponseMessage());

            conn.disconnect();
        } catch (Exception e) {
            e.printStackTrace();
            return new ArrayList<EmployeeDC>();
        }


        return  employees;

    }

    public static ArrayList<EmployeeDC> GetEmployeeByBranch(EmployeeDC employeeDC) {

        if(employeeDC==null) return null;
        ArrayList<EmployeeDC> employees=new ArrayList<EmployeeDC>();

        try {
            JSONObject jsonEmployee=new JSONObject();
            jsonEmployee.put("event_key","search");
            if(employeeDC.user!=null ){

                if(employeeDC.user.Id>0){
                    JSONObject jsonUser= new JSONObject();
                    jsonUser.put("id",employeeDC.user.Id);
                    jsonEmployee.put("user",jsonUser);

                }

            }

            if(employeeDC.branch!=null ){


                JSONObject jsonBranch= new JSONObject();
                jsonBranch.put("id",employeeDC.branch.Id);
                jsonBranch.put("branchname",employeeDC.branch.BranchName);
                jsonEmployee.put("branch",jsonBranch);


            }
            if(employeeDC.type_employee>0){
                jsonEmployee.put("type_employee",employeeDC.type_employee);
            }

            HttpURLConnection conn= ApiConnection.getConnection("employees");
            DataOutputStream os = new DataOutputStream(conn.getOutputStream());
            os.writeBytes(jsonEmployee.toString());

            os.flush();
            os.close();

            int responseCode = conn.getResponseCode();
            String responseMessage = conn.getResponseMessage();

            if(responseCode == HttpURLConnection.HTTP_OK){
                String responseString = ApiConnection.readStream(conn.getInputStream());

                JSONArray array= new JSONArray(responseString);
                int top =10;
                if(array.length()>=top){
                    top=10;
                }else{
                    top=array.length();
                }
                if(array.length()>0){
                    for (int i=0 ;i<top;i++) {
                        JSONObject json =array.getJSONObject(i);

                        if(json.getString("id")!="0"){

                            EmployeeDC employeeReturn= new EmployeeDC();
                            employeeReturn.id=json.getInt("id");
                            employeeReturn.name=json.getString("name");

                            if(!json.isNull("branch")){
                                JSONObject jsonBranch=json.getJSONObject("branch");
                                BranchDC branch=new BranchDC();
                                branch.Id=jsonBranch.getInt("id");
                                branch.BranchName=jsonBranch.getString("branchName");
                                employeeReturn.branch=branch;
                            }


                            if(!json.isNull("repo_files")){
                                employeeReturn.repo_files=new ArrayList<>();
                                JSONArray files=json.getJSONArray("repo_files");
                                for(int k=0;k<files.length() ;k++){
                                    JSONObject jsonObjPicture=files.getJSONObject(k);
                                    VaultFileDC vault= new VaultFileDC();
                                    vault.Id=jsonObjPicture.getInt("id");
                                    //vault.Description=jsonObjPicture.getString("description");
                                    if(!jsonObjPicture.isNull("url")){
                                        vault.Url=jsonObjPicture.getString("url");
                                    }

                                    employeeReturn.repo_files.add(vault);
                                }
                            }





                            employees.add(employeeReturn);

                        }
                    }


                }else {
                    return new ArrayList<EmployeeDC>();
                }



            }else{
                Log.v("CatalogClient", "Response code:"+ responseCode);
                Log.v("CatalogClient", "Response message:"+ responseMessage);
            }
            Log.i("STATUS", String.valueOf(conn.getResponseCode()));
            Log.i("MSG" , conn.getResponseMessage());

            conn.disconnect();
        } catch (Exception e) {
            e.printStackTrace();
            return new ArrayList<EmployeeDC>();
        }


        return  employees;

    }



     public static ArrayList<ServiceDC> GetServicesByBranch(ServiceDC serviceDC) {

        if(serviceDC==null) return null;
        ArrayList<ServiceDC> services=new ArrayList<ServiceDC>();

        try {
            JSONObject jsonService=new JSONObject();
            if(serviceDC.Branch!=null ){
                jsonService.put("event_key","search");
                JSONObject jsonBranch =new JSONObject();
                 jsonBranch.put("id",serviceDC.Branch.Id);
                 jsonBranch.put("branchname",serviceDC.Branch.BranchName);
                 jsonService.put("branch",jsonBranch);


            }


            HttpURLConnection conn= ApiConnection.getConnection("services");
            DataOutputStream os = new DataOutputStream(conn.getOutputStream());
            os.writeBytes(jsonService.toString());

            os.flush();
            os.close();

            int responseCode = conn.getResponseCode();
            String responseMessage = conn.getResponseMessage();

            if(responseCode == HttpURLConnection.HTTP_OK){
                String responseString = ApiConnection.readStream(conn.getInputStream());

                JSONArray array= new JSONArray(responseString);
                int top =10;
                if(array.length()>=top){
                    top=10;
                }else{
                    top=array.length();
                }
                if(array.length()>0){
                    for (int i=0 ;i<top;i++) {
                        JSONObject json =array.getJSONObject(i);

                        if(!json.isNull("service_id")){

                            ServiceDC serviceReturn= new ServiceDC();
                            serviceReturn.service_id=json.getInt("service_id");
                            serviceReturn.Name=json.getString("name");

                            serviceReturn.Price=json.getDouble("price");
                            serviceReturn.Cost=json.getDouble("cost");
                            serviceReturn.servicetime=json.getDouble("servicetime");

                            if(!json.isNull("branch")){
                                JSONObject jsonBranch=json.getJSONObject("branch");
                                BranchDC branch=new BranchDC();
                                branch.Id=jsonBranch.getInt("id");
                                branch.BranchName=jsonBranch.getString("branchName");
                                serviceReturn.Branch=branch;
                            }

                            services.add(serviceReturn);

                        }
                    }


                }else {
                    return new ArrayList<ServiceDC>();
                }



            }else{
                Log.v("CatalogClient", "Response code:"+ responseCode);
                Log.v("CatalogClient", "Response message:"+ responseMessage);
            }
            Log.i("STATUS", String.valueOf(conn.getResponseCode()));
            Log.i("MSG" , conn.getResponseMessage());

            conn.disconnect();
        } catch (Exception e) {
            e.printStackTrace();
            return new ArrayList<ServiceDC>();
        }


        return  services;

    }

     public static ArrayList<CustomerDC> GetCustomerByBranch(CustomerDC customerDC) {

        if(customerDC==null) return null;
        ArrayList<CustomerDC> customers=new ArrayList<CustomerDC>();

        try {
            JSONObject jsonCustomer=new JSONObject();
            if(customerDC.branch!=null ){
                jsonCustomer.put("event_key","search");
                JSONObject jsonBranch =new JSONObject();
                 jsonBranch.put("id",customerDC.branch.Id);
                 jsonBranch.put("branchname",customerDC.branch.BranchName);
                 jsonCustomer.put("branch",jsonBranch);


            }


            HttpURLConnection conn= ApiConnection.getConnection("customers");
            DataOutputStream os = new DataOutputStream(conn.getOutputStream());
            os.writeBytes(jsonCustomer.toString());

            os.flush();
            os.close();

            int responseCode = conn.getResponseCode();
            String responseMessage = conn.getResponseMessage();

            if(responseCode == HttpURLConnection.HTTP_OK){
                String responseString = ApiConnection.readStream(conn.getInputStream());

                JSONArray array= new JSONArray(responseString);
                int top =10;
                top=array.length();

                if(array.length()>0){
                    for (int i=0 ;i<top;i++) {
                        JSONObject json =array.getJSONObject(i);

                        if(!json.isNull("id")){

                            CustomerDC customerReturn= new CustomerDC();
                            customerReturn.id=json.getInt("id");
                            customerReturn.name=json.getString("name");
                          if(!json.isNull("principalPhone")){
                           customerReturn.principalPhone=json.getString("principalPhone");
                          }
                          if(!json.isNull("principal_email")){
                           customerReturn.principal_email=json.getString("principal_email");
                          }
                           if(!json.isNull("cantidadCitas")){
                           customerReturn.cantidadCitas=json.getInt("cantidadCitas");
                          }
                          if(!json.isNull("ultimaCita")){
                           customerReturn.ultimacita=json.getString("ultimaCita");
                          }
                          if(!json.isNull("repo_files")) {
                          JSONArray jsonVaultArray=json.getJSONArray("repo_files");
                                    ArrayList<VaultFileDC> pictures=new ArrayList<VaultFileDC>();
                                    for(int k=0;k<jsonVaultArray.length();k++){
                                        VaultFileDC vault=new VaultFileDC();
                                        JSONObject obj_array =jsonVaultArray.getJSONObject(k);
                                        vault.Id=obj_array.getInt("id");
                                        vault.Name=obj_array.getString("name");
                                        vault.Url=obj_array.getString("url");
                                        pictures.add(vault);
                                    }
                                    if(!pictures.isEmpty()){
                                        customerReturn.repo_files= new ArrayList<VaultFileDC>();
                                       customerReturn.repo_files.addAll(pictures);
                                    }
                          }
                            if(!json.isNull("branch")){
                                JSONObject jsonBranch=json.getJSONObject("branch");
                                BranchDC branch=new BranchDC();
                                branch.Id=jsonBranch.getInt("id");
                                branch.BranchName=jsonBranch.getString("branchName");
                                customerReturn.branch=branch;
                            }

                            customers.add(customerReturn);

                        }
                    }


                }else {
                    return new ArrayList<CustomerDC>();
                }



            }else{
                Log.v("CatalogClient", "Response code:"+ responseCode);
                Log.v("CatalogClient", "Response message:"+ responseMessage);
            }
            Log.i("STATUS", String.valueOf(conn.getResponseCode()));
            Log.i("MSG" , conn.getResponseMessage());

            conn.disconnect();
        } catch (Exception e) {
            e.printStackTrace();
            return new ArrayList<CustomerDC>();
        }


        return  customers;

    }



    public static ArrayList<CustomerDC> SaveCustomerImage(AppointmentDC appointmentDC) {

        if(appointmentDC==null || appointmentDC.Customer==null) return null;
        CustomerDC customerDC= appointmentDC.Customer;
        customerDC.branch=appointmentDC.Branch;

        ArrayList<CustomerDC> customers=new ArrayList<CustomerDC>();

        try {
            JSONObject jsonCustomer=new JSONObject();
            if(customerDC.id>0){
                jsonCustomer.put("id",customerDC.id);
                jsonCustomer.put("event_key","load_picture");

                if(customerDC.photoBytes!=null && customerDC.photoBytes.length>0){

                    jsonCustomer.put("photo_string64",encodeBase64URLSafeString(customerDC.photoBytes));
                }
                if(customerDC.photoName!=null && !customerDC.photoName.equals("")){

                    jsonCustomer.put("photo_name",customerDC.photoName);
                }

                if(customerDC.branch!=null ){
                    JSONObject jsonBranch=new JSONObject();
                    jsonBranch.put("id",customerDC.branch.Id);
                    jsonBranch.put("branchName",customerDC.branch.BranchName);
                    jsonCustomer.put("branch",jsonBranch);

                    if(customerDC.branch.CompanyDC !=null ){
                        JSONObject jsonCompany=new JSONObject();
                        jsonCompany.put("id",customerDC.branch.CompanyDC.Id);
                        jsonCompany.put("name",customerDC.branch.CompanyDC.Name);
                        jsonBranch.put("CompanyDC",jsonCompany);
                    }
                }

            }

            HttpURLConnection conn= ApiConnection.getConnection("customers");
            DataOutputStream os = new DataOutputStream(conn.getOutputStream());
            os.writeBytes(jsonCustomer.toString());

            os.flush();
            os.close();

            int responseCode = conn.getResponseCode();
            String responseMessage = conn.getResponseMessage();

            if(responseCode == HttpURLConnection.HTTP_OK){
                String responseString = ApiConnection.readStream(conn.getInputStream());

                JSONArray array= new JSONArray(responseString);
                int top =10;
                if(array.length()>=top){
                    top=10;
                }else{
                    top=array.length();
                }
                if(array.length()>0){
                    for (int i=0 ;i<top;i++) {
                        JSONObject json =array.getJSONObject(i);

                        if(json.getString("id")!="0"){

                            CustomerDC customerReturn= new CustomerDC();
                            customerReturn.id=json.getInt("id");
                            customerReturn.name=json.getString("name");

                            customers.add(customerReturn);
                            //regresa a la primer usuario encontrado
                            return customers;
                        }
                    }


                }else {
                    return new ArrayList<CustomerDC>();
                }



            }else{
                Log.v("SavePicture", "Response code:"+ responseCode);

            }
            Log.i("STATUS", String.valueOf(conn.getResponseCode()));
            Log.i("MSG" , conn.getResponseMessage());

            conn.disconnect();
        } catch (Exception e) {
            e.printStackTrace();
            return new ArrayList<CustomerDC>();
        }


        return  customers;

    }


    public static void RestartGlobalParameters(){

        TormundManager.GlobalCompanyDC=null;
        TormundManager.GlobalEmployeeDC=null;
        TormundManager.GlobalBranchDC=null;
        TormundManager.GlobalCustomersList=new ArrayList<CustomerDC>();
        TormundManager.GlobalServicesList=new ArrayList<ServiceDC>();
        TormundManager.GlobalEmployeesList=new ArrayList<EmployeeDC>();
    }


    public static String encodeBase64URLSafeString(byte[] binaryData) {

        return android.util.Base64.encodeToString(binaryData, Base64.URL_SAFE);

    }

    public static byte[] decodeBase64StringToBytes(String strBase64) {

        return android.util.Base64.decode(strBase64,Base64.URL_SAFE);

    }

    public static void CreateDirectoryForPictures(){
        String path=Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES)+File.separator+"TormundStylist";
        GlobalPicturePath=path;
        File directory= new File(path);
        if(!directory.exists()){
            directory.mkdirs();
        }
    }

    public static String  getLocalUSNowHour(){
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy_MM_dd_HH_mm_ss", Locale.US);
        Date now = new Date();

        return formatter.format(now);

    }

    public  static boolean isStoragePermissionGranted(Activity activity) {
        if (Build.VERSION.SDK_INT >= 23) {
            if (activity.checkSelfPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    == PackageManager.PERMISSION_GRANTED) {
                Log.v(TAG,"Permission is granted");
                return true;
            } else {

                Log.v(TAG,"Permission is revoked");
                ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
                return false;
            }
        }
        else { //permission is automatically granted on sdk<23 upon installation
            Log.v(TAG,"Permission is granted");
            return true;
        }
    }


/*    public static List<AppointmentDC> SearchDate(AppointmentDC dateDC) {

        if(dateDC==null) return null;
        ArrayList<AppointmentDC> datesList=new ArrayList<AppointmentDC>();

        try {

            JSONObject jsonParam = new JSONObject();
            jsonParam.put("customer_id", dateDC.Customer_id);
            jsonParam.put("event_key","search");
            jsonParam.put("status_list",dateDC.status_list);
            jsonParam.put("sub_status",dateDC.SubStatus);
            jsonParam.put("appointment_date", TormundManager.FormatDateTime(dateDC.AppointmentDate));
            jsonParam.put("due_date",  TormundManager.FormatDateTime(dateDC.DueDate));

            if(dateDC.Employee!=null && dateDC.Employee.id>0){
                JSONObject jsonEmployee = new JSONObject();
                jsonEmployee.put("id", dateDC.Employee.id);
            }

            HttpURLConnection conn= ApiConnection.getConnection("users");
            DataOutputStream os = new DataOutputStream(conn.getOutputStream());
            os.writeBytes(jsonParam.toString());

            os.flush();
            os.close();




            int responseCode = conn.getResponseCode();
            String responseMessage = conn.getResponseMessage();

            if(responseCode == HttpURLConnection.HTTP_OK){
                String responseString = ApiConnection.readStream(conn.getInputStream());

                JSONArray array= new JSONArray(responseString);
                int top =10;
                if(array.length()>=top){
                    top=10;
                }else{
                    top=array.length();
                }
                if(array.length()>0){
                    for (int i=0 ;i<top;i++) {
                        JSONObject json =array.getJSONObject(i);

                        if(json.getString("id")!="0"){
                            DateFormat df = new SimpleDateFormat("EEE MMM dd kk:mm:ss z yyyy", Locale.ENGLISH);
                            AppointmentDC date= new AppointmentDC();
                            date.Id=json.getInt("id");
                            date.Description=json.getString("description");
                            date.AppointmentDate= TormundManager.JsonStringToDate2(json.getString("appointment_date"));
                            date.DueDate= TormundManager.JsonStringToDate2(json.getString("due_date"));

                            date.Employee_id=json.getInt("id");
                            date.FromApp=json.getString("from_app");
                            date.Status=json.getInt("status");
                            date.CreationDate= TormundManager.JsonStringToDate2(json.getString("creation_date"));
                            date.LastModified= TormundManager.JsonStringToDate2(json.getString("lastModified"));

                            date.Customer_id=json.getInt("customer_id");


                            if(!json.isNull("service")){
                                JSONObject jsonService=json.getJSONObject("service");
                                ServiceDC service=new ServiceDC();
                                service.service_id=jsonService.getInt("service_id");
                                service.Name=jsonService.getString("name");
                                service.Cost=jsonService.getDouble("cost");
                                service.Price=jsonService.getDouble("price");
                                service.servicetime=jsonService.getDouble("servicetime");
                                date.Service=service;
                            }
                            if(!json.isNull("employee")){
                                JSONObject jsonBarber=json.getJSONObject("employee");
                                EmployeeDC barber=new EmployeeDC();
                                barber.id=jsonBarber.getInt("id");
                                barber.name=jsonBarber.getString("name");
                                if(!jsonBarber.isNull("vault_file_id")){
                                    JSONObject jsonVault=jsonBarber.getJSONObject("vault_file_id");
                                    VaultFileDC vault=new VaultFileDC();
                                    vault.Id=jsonVault.getInt("id");
                                    vault.Url=jsonVault.getString("url");
                                    barber.vault_file_id=vault;
                                }
                                if(!jsonBarber.isNull("repo_files")){
                                    JSONArray jsonVaultArray=jsonBarber.getJSONArray("repo_files");
                                    ArrayList<VaultFileDC> pictures=new ArrayList<VaultFileDC>();
                                    for(int k=0;i<jsonVaultArray.length();k++){
                                        VaultFileDC vault=new VaultFileDC();
                                        JSONObject obj_array =jsonVaultArray.getJSONObject(k);
                                        vault.Id=obj_array.getInt("id");
                                        vault.Name=obj_array.getString("name");
                                        vault.Url=obj_array.getString("url");
                                        pictures.add(vault);
                                    }
                                }
                                if(!jsonBarber.isNull("barber_round_picture_url")){
                                    barber.barber_round_picture_url=jsonBarber.getString("barber_round_picture_url");

                                }
                                date.Employee=barber;
                            }

                            if(!json.isNull("branch")){
                                JSONObject jsonService=json.getJSONObject("branch");
                                BranchDC branch=new BranchDC();
                                branch.Id=jsonService.getInt("id");
                                branch.BranchName=jsonService.getString("branchName");
                                date.Branch=branch;
                            }




                            datesList.add(date);

                        }
                    }


                }else {
                    return new ArrayList<AppointmentDC>();
                }



            }else{
                Log.v("CatalogClient", "Response code:"+ responseCode);
                Log.v("CatalogClient", "Response message:"+ responseMessage);
            }
            Log.i("STATUS", String.valueOf(conn.getResponseCode()));
            Log.i("MSG" , conn.getResponseMessage());

            conn.disconnect();
        } catch (Exception e) {
            e.printStackTrace();
            return new ArrayList<AppointmentDC>();
        }


        return  datesList;

    }*/



}
