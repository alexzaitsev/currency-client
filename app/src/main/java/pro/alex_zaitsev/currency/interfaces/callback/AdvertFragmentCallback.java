package pro.alex_zaitsev.currency.interfaces.callback;

/**
 * Created by rocknow on 01.03.2015.
 */
public interface AdvertFragmentCallback extends Callback {

    boolean isEditMode();

    boolean isBtnCallVisible();

    void onBtnCallClick();

    int getCityPosition();

    int getFilterBuySell();
}
