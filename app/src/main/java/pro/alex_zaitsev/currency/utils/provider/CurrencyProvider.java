package pro.alex_zaitsev.currency.utils.provider;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import pro.alex_zaitsev.currency.Constants;

/**
 * Created by rocknow on 01.03.2015.
 */
public class CurrencyProvider {

    public List<String> getCurrencyFrom() {
        List<String> currencyList = new ArrayList<>();
        currencyList.add(Constants.USD);
        currencyList.add(Constants.EUR);
        currencyList.add(Constants.RUB);
        return currencyList;
    }

    public int getPositionFrom(String currency) {
        return getCurrencyFrom().indexOf(currency.toUpperCase(Locale.US));
    }
}
