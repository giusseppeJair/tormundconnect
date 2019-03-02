package com.macguffinco.tormundconnect.Logic;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.util.Log;

import com.macguffinco.tormundconnect.Solution.ApiConnection;
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
import java.io.FileOutputStream;
import java.net.HttpURLConnection;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import static com.macguffinco.tormundconnect.Logic.TormundManager.decodeBase64StringToBytes;

public final class PictureManager {

    public static ArrayList<String> GetCustomerImagesFromStorage(AppointmentDC appointmentDC){

        ArrayList<String> customerPictures= new ArrayList<>();
        String nomenclatureCustomerFile=TormundManager.getCustomerFileName(appointmentDC)+"_";
        if(nomenclatureCustomerFile!=null && nomenclatureCustomerFile!=""){

            File directory=new File(TormundManager.GlobalPicturePath);
            if(directory.exists() && directory.listFiles()!=null){
                for (File file:directory.listFiles() ) {

                    String name=file.getName();

                    if(name.contains(nomenclatureCustomerFile)){
                        customerPictures.add(TormundManager.GlobalPicturePath+File.separator+name);
                    }
                }
            }


            if(customerPictures.isEmpty()){
                customerPictures.add(TormundManager.ImageDefaultUrl);
                customerPictures.add(TormundManager.ImageDefaultUrl);
                customerPictures.add(TormundManager.ImageDefaultUrl);
            }
            if(!customerPictures.isEmpty()){
                if(customerPictures.size()==1){
                    customerPictures.add(TormundManager.ImageDefaultUrl);
                    customerPictures.add(TormundManager.ImageDefaultUrl);
                }else if(customerPictures.size()==2){
                    customerPictures.add(TormundManager.ImageDefaultUrl);

                }
            }


         //   Bitmap bitmap1 = BitmapFactory.decodeFile(photoPath);


        }

       return customerPictures;
    }


    public static ArrayList<CustomerDC> DownloadCustomerPicturesToPhone(AppointmentDC appointmentDC){


        if(appointmentDC==null) return null;
        ArrayList<CustomerDC> fileList=new ArrayList<CustomerDC>();
        CustomerDC customer=appointmentDC.Customer;

        try {
            JSONObject jsonCustomer=new JSONObject();
           jsonCustomer.put("id",customer.id);
           jsonCustomer.put("event_key","download_picture");

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

                            CustomerDC customerDc=new CustomerDC();
                            customerDc.id=json.getInt("id");
                            if(!json.isNull("photo_string64")){
                                customerDc.photo_string64=json.getString("photo_string64");
                                if(customerDc.photo_string64==null && customerDc.photo_string64.length()<=0){

                                    continue;
                                }
                                customer.photoBytes=decodeBase64StringToBytes(customerDc.photo_string64);
                                customerDc.photoName=json.getString("photo_name");
                            }
                            if(!json.isNull("branch")){

                                JSONObject jsonBranch= json.getJSONObject("branch");
                                BranchDC branch= new BranchDC();
                                branch.Id=jsonBranch.getInt("id");
                                branch.BranchName=jsonBranch.getString("branchName");

                                if(!jsonBranch.isNull("companyDC")){

                                    JSONObject jsonCompany= jsonBranch.getJSONObject("companyDC");
                                    CompanyDC companyDC= new CompanyDC();
                                    companyDC.Id=jsonCompany.getInt("id");
                                    companyDC.Name=jsonCompany.getString("name");
                                    branch.CompanyDC=companyDC;

                                }

                                customerDc.branch=branch;

                            }

                            File file= new File(TormundManager.GlobalPicturePath+ File.separator+customerDc.photoName);
                            if(!file.exists() && customerDc.photoBytes.length>0){
                                FileOutputStream output = null;
                                output = new FileOutputStream(file);
                                output.write(customerDc.photoBytes);
                            }

                            fileList.add(customerDc);


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

        return  fileList;
    }

}
