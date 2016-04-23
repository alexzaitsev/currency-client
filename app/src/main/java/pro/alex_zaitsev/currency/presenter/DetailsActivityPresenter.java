package pro.alex_zaitsev.currency.presenter;

import android.content.Intent;
import android.net.Uri;
import android.text.TextUtils;

import pro.alex_zaitsev.currency.App;
import pro.alex_zaitsev.currency.activity.DetailsActivity;
import pro.alex_zaitsev.currency.object.model.Advert;
import pro.alex_zaitsev.currency.presenter.core.ActivityPresenter;
import pro.alex_zaitsev.currency.utils.PlaceConverter;

/**
 * Created by rocknow on 01.03.2015.
 */
public class DetailsActivityPresenter extends ActivityPresenter<DetailsActivity> {

    public DetailsActivityPresenter(DetailsActivity activity) {
        super(activity);
    }

    public boolean isUserOwner(Advert advert) {
        return !TextUtils.isEmpty(advert.getOwnerId()) &&
                App.getParseManager().getUser() != null &&
                advert.getOwnerId().equals(App.getParseManager().getUser().getObjectId());
    }

    public void startCall(String phone) {
        String uri = "tel:" + phone;
        Intent intent = new Intent(Intent.ACTION_DIAL);
        intent.setData(Uri.parse(uri));
        activity.startActivity(intent);
    }

    public void deleteAdvert(Advert advert) {
        App.getParseManager().deleteAdvert(advert);
    }

    public void updateAdvert(Advert advert) {
        App.getParseManager().updateAdvert(advert);
    }

    public int getCityPosition() {
        int cityId = App.getSharedPrefManager().getCityId();
        return PlaceConverter.getListIndex(cityId);
    }
}
