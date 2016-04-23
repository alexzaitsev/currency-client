package pro.alex_zaitsev.currency.interfaces.callback;

import pro.alex_zaitsev.currency.object.model.Advert;

/**
 * Created by rocknow on 13.03.2015.
 */
public interface BaseAdvertListFragmentCallback extends BaseListFragmentCallback {

    void getAdverts();

    void onAdvertClick(Advert advert);
}
