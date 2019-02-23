package com.borcofix.iettmst;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.app.Fragment;

public class Info extends Fragment {

    TextView tvInfoName;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View test1 = inflater.inflate(R.layout.info, container, false);
        //setHasOptionsMenu notificationview fragment yapısında olduğu için
        setHasOptionsMenu(true);


        tvInfoName = (TextView) test1.findViewById(R.id.tvInfoName);


        return test1;

    }
}
