package com.example.q.helpme;

import android.Manifest;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.ContentResolver;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.graphics.Typeface;
import android.net.Uri;
import android.support.v4.app.Fragment;
import android.os.Handler;
import android.os.Message;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.text.DecimalFormat;
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
import android.graphics.Color;
import android.widget.Toast;

import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Temp extends Fragment implements SwipeRefreshLayout.OnRefreshListener {
    public Temp(){}

    private static final int ID_CALCULAR = 0;
    private Dialog dlg;
    private EditText dlgFirst, dlgSecond;
    private TextView dlgCalView, dlgNation;
    private Double globalCurrency;
    private String Nation;

    private static final String TAG = "apitest";
    private SwipeRefreshLayout mSwipeRefreshLayout;
    public static final int LOAD_SUCCESS = 101;
    public ListView listView=null;
    private String REQUEST_URL = "https://api.manana.kr/exchange.json";
    private String REQUEST_METHOD = "GET";

    private TextView textviewJSONText;
    public ArrayList<String> exchangeRateList = new ArrayList<>();

    private SimpleAdapter adapter = null;
    static int counter = 0;





    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.temp, container, false);

        if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.INTERNET) != PackageManager.PERMISSION_GRANTED) {
            return view;
        }

        mSwipeRefreshLayout = view.findViewById(R.id.swipe_layout_exchangerate);
        mSwipeRefreshLayout.setOnRefreshListener(this);


        listView = view.findViewById(R.id.listview_exchangerate);
        getJSON();

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_list_item_1, exchangeRateList)

        {

            @Override
            public View getView(int position, View convertView, ViewGroup parent){
                // Cast the list view each item as text view
                TextView item = (TextView) super.getView(position,convertView,parent);

                // Set the list view item's text color
                item.setTextColor(Color.parseColor("#000000"));
                Typeface customFont = Typeface.createFromAsset(getActivity().getAssets(), "fonts/Chewy.ttf");
                item.setTypeface(customFont);
                // return the view
                return item;
            }

        };

        Log.d(TAG,"ARRAYLIST is :: " + exchangeRateList);

//         Assign adapter to ListView
        Comparator<String> cmpAsc = new Comparator<String>(){

            @Override
            public int compare(String arg1,String arg0){
                return arg1.compareTo(arg0);
            }
        };

        adapter.sort(cmpAsc);
        listView.setAdapter(adapter);

        ListViewExampleClickListener listViewExampleClickListener = new ListViewExampleClickListener();
        listView.setOnItemClickListener(listViewExampleClickListener);

        return view;
    }

    private class ListViewExampleClickListener implements AdapterView.OnItemClickListener {

        @Override
        public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {

            String rawString = listView.getItemAtPosition(position).toString();
            int rawStringLength = rawString.length();

            String cmpCharToValue=":";
            int firstIndexToValue = rawString.indexOf(cmpCharToValue)+2;
            int lastIndexToValue = rawStringLength-5;
            globalCurrency = Double.parseDouble(rawString.substring(firstIndexToValue, lastIndexToValue));

            String cmpCharToUSD=")";
            int lastIndexToUSD=rawString.indexOf(cmpCharToUSD);
            final String USD = rawString.substring(lastIndexToUSD-3, lastIndexToUSD);

            int lastIndexToNation = rawString.length()-1;
//            final String Nation = rawString.substring(0, lastIndexToNation);
            Nation = rawString.substring(lastIndexToNation-3, lastIndexToNation);

            createdDialog(ID_CALCULAR).show(); // Instead of showDialog(0);

        }
    }

    protected Dialog createdDialog(int id) {
        dlg = null;

        switch (id) {
            case ID_CALCULAR:

                Context mContext = getContext();
                dlg = new Dialog(mContext);
                dlg.setContentView(R.layout.dialog_calculator_view);

                dlgFirst = (EditText) dlg.findViewById(R.id.editText1);
                //dlgSecond = (EditText) dlg.findViewById(R.id.editText2);
                dlgCalView = (TextView) dlg.findViewById(R.id.textView3);
                dlgNation = dlg.findViewById(R.id.currency_nation);
                dlgNation.setText(Nation);

                Button okDialogButton = (Button) dlg.findViewById(R.id.btnConvert);
                okDialogButton.setOnClickListener(okDlgCalculator);

                break;
            default:
                break;
        }
        return dlg;
    }

    private Button.OnClickListener okDlgCalculator =
            new Button.OnClickListener() {

                public void onClick(View v) {


                    String getEdit = dlgFirst.getText().toString();
                    if(getEdit.equals("")){
                        Toast.makeText(getContext(), "값을 입력하세요.",Toast.LENGTH_SHORT).show();
                        return;
                    }

                    double calValue = Double.parseDouble(dlgFirst.getText().toString()) * globalCurrency;
                    dlgCalView.setText(String.valueOf(calValue));
                }
            };





    @Override
    public void onRefresh() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                mSwipeRefreshLayout.setRefreshing(false);
                getJSON();
            }
        }, 1000);
    }

    private final MyHandler mHandler = new MyHandler(this);

    private class MyHandler extends Handler {
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
                        ArrayAdapter<String> adapter = new ArrayAdapter<String>(context, android.R.layout.simple_list_item_1, temp.exchangeRateList)

                        {

                            @Override
                            public View getView(int position, View convertView, ViewGroup parent){
                                // Cast the list view each item as text view
                                TextView item = (TextView) super.getView(position,convertView,parent);

                                // Set the list view item's text color
                                item.setTextColor(Color.parseColor("#000000"));

                                Typeface customFont = Typeface.createFromAsset(getActivity().getAssets(), "fonts/Chewy.ttf");
                                item.setTypeface(customFont);

                                // return the view
                               return item;
                            }

                        };
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

                String result = null;

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


                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
                catch (Exception e) {
                    e.printStackTrace();
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

            String p = "(USD)+/[A-Z]*";
//            Pattern pk = Pattern.compile("(USD)+/(KRW)+");

            for( int i=0; i< jsonArray.length();i++){
                JSONObject exchangeInfo = jsonArray.getJSONObject(i);

                String currencyPair = exchangeInfo.getString("name");

                boolean patternMatches = Pattern.matches(p,currencyPair);
                if(patternMatches==true){
                    String currencyFrom = currencyPair.split("/")[0];
                    String currencyTo = currencyPair.split("/")[1];
                    DecimalFormat form = new DecimalFormat("#.###");
                    String price = form.format(Float.parseFloat(exchangeInfo.getString("price")));
                    String date = exchangeInfo.getString("date");

                    if(exchangeInfo.has("en")) {
                        String enName = exchangeInfo.getString("en");
                        String exchangeRate;
                        exchangeRate = enName + " (1 " + currencyFrom + ") : " + price + "(" + currencyTo + ")";
                        exchangeRateList.add(exchangeRate);

                    }


                }
            }

            Log.d(TAG,"ArrayList : " + exchangeRateList);
            return true;
        }
        catch (JSONException e) {
            Log.d(TAG,"JSON Parsing Error : "+e.toString());
        }
        return false;
    }


}
