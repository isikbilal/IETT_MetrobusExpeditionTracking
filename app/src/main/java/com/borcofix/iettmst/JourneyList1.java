package com.borcofix.iettmst;

import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
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
import android.preference.PreferenceManager;
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

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class JourneyList1 extends Fragment {


    PersonelAdapter personelAdapter;
    ListView lvPersonel;
    int SelectedItem;
    Document document;
    TextView tvJ1UpdateDate;
    TextView tvJ1PersonnelCount;


    @Override
    // fragment yapısında olduğu için onCreateView methodunda tanımladık. Normalde onCreate methodunda yapılıyordu.
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View test1 = inflater.inflate(R.layout.journeylist1, container, false);
        //setHasOptionsMenu notificationview fragment yapısında olduğu için
        setHasOptionsMenu(true);

        //nesnelerimizi tanıttık
        lvPersonel = (ListView) test1.findViewById(R.id.lvJ1Personel);

        tvJ1UpdateDate = (TextView) test1.findViewById(R.id.tvJ1UpdateDate);
        tvJ1PersonnelCount = (TextView) test1.findViewById(R.id.tvJ1PersonnelCount);

        //adapterimizi tanımladık ve setledik. Burada fragment yapımız olduğu  için getActivity() verdik, activity yapımız olsaydı getApplicationContext() derdik.
        personelAdapter = new PersonelAdapter(getActivity(), R.layout.journeylist11, G.journeyList1);
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



        return test1;

    }




    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {

        menu.clear();
        getActivity().getMenuInflater().inflate(R.menu.om_journeylist1, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }


    public class PersonelAdapter extends ArrayAdapter<Personel> {

        ArrayList<Personel> pList;

        private PersonelAdapter(Context context, int textViewResourceId,
                               ArrayList<Personel> objects) {

            super(context, textViewResourceId, objects);
            this.pList = objects;
        }


        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            if (convertView == null) {
                LayoutInflater li = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                convertView = li.inflate(R.layout.journeylist11, null);
            }
            //Log.i("TEST1", "Satır:" + Integer.toString(position));

            Personel p = G.journeyList1.get(position);
            Bitmap imgBitmap = getUserPictureFromRawFolder(getActivity(), p.RegNumber);

            ImageView profileImage = (ImageView) convertView.findViewById(R.id.J11profileImage);
            TextView tvRegNumber = (TextView) convertView.findViewById(R.id.tvJ11RegNumber);
            TextView tvNameAndSurname = (TextView) convertView.findViewById(R.id.tvJ11NameAndSurname);
            TextView tvWorkLine = (TextView) convertView.findViewById(R.id.tvJ11WorkLine);
            TextView tvDepTime = (TextView) convertView.findViewById(R.id.tvJ11DepTime);
            TextView tvPDKSTime = (TextView) convertView.findViewById(R.id.tvJ11PDKSTime);
            LinearLayout ll1 = (LinearLayout) convertView.findViewById(R.id.lnJ11Main);

// burada setImage sınıfı sadece nesne alır p. regnumber gibi olmaz
            profileImage.setImageBitmap(imgBitmap);

            tvRegNumber.setText(p.RegNumber);
            tvNameAndSurname.setText(p.NameAndSurname);
            tvWorkLine.setText(p.WorkLine);
            tvDepTime.setText(p.DepTime);
            tvPDKSTime.setText(p.PDKSTime);
            ll1.setBackgroundColor(Color.parseColor(p.RowColor));
  /*
 yukarıdaki satırda satır rengimizi verdik.
 parseColor işlemiyle p.RowColor değerimizde bulunan rengi linearlayoutumuza (ll1) setledik.

eğer direk tablodan gelen rengi değil de kendi istediğimiz bir rengi vermek isteseydik aşağıdaki gibi yapacaktık:
int color = 0;
            // açık yeşil - zamanında PDKS kullanım
            if(sr.Color.contentEquals("#CCFFCC"))
                color = Color.parseColor("#006622");
                // yeşil - zamanı geldiği halde PDKS kullanmadı
            else if(sr.Color.contentEquals("#AAFFAA"))
                color = Color.parseColor("#cccc00");
                // mor - saati yaklaşıyor
            else if(sr.Color.contentEquals("#CCCCFF"))
                color = Color.parseColor("#3d0099");
                // kırmızı - bir önceki kalkışında da PDKS kartını kullanmadı
            else if(sr.Color.contentEquals("#FFCCCC"))
                color = Color.parseColor("#800033");
            else color = Color.BLACK;

            tvMT1State.setBackgroundColor(color);


Burada int tipinde bir renk tanımladıktan sonra parseColor ile içindeki rengi alıp, parseColor ile bizim istediğimiz rengi koyuyoruz.
           */


            return convertView;
        }
    }

    public void ShowPersonelList() {
// Burada aşağıdaki üç adım özetle gerçekleştirilir. liste temizlenir, readResFile ile okunur, Parse ile parçalarnır ve Adaptere bildirim göndererek lisview e yazdırılır
        G.journeyList1.clear();
        //String data = readResFile(getApplicationContext(),"msgt");
        //ParseData(data);
        new ParseURL().execute("http://filo5.iett.gov.tr/mb.php?durak=BDZ");
    }

   /* @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {

        super.onCreateContextMenu(menu, v, menuInfo);
        MenuInflater inflater = getActivity().getMenuInflater();
        inflater.inflate(R.menu.cm_journeylist1, menu);
    }
*/

   /* @Override
    public boolean onContextItemSelected(MenuItem item) {

        switch (item.getItemId()) {

            case R.id.miJ1Add: {

                Personel p1 = new Personel("640637", "Eklenen Nesne", "34BZ", "10:31", "09:13");
                personelList.add(p1);
                personelAdapter.notifyDataSetChanged();
                //Toast.makeText(getApplicationContext(), "OK", Toast.LENGTH_LONG).show();
                return true;
            }
            case R.id.miJ1Delete: {

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

            case R.id.miJ1Refresh: {

                boolean netExist = isNetActive(getActivity());
                if(netExist) {
                    ShowPersonelList();
                    return true;
                }
                else {
                    Toast.makeText(getActivity(), "Lütfen İnternet Bağlantınızı Kontrol Edin", Toast.LENGTH_SHORT).show();
                    return true;
                }

            }

        }
        return true;
    }




    //Raw klasöründen Bitmap dosyasını alır ve nesne olarak geri döndüren method.

   /* public Bitmap getJPEGFile(Context context, String imgDriver) {

        Bitmap bitmap = null;

        Resources res = context.getResources();
        if (res != null) {

            // buradaki "p" eşleşmesi için koyduk. cunku raw klasorundeki dosyalar rakamla baslayamıyordu,
            // sicillerle eşleşmesi için yaptık.
            int rID = res.getIdentifier("p" + imgDriver, "raw", context.getPackageName());
            if (rID != 0) {

                InputStream ins = res.openRawResource(rID);
                if (ins != null) {

                    bitmap = BitmapFactory.decodeStream(ins);
                }
                return bitmap;
            } else {

                return bitmap;
            }
        } else {

            return bitmap;
        }
    }
*/

    // Bu fonk. üstteki getJPEGFile metodundan farkı raw klasöründe ilgili resmi bulamadıgında, imageview in içini temizleyip boş göstermek yerine bizim belirlediğimiz default resmi (no_pic_driver) gösterir.
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

    // buda getJPEGFile gibi fakat dosya okumak için yapılmış bir metod. Bunları kendine bir kütüphane oluşturup daha sonra kullanabilirsin farklı yerlerde

   /* public String readResFile(Context context, String RawFileName) {


        Resources res = context.getResources();
        int rID = res.getIdentifier(RawFileName, "raw", context.getPackageName());

        if (rID != 0) {

            InputStream inputStream = res.openRawResource(rID);

            InputStreamReader isr = null;
            try {

                isr = new InputStreamReader(inputStream, "cp1254");
            } catch (UnsupportedEncodingException e) {

                e.printStackTrace();
            }

            BufferedReader br = new BufferedReader(isr);
            String line;
            StringBuilder text = new StringBuilder();

            try {

                while ((line = br.readLine()) != null) {

                    text.append(line);
                    text.append('\n');
                }
            } catch (IOException e) {

                return null;
            }
            return text.toString();

        } else return null;
    }
    */


        private class ParseURL extends AsyncTask<String, Void, String> {
        // Burada fragment yapımız olduğu  için getActivity() verdik, activity yapımız olsaydı getApplicationContext() derdik.
            ProgressDialog pd = new ProgressDialog(getActivity());


        @Override
        protected void onCancelled() {

            super.onCancelled();
        }


            @Override
            protected void onPreExecute() {

                super.onPreExecute();

                document = null;

                pd.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                pd.setMessage("Lütfen bekleyiniz...");
                pd.setCanceledOnTouchOutside(false);
                pd.setCancelable(false);
                pd.show();
            }

            @Override
            protected String doInBackground(String... strings) {

                try {
                    document = Jsoup.connect(strings[0])
                            .timeout(5000)
                            .followRedirects(true)
                            .get();
                   // Log.i("TEST1",(document.text()));

                } catch (Throwable t) {

                    document = null;
                   // Log.i("TEST1","Veri Alınamadı");
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

                                            G.journeyList1.add(p);

                                        }

                                    }
                                }
                            }
                        }
                    }
                }
                Date updateDate = new Date();
                SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy  HH:mm");

                tvJ1UpdateDate.setText("Son Güncellenme Tarihi: "+df.format(updateDate));
                tvJ1PersonnelCount.setText("Toplam: "+G.journeyList1.size());


                personelAdapter.notifyDataSetChanged();
            }
        };

    }

