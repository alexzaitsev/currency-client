package pro.alex_zaitsev.currency.utils;

import android.app.Activity;
import android.content.Intent;

import com.anjlab.android.iab.v3.BillingProcessor;

import java.io.Closeable;

import pro.alex_zaitsev.currency.App;
import pro.alex_zaitsev.currency.object.model.Config;

/**
 * Created by rocknow on 18.03.2015.
 */
public class InAppPurchase implements Closeable {

    public static final int LIFE_TIME_DEFAULT = 24;
    public static final int LIFE_TIME_1 = 2 * 24;
    public static final int LIFE_TIME_2 = 3 * 24;

    public static final String SKU_IS_COLORED = "advert_is_colored";
    public static final String SKU_IS_TOP = "advert_is_top";
    public static final String SKU_LIFE_TIME_1 = "advert_lifetime_1";
    public static final String SKU_LIFE_TIME_2 = "advert_lifetime_2";

    private Activity activity;
    private BillingProcessor bp;

    public InAppPurchase(Activity activity) {
        BillingProcessor.IBillingHandler billingHandler;
        try {
            billingHandler = (BillingProcessor.IBillingHandler) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.getClass().getSimpleName() +
                " must implement " + BillingProcessor.IBillingHandler.class.getSimpleName());
        }
        Config config = App.getConfig();
        if (config == null) {
            return;
        }
        this.activity = activity;
        bp = new BillingProcessor(activity, config.getLicenceKey(), billingHandler);
    }

    public boolean onActivityResult(int requestCode, int resultCode, Intent data) {
        return bp != null && bp.handleActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void close() {
        if (bp != null) {
            bp.release();
        }
    }

    public void onBuy(String productId) {
        if (bp != null) {
            bp.purchase(activity, productId);
        }
    }

    public void consumePurchase(String productId) {
        if (bp != null) {
            bp.consumePurchase(productId);
        }
    }
}