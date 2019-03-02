package com.macguffinco.model.comercial;

public class AddressDC {



    public AddressDC(int Id,String street,String number,int addressType,boolean isEnabled,String googleLocation) {
        this.Address_Id=Id;
        this.Street=street==null?"":street;
        this.Number=number==null?"":number;
        this.AddressType=addressType;
        this.IsEnabled=isEnabled;
        this.GoogleLocation=googleLocation==null?"":googleLocation;;

    }

    public AddressDC() {



    }

    public int Address_Id ;

    public String Street ;



    public String Number ;


    public int AddressType ;

    public boolean IsEnabled ;

    public String GoogleLocation ;




    public String toString(){
        return Street + " " + Number;
    }
}

