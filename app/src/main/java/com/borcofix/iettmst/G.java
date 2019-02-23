package com.borcofix.iettmst;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import java.util.ArrayList;

/**
 * Created by ABRA-A5-V52 on 25.10.2017.
 */

public class G {

   public static final ArrayList<Personel> journeyList1 = new ArrayList<Personel>();
   public static final ArrayList<Personel> journeyList2 = new ArrayList<Personel>();
   public static final ArrayList<Personel> journeyList3 = new ArrayList<Personel>();
   public static final ArrayList<Personel> journeyList4 = new ArrayList<Personel>();
   public static final ArrayList<Personel> journeyList5 = new ArrayList<Personel>();
   public static final ArrayList<Personel> journeyList6 = new ArrayList<Personel>();
   public static final ArrayList<Personel> journeyList7 = new ArrayList<Personel>();

   public static int CURRENT_FRAGMENT = -1;
   public static int DEFAULT_FRAGMENT = R.id.iBeylikduzu34BZ;

   private static int getIntData(Context context, String Key, int defValue) {

      SharedPreferences sPref = PreferenceManager.getDefaultSharedPreferences(context);

      return sPref.getInt(Key, defValue);
   }

   private static void setIntData(Context context, String Key, int Value) {

      SharedPreferences sPref = PreferenceManager.getDefaultSharedPreferences(context);

      SharedPreferences.Editor edt = sPref.edit();
      edt.putInt(Key, Value);
      edt.commit();
   }

   public static int getFragmentID(Context context) {

      return getIntData(context, "FragmentID", -1);
   }

   public static void setFragmentID(Context context, int FragmentID) {

      setIntData(context, "FragmentID", FragmentID);
   }
}
