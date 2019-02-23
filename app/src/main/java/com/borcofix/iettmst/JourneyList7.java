package com.borcofix.iettmst;

import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import static com.borcofix.iettmst.R.id.tvJ7PersonnelCount;
import static com.borcofix.iettmst.R.id.tvJ7UpdateDate;

public class JourneyList7 extends Fragment {

    PersonelAdapter personelAdapter;
    ListView lvPersonel;
    int SelectedItem;
    Document document;
    TextView tvJ7UpdateDate;
    TextView tvJ7PersonnelCount;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View test7 = inflater.inflate(R.layout.journeylist7, container, false);

        setHasOptionsMenu(true);
        lvPersonel = (ListView) test7.findViewById(R.id.lvJ7Personel);
        tvJ7UpdateDate = (TextView) test7.findViewById(R.id.tvJ7UpdateDate);
        tvJ7PersonnelCount = (TextView) test7.findViewById(R.id.tvJ7PersonnelCount);
        personelAdapter = new PersonelAdapter(getActivity(), R.layout.journeylist71, G.journeyList7);
        lvPersonel.setAdapter(personelAdapter);
        //context menu kayıt
        registerForContextMenu(lvPersonel);

        lvPersonel.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int index, long l) {

                SelectedItem = index;
                getActivity().openContextMenu(lvPersonel);
                //Personel p = personelList.get(index);
                // Toast.makeText(getApplicationContext(), p.RegNumber,Toast.LENGTH_SHORT).show();
            }
        });

        return test7;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {

        menu.clear();
        getActivity().getMenuInflater().inflate(R.menu.om_journeylist7, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    public class PersonelAdapter extends ArrayAdapter<Personel> {

        ArrayList<Personel> pList;

        public PersonelAdapter(Context context, int textViewResourceId,
                               ArrayList<Personel> objects) {

            super(context, textViewResourceId, objects);
            this.pList = objects;
        }


        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            if (convertView == null) {
                LayoutInflater li = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                convertView = li.inflate(R.layout.journeylist71, null);
            }
            //Log.i("TEST1", "Satır:" + Integer.toString(position));

            Personel p = G.journeyList7.get(position);
            Bitmap imgBitmap = getUserPictureFromRawFolder(getActivity(), p.RegNumber);

            ImageView profileImage = (ImageView) convertView.findViewById(R.id.J71profileImage);
            TextView tvRegNumber = (TextView) convertView.findViewById(R.id.tvJ71RegNumber);
            TextView tvNameAndSurname = (TextView) convertView.findViewById(R.id.tvJ71NameAndSurname);
            TextView tvWorkLine = (TextView) convertView.findViewById(R.id.tvJ71WorkLine);
            TextView tvDepTime = (TextView) convertView.findViewById(R.id.tvJ71DepTime);
            TextView tvPDKSTime = (TextView) convertView.findViewById(R.id.tvJ71PDKSTime);
            LinearLayout ll7 = (LinearLayout) convertView.findViewById(R.id.lnJ71Main);

// burada setImage sınıfı sadece nesne alır p. regnumber gibi olmaz
            profileImage.setImageBitmap(imgBitmap);

            tvRegNumber.setText(p.RegNumber);
            tvNameAndSurname.setText(p.NameAndSurname);
            tvWorkLine.setText(p.WorkLine);
            tvDepTime.setText(p.DepTime);
            tvPDKSTime.setText(p.PDKSTime);
            ll7.setBackgroundColor(Color.parseColor(p.RowColor));


            return convertView;
        }
    }

    public void ShowPersonelList() {
// Burada aşağıdaki üç adım özetle gerçekleştirilir. liste temizlenir, readResFile ile okunur, Parse ile parçalarnır ve Adaptere bildirim göndererek lisview e yazdırılır
        G.journeyList7.clear();
        //String data = readResFile(getApplicationContext(),"msgt");
        //ParseData(data);
        new ParseURL().execute("http://filo5.iett.gov.tr/mb.php?durak=SCM");

    }

    /*
    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {

        super.onCreateContextMenu(menu, v, menuInfo);
        MenuInflater inflater = getActivity().getMenuInflater();
        inflater.inflate(R.menu.cm_journeylist7, menu);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {

        switch (item.getItemId()) {

            case R.id.miJ7Add: {

                Personel p1 = new Personel("640637", "Eklenen Nesne", "34BZ", "10:31", "09:13");
                personelList.add(p1);
                personelAdapter.notifyDataSetChanged();
                //Toast.makeText(getApplicationContext(), "OK", Toast.LENGTH_LONG).show();
                return true;
            }
            case R.id.miJ7Delete: {

                personelList.remove(SelectedItem);
                personelAdapter.notifyDataSetChanged();
                //Toast.makeText(getApplicationContext(), "OK", Toast.LENGTH_LONG).show();
                return true;
            }
        }
        return true;
    }
    */
    public boolean isNetActive(Context context) {

        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo ni = cm.getActiveNetworkInfo();

        if (ni != null && ni.isConnected()) {
            return true;
        }
        return false;
    }

    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {

            case R.id.miJ7Refresh: {
                boolean netExist = isNetActive(getActivity());
                if (netExist) {
                    ShowPersonelList();
                    return true;
                } else {
                    Toast.makeText(getActivity(), "Lütfen İnternet Bağlantınızı Kontrol Edin", Toast.LENGTH_SHORT).show();
                    return true;
                }
            }

        }
        return true;
    }

    public static Bitmap getUserPictureFromRawFolder(Context context, String imgDriver) {

        Bitmap bitmap = null;
        // TODO BUG Sürüm: 1.1.4.2016
        Resources res = context.getResources();
        if (res != null) {

            int rID = res.getIdentifier("p" + imgDriver, "raw", context.getPackageName());
            if (rID != 0) {

                InputStream ins = res.openRawResource(rID);
                if (ins == null) {

                    bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.no_pic_driver);
                } else {

                    bitmap = BitmapFactory.decodeStream(ins);
                }
                return bitmap;
            } else {

                bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.no_pic_driver);
                return bitmap;
            }
        } else {

            bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.no_pic_driver);
            return bitmap;
        }
    }

    private class ParseURL extends AsyncTask<String, Void, String> {

        ProgressDialog pd = new ProgressDialog(getActivity());

       /*- @Override
        protected void onCancelled() {

            super.onCancelled();
        }
        */

        @Override
        protected void onPreExecute() {

            super.onPreExecute();

            pd.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            pd.setMessage("Lütfen bekleyiniz...");
            pd.setCanceledOnTouchOutside(false);
            pd.setCancelable(false);
            pd.show();

            document = null;
        }

        @Override
        protected String doInBackground(String... strings) {

            try {
                document = Jsoup.connect(strings[0])
                        .timeout(5000)
                        .followRedirects(true)
                        .get();
                Log.i("TEST1", (document.text()));

            } catch (Throwable t) {

                document = null;
                Log.i("TEST1", "Veri Alınamadı");
            }
            return "";

        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            pd.dismiss();
            ParseURLHandler.sendEmptyMessage(0);
        }

    }

    Handler ParseURLHandler = new Handler() {

        @Override
        public void handleMessage(Message msg) {


            if (document != null) {

                Elements tables = null;
                tables = document.select("table");
                if ((tables != null) && (tables.size() > 0)) {

                    Element table = tables.get(0);
                    if (table != null) {

                        Elements rows = table.select("tr");
                        if (rows != null) {

                            for (int i = 0; i < rows.size(); i++) {

                                Element row = rows.get(i);
                                if (i > 0) {

                                    String trColor = row.attr("bgcolor");

                                    if (row != null) {

                                        Personel p = new Personel();

                                        Elements cols = row.select("td");
                                        if (cols != null) {

                                            Element col1 = cols.get(1);
                                            if (col1 != null) {
                                                String relHref = col1.text();
                                                p.RegNumber = relHref;
                                            } else p.RegNumber = "?";
                                        }
                                        Element col2 = cols.get(2);
                                        if (col2 != null) {
                                            String relHref = col2.text();
                                            p.NameAndSurname = relHref;
                                        } else p.NameAndSurname = "?";

                                        Element col3 = cols.get(3);
                                        if (col3 != null) {
                                            String relHref = col3.text();
                                            p.WorkLine = relHref;
                                        } else p.WorkLine = "?";

                                        Element col4 = cols.get(4);
                                        if (col4 != null) {
                                            String relHref = col4.text();
                                            p.DepTime = relHref;
                                        } else p.DepTime = "?";

                                        Element col5 = cols.get(5);
                                        if (col5 != null) {
                                            String relHref = col5.text();
                                            p.PDKSTime = relHref;
                                        } else p.PDKSTime = "?";

                                        p.RowColor = trColor;

                                        G.journeyList7.add(p);

                                    }

                                }
                            }
                        }
                    }
                }
            }
            Date updateDate = new Date();
            SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy  HH:mm");

            tvJ7UpdateDate.setText("Son Güncellenme Tarihi: "+df.format(updateDate));
            tvJ7PersonnelCount.setText("Toplam: "+G.journeyList7.size());

            personelAdapter.notifyDataSetChanged();
        }
    };
}




