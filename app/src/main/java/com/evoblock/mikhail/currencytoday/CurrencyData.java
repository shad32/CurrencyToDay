package com.evoblock.mikhail.currencytoday;

/**
 * Created by Mihail on 30.03.2015.
 */
public class CurrencyData {
    private String nameCurrency ;
    private String rateCurrency ;

    public CurrencyData(String newNameCurrency,String newRateCurrency){
        nameCurrency  = newNameCurrency;
        rateCurrency = newRateCurrency;
    }

    public String getNameCurrency() {
        return nameCurrency;
    }

    public String getRateCurrency() {
        return rateCurrency;
    }
}
