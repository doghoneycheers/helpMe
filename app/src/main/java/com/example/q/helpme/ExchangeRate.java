package com.example.q.helpme;

import java.util.ArrayList;
import java.util.List;

public class ExchangeRate {
    String currencyPair;
    String currencyFrom;
    String currencyTo;
    float rate;
    float quantity;
    public List<String> currencyBook = [
            "KRWEUR","USDCHF","USDRUB","USDEUR","USDGBP","USDKRW","KRWCHF","USDCNY","USDJPY","KRWJPY","KRWCNY","KRWGBP","KRWRUB"
            ];

    public ExchangeRate(String currencyPair, float rate){
        this.currencyPair = currencyPair;
        this.currencyFrom = currencyPair.substring(0,2);
        this.currencyTo = currencyPair.substring(3,5);
        this.rate = rate;
        setQuantity(this.currencyFrom,this.currencyTo);
    }

    private void setQuantity(String currencyFrom, String currencyTo, float rate) {
        this.quantity = 1.0/rate;
        return;
    }
    public float getQuantity(String currencyPair){
        return this.quantity;
    }
    public String getCurrencyPair(){
        return this.currencyPair;
    }

}
//public class Weather {
//    String from;
//    i;
//    int temprature;
//    int cloudy;
//    String city;
//
//    public void setLat(int lat){ this.lat = lat;}
//    public void setIon(int ion){ this.ion = ion;}
//    public void setTemprature(int t){ this.temprature = t;}
//    public void setCloudy(int cloudy){ this.cloudy = cloudy;}
//    public void setCity(String city){ this.city = city;}
//
//    public int getLat(){ return lat;}
//    public int getIon() { return ion;}
//    public int getTemprature() { return temprature;}
//    public int getCloudy() { return cloudy; }
//    public String getCity() { return city; }
//}
//
//출처: http://bcho.tistory.com/1050 [조대협의 블로그]