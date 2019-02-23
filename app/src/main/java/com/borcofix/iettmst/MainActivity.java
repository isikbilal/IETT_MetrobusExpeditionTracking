package com.borcofix.iettmst;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        /*FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        */

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

       int value = G.getFragmentID(getApplicationContext());
        if(value == -1){

            G.CURRENT_FRAGMENT = G.DEFAULT_FRAGMENT;
        }else G.CURRENT_FRAGMENT = value;

        G.setFragmentID(getApplicationContext(),G.CURRENT_FRAGMENT);

        showFragment(G.CURRENT_FRAGMENT);

    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            G.setFragmentID(getApplicationContext(),G.CURRENT_FRAGMENT);

           // Toast.makeText(getApplicationContext(), "Başarıyla çıkış yaptınız", Toast.LENGTH_SHORT).show();
            super.onBackPressed();
        }
    }

    public void showFragment(int menuItem){
        G.CURRENT_FRAGMENT = menuItem;
        G.setFragmentID(getApplicationContext(),G.CURRENT_FRAGMENT);

        if (menuItem == R.id.iBeylikduzu34BZ) {
            // Handle the camera action
            FragmentManager fragmentManager = getFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            JourneyList1 j1 = new JourneyList1();
            fragmentTransaction.replace(R.id.fl_content, j1, "j1");
            fragmentTransaction.commit();


        } else if (menuItem == R.id.iBeylikduzu34C) {

            FragmentManager fragmentManager = getFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            JourneyList2 j2 = new JourneyList2();
            fragmentTransaction.replace(R.id.fl_content, j2, "j2");
            fragmentTransaction.commit();

        } else if (menuItem == R.id.iAvcilarSogutlucesme) {

            FragmentManager fragmentManager = getFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            JourneyList3 j3 = new JourneyList3();
            fragmentTransaction.replace(R.id.fl_content, j3, "j3");
            fragmentTransaction.commit();

        } else if (menuItem == R.id.iCevizlibagBeylikduzu) {

            FragmentManager fragmentManager = getFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            JourneyList4 j4 = new JourneyList4();
            fragmentTransaction.replace(R.id.fl_content, j4, "j4");
            fragmentTransaction.commit();

        } else if (menuItem == R.id.iZincirlikuyuSogutlucesme) {

            FragmentManager fragmentManager = getFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            JourneyList5 j5 = new JourneyList5();
            fragmentTransaction.replace(R.id.fl_content, j5, "j5");
            fragmentTransaction.commit();

        } else if (menuItem == R.id.iZincirlikuyuBeylikduzu) {

            FragmentManager fragmentManager = getFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            JourneyList6 j6 = new JourneyList6();
            fragmentTransaction.replace(R.id.fl_content, j6, "j6");
            fragmentTransaction.commit();

        } else if (menuItem == R.id.iSogutlucesme) {

            FragmentManager fragmentManager = getFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            JourneyList7 j7 = new JourneyList7();
            fragmentTransaction.replace(R.id.fl_content, j7, "j7");
            fragmentTransaction.commit();

        } else if (menuItem == R.id.iInfo){

            FragmentManager fragmentManager = getFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            Info info = new Info();
            fragmentTransaction.replace(R.id.fl_content, info, "Info");
            fragmentTransaction.commit();
        }
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
    //Burada üzerine tıkladığımız fragmentin değerinin MenuItem değeri olması için id değişkenini MenuItem nesnesinin item parametresine aktardık.
        int id = item.getItemId();

        showFragment(id);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
