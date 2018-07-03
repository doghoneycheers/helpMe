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
import android.widget.Button;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.ref.WeakReference;
import java.net.HttpURLConnection;
import java.net.URL;

public class Temp extends Fragment {
    public Temp(){}
    private static final String TAG = "apitest";
    public static final int LOAD_SUCCESS = 101;

    private String REQUEST_URL = "https://api.manana.kr/exchange.json";
    private String REQUEST_METHOD = "GET";

    //private DialogFragment progressDialog = new DialogFragment();
    private TextView textviewJSONText;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.temp, container, false);

        if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.INTERNET) != PackageManager.PERMISSION_GRANTED) {
            return view;
        }

        Button buttonRequestJSON = view.findViewById(R.id.button_main_requestjson);
        textviewJSONText = view.findViewById(R.id.textview_main_jsontext);
        textviewJSONText.setMovementMethod(new ScrollingMovementMethod());

        buttonRequestJSON.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //progressDialog.show();
                getJSON();
            }
        });

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

            if (temp != null) {
                switch (msg.what) {

                    case LOAD_SUCCESS:
                        //fragment.progressDialog.dismiss();

                        String jsonString = (String)msg.obj;

                        temp.textviewJSONText.setText(jsonString);
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

                    Log.d(TAG, REQUEST_URL);
                    URL url = new URL(REQUEST_URL);
                    HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();

                    httpURLConnection.setReadTimeout(3000);
                    httpURLConnection.setConnectTimeout(3000);
//                    httpURLConnection.setDoOutput(true);
//                    httpURLConnection.setDoInput(true);
                    httpURLConnection.setRequestMethod(REQUEST_METHOD);
                    httpURLConnection.setUseCaches(false);
                    httpURLConnection.connect();

                    int responseStatusCode = httpURLConnection.getResponseCode();

                    InputStream inputStream;
                    if (responseStatusCode == HttpURLConnection.HTTP_OK) {

                        inputStream = httpURLConnection.getInputStream();
                    } else {
                        inputStream = httpURLConnection.getErrorStream();

                    }


                    InputStreamReader inputStreamReader = new InputStreamReader(inputStream, "UTF-8");
                    BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

                    StringBuilder sb = new StringBuilder();
                    String line;


                    while ((line = bufferedReader.readLine()) != null) {
                        sb.append(line);
                    }

                    bufferedReader.close();
                    httpURLConnection.disconnect();

                    result = sb.toString().trim();


                } catch (Exception e) {
                    result = e.toString();
                }


                Message message = mHandler.obtainMessage(LOAD_SUCCESS, result);
                mHandler.sendMessage(message);
            }

        });
        thread.start();
    }

}