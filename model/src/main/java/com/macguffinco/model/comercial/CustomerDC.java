package com.macguffinco.model.comercial;

import com.macguffinco.model.Solution.UserDC;
import com.macguffinco.model.VaultFiles.VaultFileDC;

import java.util.ArrayList;
import java.util.Date;

public class CustomerDC {

    public int id;
    public String name;
    public BranchDC branch;
    public UserDC user;
    public String event_key;
    public String principalPhone;
    public String principal_email;
    public int user_id;
    public Date birthDate;
    public Date creationDate;
    public String ultimacita;
    public int cantidadCitas;
    public byte[] photoBytes;
    public String photoName;
    public String photo_string64;
    public ArrayList<VaultFileDC> repo_files;
    public ArrayList<CommentDC> comments;

}