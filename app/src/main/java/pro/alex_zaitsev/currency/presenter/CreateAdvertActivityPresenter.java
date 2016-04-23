package pro.alex_zaitsev.currency.presenter;

import android.content.Intent;

import pro.alex_zaitsev.currency.App;
import pro.alex_zaitsev.currency.R;
import pro.alex_zaitsev.currency.activity.CreateAdvertActivity;
import pro.alex_zaitsev.currency.object.model.Advert;
import pro.alex_zaitsev.currency.utils.InAppPurchase;
import pro.alex_zaitsev.currency.utils.Network;
import pro.alex_zaitsev.currency.utils.PlaceConverter;
import pro.alex_zaitsev.currency.utils.Toaster;

/**
 * Created by rocknow on 01.03.2015.
 */
public class CreateAdvertActivityPresenter extends AbstractLoginActivityPresenter<CreateAdvertActivity> {

    public static final int MIN_PASSWORD_LENGTH = 4;

    private InAppPurchase inAppPurchase;
    private boolean isColored;
    private boolean isTop;
    private int lifeTime = InAppPurchase.LIFE_TIME_DEFAULT;

    public CreateAdvertActivityPresenter(CreateAdvertActivity activity) {
        super(activity);
    }

    public void openInAppPurchase() {
        inAppPurchase = new InAppPurchase(activity);
    }

    public boolean onActivityResult(int requestCode, int resultCode, Intent data) {
        return inAppPurchase.onActivityResult(requestCode, resultCode, data);
    }

    public void closeInAppPurchase() {
        inAppPurchase.close();
    }

    public void consumePurchase(String productId) {
        inAppPurchase.consumePurchase(productId);
    }

    public boolean save(Advert advert) {
        if (Network.isOnline(activity)) {
            advert.setOwnerId(App.getParseManager().getUser() == null ? "" :
                    App.getParseManager().getUser().getObjectId());
            advert.setIsColored(isColored);
            advert.setIsTop(isTop);
            advert.setLifeTime(lifeTime);
            App.getParseManager().saveAdvert(advert);
            return true;
        } else {
            Toaster.makeToastShort(activity, R.string.network_warn);
            return false;
        }
    }

    public void buy(String sku) {
        inAppPurchase.onBuy(sku);
    }

    public boolean isUserLoggedIn() {
        return App.getParseManager().isUserLoggedIn();
    }

    public int getCityPosition() {
        int cityId = App.getSharedPrefManager().getCityId();
        return PlaceConverter.getListIndex(cityId);
    }

    public void setIsColored(boolean isColored) {
        this.isColored = isColored;
    }

    public void setIsTop(boolean isTop) {
        this.isTop = isTop;
    }

    public void setLifeTime(int lifeTime) {
        this.lifeTime = lifeTime;
    }

    public int getFilterBuySell() {
        return App.getSharedPrefManager().getFilterBuySell();
    }
}
