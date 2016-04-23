package pro.alex_zaitsev.currency.interfaces.callback;

/**
 * Created by rocknow on 05.03.2015.
 */
public interface LifeTimeFragmentCallback extends Callback {

    void buy(String sku);

    boolean isUserLoggedIn();
}
