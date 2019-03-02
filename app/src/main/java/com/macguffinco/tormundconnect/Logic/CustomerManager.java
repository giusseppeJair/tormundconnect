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
import java.util.Locale;

public final class CustomerManager {

    public static CustomerDC AddCustomerComment(CustomerDC customerDC) {

        if(customerDC==null) return null;
        CustomerDC customerReturn=new CustomerDC();

        if(customerDC.comments==null && customerDC.comments.isEmpty()) return null;
        try {
            JSONObject jsonCustomer=new JSONObject();
            jsonCustomer.put("id",customerDC.id);
            jsonCustomer.put("event_key","add_comment");

            JSONArray arrayComments = new JSONArray();
            for (CommentDC item:customerDC.comments ) {
                JSONObject jsonComment=new JSONObject();
                jsonComment.put("id",item.id);
                jsonComment.put("message",item.message);
                jsonComment.put("title",item.title);

                if(item.employee!=null){
                    JSONObject jsonEmp= new JSONObject();
                    jsonEmp.put("id",item.employee.id);
                    jsonComment.put("employee",jsonEmp);
                }
                if(item.customer!=null){
                    JSONObject jsonCustom= new JSONObject();
                    jsonCustom.put("id",item.customer.id);
                    jsonComment.put("customer",jsonCustom);
                }
                arrayComments.put(jsonComment);
            }
            jsonCustomer.put("comments",arrayComments);

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

                        if(!json.isNull("id")){

                            customerReturn.id=json.getInt("id");

                            if(!json.isNull("comments")){
                                JSONArray jsonCommentsArray=json.getJSONArray("comments");
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
                                customerReturn.comments=new ArrayList<>();
                                customerReturn.comments=comments;
                            }
                        }
                    }

                    return customerReturn;
                }else {
                   return null;
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


        return  null;
    }
}
