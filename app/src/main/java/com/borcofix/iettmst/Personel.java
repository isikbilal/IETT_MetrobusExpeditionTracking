package com.borcofix.iettmst;

/**
 * Created by ABRA-A5-V52 on 25.09.2017.
 */

public class Personel {

    String RegNumber;
    String NameAndSurname ;
    String WorkLine;
    String DepTime;
    String PDKSTime;
    String RowColor;

    //Burada sınıfımızın parametresiz olarak constructor(yapıcı) metodunu oluşturduk çünkü parametreli fonksiyonumuzla birlikte aynı zamanda fonksiyonumuzun parametresiz olarak da kullanılabilmesini istiyorduk.
    //Parametre girildiğinde parametreli olan Personel fonk. alıyor. Parametresiz kullanmak istediğimizde ise constructor(yapıcı) metodumuzu kullanıyor.
    public Personel(){

    }
    public Personel(String regNumber, String nameAndSurname, String workLine, String depTime, String PDKSTime, String rowColor) {
        this.RegNumber = regNumber;
        this.NameAndSurname = nameAndSurname;
        this.WorkLine = workLine;
        this.DepTime = depTime;
        this.PDKSTime = PDKSTime;
        this.RowColor = rowColor;


    }



}
