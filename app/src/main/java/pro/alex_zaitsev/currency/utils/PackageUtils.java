package pro.alex_zaitsev.currency.utils;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;

import timber.log.Timber;

/**
 * Created by rocknow on 25.03.2015.
 */
public class PackageUtils {

    public static int getVersionCode(Context context) {
        PackageInfo pInfo = null;
        try {
            pInfo = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
        } catch (PackageManager.NameNotFoundException e) {
            Timber.e(e, e.getMessage());
        }
        return pInfo == null ? Integer.MAX_VALUE : pInfo.versionCode;
    }
}
