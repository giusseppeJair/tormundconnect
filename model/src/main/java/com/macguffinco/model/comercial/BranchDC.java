package com.macguffinco.model.comercial;

import java.util.Date;
import com.macguffinco.model.VaultFiles.VaultFileDC;
public class BranchDC {

    public BranchDC(String Name, String Email, int foto1,String phone,AddressDC address) {
        this.BranchName = Name;
        this.Email = Email;
        this.foto1 = foto1;
        this.Phone=phone;
        this.Address_Id= address;

    }

    public BranchDC() {

    }

    public int Id ;

    public String BranchName ;


    public String Email ;
    public String Phone ;

    public Date CreationDate ;

    public byte[] Photo ;

    public VaultFileDC VaultFileDC;

    public CompanyDC CompanyDC;

    public AddressDC Address_Id;

    public int foto1;

    public String Horary;
}

