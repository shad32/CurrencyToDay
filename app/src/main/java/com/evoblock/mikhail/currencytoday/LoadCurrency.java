package com.evoblock.mikhail.currencytoday;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * Created by Mihail on 30.03.2015.
 */
public class LoadCurrency extends AsyncTask<String, String, String> {
    private ProgressDialog dialog;
    Activity mActivity;

    public LoadCurrency(MainActivity activity){
        mActivity = activity;
        dialog = new ProgressDialog((activity));
    }

    @Override
    protected void onPreExecute() {
        dialog.setMessage("Update...");
        dialog.show();
    }

    @Override
    protected void onPostExecute(String result) {

        ParseJsonCurrency parseJsonCurrency = new ParseJsonCurrency(result);
        CurrencyData currencyData[] = parseJsonCurrency.getData();
        Animation animTo = AnimationUtils.loadAnimation(mActivity, R.anim.transform_to);

        TextView curUSD =(TextView) mActivity.findViewById(R.id.Cur0);
        TextView curEUR =(TextView) mActivity.findViewById(R.id.Cur1);
        TextView nameCurUSD =(TextView) mActivity.findViewById(R.id.currencyFirst);
        TextView nameCurEUR =(TextView) mActivity.findViewById(R.id.currencySecond);
        curUSD.setAlpha(1.0f);
        curEUR.setAlpha(1.0f);
        curUSD.setText(currencyData[0].getRateCurrency());
        curUSD.startAnimation(animTo);

        curEUR.setText(currencyData[1].getRateCurrency());
        curEUR.startAnimation(animTo);

        nameCurUSD.setText(currencyData[0].getNameCurrency());
        nameCurEUR.setText(currencyData[1].getNameCurrency());

        SharedPreferences sharedPreferences;
        sharedPreferences = mActivity.getPreferences(Context.MODE_PRIVATE);
        SharedPreferences.Editor ed = sharedPreferences.edit();
        ed.putString("data_usd",currencyData[0].getRateCurrency());
        ed.putString("data_eur",currencyData[1].getRateCurrency());
        ed.putString("data_usd_name",currencyData[0].getNameCurrency());
        ed.putString("data_eur_name",currencyData[1].getNameCurrency());
        ed.commit();

        //System.out.println("==========" + result);
        if (dialog.isShowing()) {
            dialog.dismiss();
        }

    }
    @Override
    protected String doInBackground(String... params) {
        String read = null;
        System.out.println(params[0]);
        String req = "http://query.yahooapis.com/v1/public/yql?q=select+*+from+yahoo.finance.xchange+where+pair+=+'"+params[0]+"'&format=json&env=store%3A%2F%2Fdatatables.org%2Falltableswithkeys&callback=";
        System.out.println(req);
        try {
            HttpGet httpRequest = new HttpGet( req);
            HttpClient httpclient = new DefaultHttpClient();
            HttpResponse response = httpclient.execute(httpRequest);
            //int code = response.getStatusLine().getStatusCode();
            //return code;
           // System.out.println("code== "+code);
            HttpEntity httpEntity = response.getEntity();
            InputStream is = httpEntity.getContent();
            InputStreamReader iSR = new InputStreamReader(is);
            BufferedReader br = new BufferedReader(iSR);
            read= br.readLine();
           // System.out.println("=---------read:"+read);

            return read;
        } catch (Exception e) {
            e.printStackTrace();

        }

        return read;
    }
}
