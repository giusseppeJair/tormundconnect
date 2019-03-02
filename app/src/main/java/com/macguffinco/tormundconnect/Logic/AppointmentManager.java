package com.macguffinco.tormundconnect.Logic;

import android.util.Log;

import com.macguffinco.tormundconnect.Solution.ApiConnection;
import com.macguffinco.model.Solution.UserDC;
import com.macguffinco.model.VaultFiles.VaultFileDC;
import com.macguffinco.model.appointments.AppointmentDC;
import com.macguffinco.model.comercial.BranchDC;
import com.macguffinco.model.comercial.CommentDC;
import com.macguffinco.model.comercial.CompanyDC;
import com.macguffinco.model.comercial.CustomerDC;
import com.macguffinco.model.comercial.EmployeeDC;
import com.macguffinco.model.comercial.ServiceDC;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.DataOutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class AppointmentManager {

    public static ArrayList<AppointmentDC> appointments(AppointmentDC dateDC,String event_key) {

        if(dateDC==null) return null;
        ArrayList<AppointmentDC> datesList=new ArrayList<AppointmentDC>();

        try {
            JSONObject jsonEmployee=new JSONObject();
            if(dateDC.Employee!=null){
                jsonEmployee.put("id",dateDC.Employee.id);
            }

            JSONObject jsonParam = new JSONObject();
            jsonParam.put("employee", jsonEmployee);
            jsonParam.put("event_key",event_key);
            jsonParam.put("appointment_date",TormundManager.FormatDateTime(dateDC.AppointmentDate));
            jsonParam.put("due_date",TormundManager.FormatDateTime(dateDC.DueDate));
            jsonParam.put("status_list",dateDC.status_list);



            HttpURLConnection conn= ApiConnection.getConnection("dates");
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
                                date.Employee=barber;
                            }

                            if(!json.isNull("branch")){
                                JSONObject jsonBranch=json.getJSONObject("branch");
                                BranchDC branch=new BranchDC();
                                branch.Id=jsonBranch.getInt("id");
                                branch.BranchName=jsonBranch.getString("branchName");
                                date.Branch=branch;
                                if(!jsonBranch.isNull("companyDC")){
                                    JSONObject jsonCompany=jsonBranch.getJSONObject("companyDC");
                                    CompanyDC companyDC=new CompanyDC();
                                    companyDC.Id=jsonCompany.getInt("id");
                                    companyDC.Name=jsonCompany.getString("name");
                                    companyDC.Email=jsonCompany.getString("email");

                                    date.Branch.CompanyDC=companyDC;
                                }
                            }

                            if(!json.isNull("customer")){
                                JSONObject jsonCustomer=json.getJSONObject("customer");
                                CustomerDC customer=new CustomerDC();
                                customer.id=jsonCustomer.getInt("id");
                                customer.name=jsonCustomer.getString("name");
                                customer.creationDate= TormundManager.JsonStringToDate2(jsonCustomer.getString("creationDate"));
                                customer.ultimacita=jsonCustomer.getString("ultimaCita");
                                customer.cantidadCitas=jsonCustomer.getInt("cantidadCitas");

                                if(!jsonCustomer.isNull("branch")){
                                    JSONObject jsonBranch=jsonCustomer.getJSONObject("branch");
                                    BranchDC branch=new BranchDC();
                                    branch.Id=jsonBranch.getInt("id");
                                    branch.BranchName=jsonBranch.getString("branchName");

                                    if(!jsonBranch.isNull("companyDC")){
                                        JSONObject jsonCompany=jsonBranch.getJSONObject("companyDC");
                                        CompanyDC companyDC=new CompanyDC();
                                        companyDC.Id=jsonCompany.getInt("id");
                                        companyDC.Name=jsonCompany.getString("name");
                                        companyDC.Email=jsonCompany.getString("email");

                                        branch.CompanyDC=companyDC;
                                    }
                                    customer.branch=branch;
                                }

                                if(!jsonCustomer.isNull("repo_files")){
                                    JSONArray jsonVaultArray=jsonCustomer.getJSONArray("repo_files");
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

                                if(!jsonCustomer.isNull("comments")){
                                    JSONArray jsonCommentsArray=jsonCustomer.getJSONArray("comments");
                                    ArrayList<CommentDC> comments=new ArrayList<CommentDC>();
                                    for(int k=0;k<jsonCommentsArray.length();k++){
                                        CommentDC commentDC=new CommentDC();
                                        JSONObject obj_array =jsonCommentsArray.getJSONObject(k);
                                        commentDC.id=obj_array.getInt("id");
                                        commentDC.title=obj_array.getString("title");
                                        commentDC.message=obj_array.getString("message");
                                        commentDC.creation_date=TormundManager.JsonStringToDate2(obj_array.getString("creation_date"));
                                        commentDC.comment_type=obj_array.getInt("comment_type");
                                        if(!obj_array.isNull("employee")){
                                            JSONObject jsonEmp=obj_array.getJSONObject("employee");
                                            EmployeeDC employeeDC= new EmployeeDC();
                                            employeeDC.id=jsonEmp.getInt("id");
                                            employeeDC.name=jsonEmp.getString("name");
                                            commentDC.employee=employeeDC;
                                        }

                                        comments.add(commentDC);
                                    }
                                    customer.comments=comments;
                                }



                                if(!jsonCustomer.isNull("user")){

                                    JSONObject jsonUser=jsonCustomer.getJSONObject("user");
                                    UserDC userDC=new UserDC();
                                    userDC.Id=jsonUser.getInt("id");
                                    userDC.Name=jsonUser.getString("name");
                                    userDC.TokenFacebook=jsonUser.getString("token_facebook");
                                    customer.user=userDC;
                                }
                                date.Customer=customer;


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

    }

    public static List<AppointmentDC> AddCustomerPicture(AppointmentDC dateDC,String event_key) {

        if(dateDC==null) return null;
        ArrayList<AppointmentDC> datesList=new ArrayList<AppointmentDC>();

        try {
            JSONObject jsonEmployee=new JSONObject();
            if(dateDC.Employee!=null){
                jsonEmployee.put("id",dateDC.Employee.id);
            }

            JSONObject jsonParam = new JSONObject();
            jsonParam.put("employee", jsonEmployee);
            jsonParam.put("event_key",event_key);
            jsonParam.put("appointment_date",TormundManager.FormatDateTime(dateDC.AppointmentDate));
            jsonParam.put("due_date",TormundManager.FormatDateTime(dateDC.AppointmentDate));



            HttpURLConnection conn= ApiConnection.getConnection("dates");
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

    }

    public static List<AppointmentDC> GetCustomersPictures(AppointmentDC dateDC,String event_key) {

        if(dateDC==null) return null;
        ArrayList<AppointmentDC> datesList=new ArrayList<AppointmentDC>();

        try {
            JSONObject jsonEmployee=new JSONObject();
            if(dateDC.Employee!=null){
                jsonEmployee.put("id",dateDC.Employee.id);
            }

            JSONObject jsonParam = new JSONObject();
            jsonParam.put("employee", jsonEmployee);
            jsonParam.put("event_key",event_key);
            jsonParam.put("appointment_date",TormundManager.FormatDateTime(dateDC.AppointmentDate));
            jsonParam.put("due_date",TormundManager.FormatDateTime(dateDC.AppointmentDate));



            HttpURLConnection conn= ApiConnection.getConnection("dates");
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

    }


    public static ArrayList<AppointmentDC> getEnabledDates(AppointmentDC dateDC) {

        if(dateDC==null) return null;
        ArrayList<AppointmentDC> datesList=new ArrayList<AppointmentDC>();

        try {



            JSONObject jsonParam = new JSONObject();

            jsonParam.put("appointment_date", dateDC.AppointmentDate);
            jsonParam.put("status", dateDC.Status);
            jsonParam.put("event_key","search_enable_hours");



            if(dateDC.Branch!=null){
                JSONObject jsonBranch=new JSONObject();
                jsonBranch.put("id",dateDC.Branch.Id);
                jsonParam.put("branch",jsonBranch);
                if(dateDC.Branch.CompanyDC!=null){
                    JSONObject jsonCompany=new JSONObject();
                    jsonCompany.put("Id",dateDC.Branch.CompanyDC.Id);
                    jsonBranch.put("CompanyDC",jsonCompany);
                }
            }

            if(dateDC.Service!=null){
                JSONObject jsonService=new JSONObject();
                jsonService.put("service_id",dateDC.Service.service_id);
                jsonParam.put("Service",jsonService);
            }

            if(dateDC.Employee!=null){
                JSONObject jsonEmployee=new JSONObject();
                jsonEmployee.put("id",dateDC.Employee.id);
                jsonParam.put("Employee",jsonEmployee);
            }


            HttpURLConnection conn= ApiConnection.getConnection("dates");
            DataOutputStream os = new DataOutputStream(conn.getOutputStream());
            os.writeBytes(jsonParam.toString());

            os.flush();
            os.close();

            int responseCode = conn.getResponseCode();
            String responseMessage = conn.getResponseMessage();

            if(responseCode == HttpURLConnection.HTTP_OK){
                String responseString = ApiConnection.readStream(conn.getInputStream());

                JSONArray array= new JSONArray(responseString);
                if(array.length()>0){
                    for (int i=0 ;i<array.length();i++) {
                        JSONObject json =array.getJSONObject(i);

                        if(!json.isNull("id")){
                            DateFormat df = new SimpleDateFormat("EEE MMM dd kk:mm:ss z yyyy", Locale.ENGLISH);
                            AppointmentDC date= new AppointmentDC();
                            date.Id=json.getInt("id");
                            date.Description=json.getString("description");
                            date.AppointmentDate= TormundManager.JsonStringToDate2(json.getString("appointment_date"));
                            date.DueDate= TormundManager.JsonStringToDate2(json.getString("due_date"));
                            date.Employee_id=json.getInt("id");
                            date.FromApp=json.getString("from_app");
                            date.Status=json.getInt("status");
                            date.SubStatus=json.getInt("sub_status");
                            date.CreationDate= TormundManager.JsonStringToDate2(json.getString("creation_date"));
                            date.LastModified= TormundManager.JsonStringToDate2(json.getString("lastModified"));
                            date.FromApp=json.getString("from_app");
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
                                JSONObject jsonService=json.getJSONObject("employee");
                                EmployeeDC barber=new EmployeeDC();
                                barber.id=jsonService.getInt("id");
                                barber.name=jsonService.getString("name");
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
                    return datesList ;
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
            return datesList ;
        }


        return datesList ;

    }

    public static AppointmentDC CreateNewAppointment(AppointmentDC date) {

        final AppointmentDC dateOut=date;

        try {
            String urlAdress="http://"+TormundManager.GlobalServer+"/api/dates";
            URL url = new URL(urlAdress);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/json;charset=UTF-8");
            conn.setRequestProperty("Accept","application/json");
            conn.setDoOutput(true);
            conn.setDoInput(true);


            JSONObject jsonParam = new JSONObject();
            jsonParam.put("TormundApp", 1);

            jsonParam.put("sub_status", date.SubStatus);
            jsonParam.put("status", 1);
            jsonParam.put("tormund_app",2);//employee Aplication
            jsonParam.put("status_list", date.status_list);
            jsonParam.put("appointment_date", TormundManager.FormatDateTime(date.AppointmentDate));
            jsonParam.put("due_date",  TormundManager.FormatDateTime(date.DueDate));
            jsonParam.put("event_key","new_pending_date");

            if(date.Branch!=null){
                JSONObject jsonBranch=new JSONObject();
                jsonBranch.put("id",date.Branch.Id);
                jsonParam.put("branch",jsonBranch);
            }

            if(date.Service!=null){
                JSONObject jsonService=new JSONObject();
                jsonService.put("service_id",date.Service.service_id);
                jsonParam.put("Service",jsonService);
            }

            if(date.Employee!=null){
                JSONObject jsonEmployee=new JSONObject();
                jsonEmployee.put("id",date.Employee.id);
                jsonParam.put("Employee",jsonEmployee);
            }

            if(date.Customer!=null){
                JSONObject jsonEmployee=new JSONObject();
                jsonEmployee.put("id",date.Customer.id);
                jsonParam.put("Customer",jsonEmployee);
            }



            DataOutputStream os = new DataOutputStream(conn.getOutputStream());
            os.writeBytes(jsonParam.toString());



            os.flush();
            os.close();

            int responseCode = conn.getResponseCode();
            String responseMessage = conn.getResponseMessage();

            if(responseCode == HttpURLConnection.HTTP_OK){
                String responseString = ApiConnection.readStream(conn.getInputStream());

                JSONArray array= new JSONArray(responseString);
                if(array.length()>0){
                    AppointmentDC dateDC= new AppointmentDC();
                    JSONObject json =array.getJSONObject(0);
                    //Status 4: Pending
                    if(!json.isNull("id")){
                        dateDC.Id=json.getInt("id");
                    }
                    if(!json.isNull("customer") ){

                        CustomerDC customerDC= new CustomerDC();
                        BranchDC branchDC= new BranchDC();
                        customerDC.id=json.getJSONObject("customer").getInt("id");
                        customerDC.name=json.getJSONObject("customer").getString("name");
                        dateDC.Customer=customerDC;
                        dateDC.tormund_app=json.getInt("tormund_app");

                        if(!json.isNull("branch")){
                            JSONObject jsonBranch=json.getJSONObject("branch");
                            BranchDC branch=new BranchDC();
                            branch.Id=jsonBranch.getInt("id");
                            branch.BranchName=jsonBranch.getString("branchName");
                            dateDC.Branch=branch;
                        }

                        if(!json.isNull("service")){
                            JSONObject jsonBranch=json.getJSONObject("service");
                            ServiceDC serviceDC=new ServiceDC();
                            serviceDC.service_id=jsonBranch.getInt("id");
                            serviceDC.Cost=jsonBranch.getDouble("cost");
                            serviceDC.Price=jsonBranch.getDouble("price");
                            serviceDC.servicetime=jsonBranch.getDouble("servicetime");

                        }

                        if(!json.isNull("employee")){
                            JSONObject jsonBranch=json.getJSONObject("employee");
                            EmployeeDC employeeDC=new EmployeeDC();
                            employeeDC.id=jsonBranch.getInt("id");
                            employeeDC.name=jsonBranch.getString("name");
                            if(!jsonBranch.isNull("repo_files")){

                            }


                        }

                        if(!json.isNull("branch")){
                            JSONObject jsonBranch=json.getJSONObject("branch");
                            BranchDC branch=new BranchDC();
                            branch.Id=jsonBranch.getInt("id");
                            branch.BranchName=jsonBranch.getString("branchName");
                            dateDC.Branch=branch;
                        }


                        return dateDC;
                    }

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
            return null;
        }

        return null;
    }




}
