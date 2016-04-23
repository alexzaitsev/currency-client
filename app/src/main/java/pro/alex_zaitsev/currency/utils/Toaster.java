package pro.alex_zaitsev.currency.utils;

import android.content.Context;
import android.widget.Toast;

/**
 * Created by rocknow on 10.03.2015.
 */
public class Toaster {

    public static void makeToastShort(Context context, String string) {
        Toast.makeText(context, string, Toast.LENGTH_SHORT).show();
    }

    public static void makeToastShort(Context context, int resId) {
        Toast.makeText(context, resId, Toast.LENGTH_SHORT).show();
    }

    public static void makeToastLong(Context context, String string) {
        Toast.makeText(context, string, Toast.LENGTH_LONG).show();
    }

    public static void makeToastLong(Context context, int resId) {
        Toast.makeText(context, resId, Toast.LENGTH_LONG).show();
    }
}
