package pro.alex_zaitsev.currency.utils;

import android.os.Build;

import pro.alex_zaitsev.currency.Constants;

/**
 * Created by rocknow on 16.03.2015.
 */
public class ListLimiter {

    public static int getLimit() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.HONEYCOMB) {
            return Constants.LIST_LIMIT_ITEMS_PRE_HB;
        } else if (Build.VERSION.SDK_INT < Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
            return Constants.LIST_LIMIT_ITEMS_PRE_ICS;
        }
        return Constants.LIST_LIMIT_ITEMS;
    }
}
