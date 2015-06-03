package com.evoblock.mikhail.currencytoday;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends ActionBarActivity implements ActionBar.OnNavigationListener{
   // LoadCurrency nn;
    SharedPreferences   sPref;
    final static String LOCAL_CURRENCY = "local_currency";
    final static String DATA_USD       = "data_usd";
    final static String DATA_EUR       = "data_eur";
    final static String DATA_USD_NAME  = "data_usd_name";
    final static String DATA_EUR_NAME  = "data_eur_name";
    String[] nameDataCur = new String[]{"Australian Dollar","British Pound","Canadian Dollar",
                                        "Czech Koruna","Danish Krone","French Franc","German Mark",
                                        "Hong Kong Dollar","Hungarian Forint","Irish Punt","Italian Lira",
                                        "Japanese Yen","Latvian Lat","Lithuanian Lita","Mexican Peso",
                                        "New Zealand Dollar","Norway Krone","Polish Zloty","Russian Rouble",
                                        "Singapore Dollar","South African Rand","Swedish Krona","Swiss Franc","Ukraine Hryvnia"};
    String[] data = new String[]{"AUD","GBP","CAD","CZK","DKK","FRF","DEM","HKD","HUF","IEP","ITL","JPY",
                                 "LVL","LTL","MXN","NZD","NOK","PLN","RUB","SGD","ZAR","SEK","CHF","UAH"};
    int localCur = 0;
    //Animation anim;
    TextView nameUSD,nameEUR,rateUSD,rateEUR;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Fragment fragmentF = new FragmentFirst();
        Fragment fragmentS = new FragmentSecond();

        loadLastData();

        ActionBar bar = getSupportActionBar();
        bar.setNavigationMode(ActionBar.NAVIGATION_MODE_LIST);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item, nameDataCur);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        bar.setListNavigationCallbacks(adapter,this);
        bar.setSelectedNavigationItem(localCur);
        bar.setTitle("");


    }

    public void loadLastData(){
        sPref = getPreferences(MODE_PRIVATE);

        nameUSD = (TextView)findViewById(R.id.currencyFirst);
        nameEUR = (TextView)findViewById(R.id.currencySecond);
        rateUSD = (TextView)findViewById(R.id.Cur0);
        rateEUR = (TextView)findViewById(R.id.Cur1);
        if(sPref.getString(DATA_USD,"")==null &&
           sPref.getString(DATA_EUR,"")==null &&
           sPref.getString(DATA_USD_NAME,"")==null &&
           sPref.getString(DATA_EUR_NAME,"")==null &&
           sPref.getInt(LOCAL_CURRENCY,0)==0){
            nameUSD.setText("USD");
            nameEUR.setText("EUR");
            rateUSD.setText("0.0");
            rateEUR.setText("0.0");
            localCur = 0;
           // System.out.println("=========11========="+localCur);


        }else{
            nameUSD.setText(sPref.getString(DATA_USD_NAME,""));
            nameEUR.setText(sPref.getString(DATA_EUR_NAME,""));
            rateUSD.setText(sPref.getString(DATA_USD,""));
            rateEUR.setText(sPref.getString(DATA_EUR,""));
            localCur = sPref.getInt(LOCAL_CURRENCY,0);
            //System.out.println("=================="+localCur);
        }

    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.item_update){

            Animation animFrom = AnimationUtils.loadAnimation(this, R.anim.transform_from);
            rateUSD.startAnimation(animFrom);
            rateEUR.startAnimation(animFrom);

            if(checkInternetConnection()) {
                LoadCurrency nn = new LoadCurrency(MainActivity.this);
                nn.execute("usd" + data[localCur] + ",eur" + data[localCur]);
            }
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onNavigationItemSelected(int i, long l) {
      //  nn.execute("usdrub,eurrub");
        System.out.println("========" + data[i]);

        Animation animFrom = AnimationUtils.loadAnimation(this, R.anim.transform_from);
        rateUSD.startAnimation(animFrom);
        rateEUR.startAnimation(animFrom);
        if(checkInternetConnection()) {
            LoadCurrency nn = new LoadCurrency(MainActivity.this);
            nn.execute("usd" + data[i] + ",eur" + data[i]);
            localCur = i;
        }
        //save
        sPref = getPreferences(MODE_PRIVATE);
        SharedPreferences.Editor ed = sPref.edit();
        ed.putInt(LOCAL_CURRENCY,localCur);
        ed.commit();

        return false;
    }
    public boolean checkInternetConnection(){
        ConnectivityManager check = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo[] info = check.getAllNetworkInfo();
        for(int i = 0;i<info.length;i++)
            if(info[i].getState() == NetworkInfo.State.CONNECTED){
                return true;
            }
        Toast.makeText(this,"Internet isn't connected",Toast.LENGTH_SHORT).show();
        return false;
    }
}
