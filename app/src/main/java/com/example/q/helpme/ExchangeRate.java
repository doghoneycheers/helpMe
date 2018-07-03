package com.example.q.helpme;

import java.util.ArrayList;
import java.util.List;

public class ExchangeRate {
    String currencyPair;
    String currencyFrom;
    String currencyTo;
    float price;

    public String[] currencyBook = {"KRWEUR","USDCHF","USDRUB","USDEUR","USDGBP","USDKRW","KRWCHF","USDCNY","USDJPY","KRWJPY","KRWCNY","KRWGBP","KRWRUB"};

    public ExchangeRate(){}

    public ExchangeRate(String currencyPair) {
        this.currencyPair = currencyPair;
//        this.currencyFrom = currencyPair.substring(0, 2);
//        this.currencyTo = currencyPair.substring(3, 5);
//        this.rate = rate;
//        setQuantity(this.currencyFrom, this.currencyTo);
    }

    private void setPrice(String currencyFrom, String currencyTo, float price) {
        this.price = price;
        return;
    }
    public float getPrice(String currencyPair){
        return this.price;
    }
    public String getCurrencyPair(){
        return this.currencyPair;
    }

}