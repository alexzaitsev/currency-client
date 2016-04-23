package pro.alex_zaitsev.currency.utils;

import pro.alex_zaitsev.currency.R;

/**
 * Created by rocknow on 06.04.2015.
 */
public class Currency {

    public static final String USD = "usd";
    public static final String EUR = "eur";
    public static final String RUB = "rub";

    public static int getCurrencyResId(String currency) {
        switch (currency) {
            case USD:
                return R.string.usd;

            case EUR:
                return R.string.eur;

            case RUB:
                return R.string.rub;

            default:
                return -1;
        }
    }
}
