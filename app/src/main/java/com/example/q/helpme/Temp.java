package com.example.q.helpme;

import android.Manifest;
import android.content.pm.PackageManager;
import android.support.v4.app.Fragment;
import android.os.Handler;
import android.os.Message;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.ref.WeakReference;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import java.net.URL;
import android.widget.SimpleAdapter;
import java.util.HashMap;
import java.util.TimerTask;
import java.util.Timer;
import android.content.Context;

public class Temp extends Fragment {
    public Temp(){}

    private static final String TAG = "apitest";
    public static final int LOAD_SUCCESS = 101;
    public ListView listView=null;
    private String REQUEST_URL = "https://api.manana.kr/exchange.json";
    private String REQUEST_METHOD = "GET";

    //private DialogFragment progressDialog = new DialogFragment();
    private TextView textviewJSONText;
//    public ArrayList<ExchangeRate> exchangeRateList = new ArrayList<ExchangeRate>();
    public ArrayList<String> exchangeRateList = new ArrayList<>();

//    private List<HashMap<String,String>> exchangeRateList = null;
    private SimpleAdapter adapter = null;
    static int counter = 0;





    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.temp, container, false);

        if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.INTERNET) != PackageManager.PERMISSION_GRANTED) {
            return view;
        }

//        exchangeRateList = new ArrayList<HashMap<String,String>>();

        listView = view.findViewById(R.id.listview_exchangerate);

//        TimerTask tt = new TimerTask(){
//            @Override
//            public void run(){
//
//
//                Log.e("Task No. " , String.valueOf(counter) );
//                counter++;
//            }
//        };
//
//        Timer timer = new Timer();
//        timer.schedule(tt,0,5000);


        Button buttonRequestJSON = view.findViewById(R.id.button_main_requestjson);
        textviewJSONText = view.findViewById(R.id.load_sucess_textview);
        textviewJSONText.setMovementMethod(new ScrollingMovementMethod());

        buttonRequestJSON.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //progressDialog.show();
                getJSON();
                Log.d(TAG,"JSON FINISHED");

            }
        });

        //set adapter here..
//        ArrayAdapter<ExchangeRate> adapter = new ArrayAdapter<>(getContext(),
//                android.R.layout.simple_list_item_1,
//                exchangeRateList);


        ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(),
                android.R.layout.simple_list_item_1,
                exchangeRateList);
        Log.d(TAG,"ARRAYLIST is :: " + exchangeRateList);

//         Assign adapter to ListView
        adapter.sort(new Comparator<String>(){

            @Override
            public int compare(String arg1,String arg0){
                return arg1.compareTo(arg0);
            }
        });
        listView.setAdapter(adapter);
        getJSON();


//
//        getJSON();
//        String[] from = new String[]{"Name", "Price","Date"};
//        int[] to = new int[] {R.id.textview_exchange_listviewdata1, R.id.textview_exchange_listviewdata2,
//                R.id.textview_exchange_listviewdata3};
//        adapter = new SimpleAdapter(getContext(), exchangeRateList, R.layout.listview_items, from, to);
//        listView.setAdapter(adapter);
//        Log.d(TAG,"Adapter Set");



        return view;
    }

    private final MyHandler mHandler = new MyHandler(this);

    private static class MyHandler extends Handler {
        private final WeakReference<Temp> weakReference;

        public MyHandler(Temp temp) {
            weakReference = new WeakReference<>(temp);
        }

        @Override
        public void handleMessage(Message msg) {

            Temp temp = weakReference.get();
            Context context = helpMe.getAppContext();
            if (temp != null) {
                switch (msg.what) {

                    case LOAD_SUCCESS:
                        //fragment.progressDialog.dismiss();

                        String jsonString = (String)msg.obj;

                        ArrayAdapter<String> adapter = new ArrayAdapter<>(context,
                                android.R.layout.simple_list_item_1,
                                temp.exchangeRateList);
                        Log.d(TAG,"ARRAYLIST is :: " + temp.exchangeRateList);

//         Assign adapter to ListView
                        adapter.sort(new Comparator<String>(){

                            @Override
                            public int compare(String arg1,String arg0){
                                return arg1.compareTo(arg0);
                            }
                        });
                        temp.listView.setAdapter(adapter);

//                        temp.textviewJSONText.setText(jsonString);
                        break;
                }
            }
        }
    }

    public void  getJSON() {

        Thread thread = new Thread(new Runnable() {
            public void run() {

                String result;

                try {

                    TrustManager[] trustAllCerts = new TrustManager[]{new X509TrustManager() {
                        @Override
                        public void checkClientTrusted(X509Certificate[] x509Certificates, String s) throws CertificateException {

                        }

                        @Override
                        public void checkServerTrusted(X509Certificate[] x509Certificates, String s) throws CertificateException {

                        }

                        @Override
                        public X509Certificate[] getAcceptedIssuers() {
                            return new X509Certificate[0];
                        }
                    }};
                    SSLContext sc = SSLContext.getInstance("SSL");
                    sc.init(null,trustAllCerts,new java.security.SecureRandom());
                    HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());

                    Log.d(TAG, REQUEST_URL);
                    URL url = new URL(REQUEST_URL);
                    HttpsURLConnection HttpsURLConnection = (HttpsURLConnection) url.openConnection();

                    HttpsURLConnection.setReadTimeout(3000);
                    HttpsURLConnection.setConnectTimeout(3000);
//                    HttpsURLConnection.setDoOutput(true);
//                    HttpsURLConnection.setDoInput(true);
                    HttpsURLConnection.setRequestMethod(REQUEST_METHOD);
                    HttpsURLConnection.setUseCaches(false);
                    HttpsURLConnection.connect();

                    int responseStatusCode = HttpsURLConnection.getResponseCode();

                    InputStream inputStream;
                    if (responseStatusCode == HttpsURLConnection.HTTP_OK) {

                        inputStream = HttpsURLConnection.getInputStream();
                    } else {
                        inputStream = HttpsURLConnection.getErrorStream();
                        Log.d(TAG,"Response Status Code : " + responseStatusCode);
                    }


                    InputStreamReader inputStreamReader = new InputStreamReader(inputStream, "UTF-8");
                    BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

                    StringBuilder sb = new StringBuilder();
                    String line;


                    while ((line = bufferedReader.readLine()) != null) {
                        sb.append(line);
                    }

                    bufferedReader.close();
                    inputStreamReader.close();
                    HttpsURLConnection.disconnect();

                    result = sb.toString().trim();
//                    Log.d(TAG,result);


                } catch (Exception e) {
                    result = e.toString();
                }

                if(jsonParser(result)) {
                    Message message = mHandler.obtainMessage(LOAD_SUCCESS, result);
                    mHandler.sendMessage(message);
                }
            }

        });
        thread.start();
    }

    public boolean jsonParser(String jsonString){
        if(jsonString == null) return false;
//        exchangeRateList = new ArrayList<ExchangeRate>();
//        onCreate에서 미리 선언해야함.
        try {
            JSONArray jsonArray = new JSONArray(jsonString);

            exchangeRateList.clear();

            for( int i=0; i< jsonArray.length();i++){
                JSONObject exchangeInfo = jsonArray.getJSONObject(i);

                String currencyPair = exchangeInfo.getString("name");
                String price = exchangeInfo.getString("price");
                String date = exchangeInfo.getString("date");
                String kr,exchangeRate;
//                if(exchangeInfo.getString("kr")!=null)
//                {
//                    kr = exchangeInfo.getString("kr");
//                    exchangeRate = currencyPair + "//"+ kr + " // " + price + " // " + date;
//                }

//                ExchangeRate exchangeRate = new ExchangeRate(currencyPair);
//                exchangeRate.setPrice(Float.parseFloat(price));
//                exchangeRate.setDate(date);
                exchangeRate = currencyPair + " // " + price + " // " + date;
                exchangeRateList.add(exchangeRate);

//                HashMap<String,String> exRateInfoMap = new HashMap<String,String>();
//                exRateInfoMap.put("Name",currencyPair);
//                exRateInfoMap.put("Price",price);
//                exRateInfoMap.put("Date",date);

//                exchangeRateList.add(exRateInfoMap);



            }

            Log.d(TAG,"ArrayList : " + exchangeRateList);
            return true;
        }
        catch (JSONException e) {
            Log.d(TAG,e.toString());
        }
        return false;
    }


}