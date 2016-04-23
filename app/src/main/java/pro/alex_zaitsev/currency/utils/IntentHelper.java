package pro.alex_zaitsev.currency.utils;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;

import pro.alex_zaitsev.currency.R;

/**
 * Created by rocknow on 25.03.2015.
 */
public class IntentHelper {

    public static final int REQUEST_MARKET = 392;

    public static void openMarket(Activity activity) {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(Uri.parse(activity.getString(R.string.market, activity.getPackageName())));
        try {
            activity.startActivityForResult(intent, REQUEST_MARKET);
        } catch (ActivityNotFoundException e) {
            Toaster.makeToastShort(activity, R.string.toast_warn_market);
        }
    }
}
