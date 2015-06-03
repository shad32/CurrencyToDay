package com.evoblock.mikhail.currencytoday;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Mihail on 30.03.2015.
 */
public class ParseJsonCurrency {
    private CurrencyData[] currencyData = new CurrencyData[2];
    public ParseJsonCurrency(String input){
        try {
            JSONObject jObj = new JSONObject(input);
            JSONArray jArr = jObj.getJSONObject("query").getJSONObject("results").getJSONArray("rate");
            for(int i = 0;i<jArr.length();i++){
                currencyData[i] = new CurrencyData(jArr.getJSONObject(i).getString("Name").toString(),jArr.getJSONObject(i).getString("Rate").toString());
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
    public CurrencyData[]  getData(){
        return currencyData;
    }
}
