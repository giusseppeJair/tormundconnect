package com.macguffinco.model.comercial;

import com.macguffinco.model.Solution.UserDC;
import com.macguffinco.model.VaultFiles.VaultFileDC;

import java.util.ArrayList;
import java.util.Date;

public class EmployeeDC {

    public int id;
    public String name;
    public BranchDC branch;
    public AddressDC Address_id;
    public String event_key;
    public String principal_phone;
    public Date birth_date;
    public Date creation_date;
    public VaultFileDC vault_file_id;
    public CompanyDC CompanyDC;
    public UserDC user;
    public ArrayList<VaultFileDC> repo_files;
    public String barber_round_picture_url;
    public int type_employee;

}