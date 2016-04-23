package pro.alex_zaitsev.currency.presenter;

import pro.alex_zaitsev.currency.App;
import pro.alex_zaitsev.currency.activity.MyAdvertsActivity;
import pro.alex_zaitsev.currency.presenter.core.ActivityPresenter;
import pro.alex_zaitsev.currency.utils.Network;

/**
 * Created by rocknow on 10.03.2015.
 */
public class MyAdvertsActivityPresenter extends ActivityPresenter<MyAdvertsActivity> {

    public MyAdvertsActivityPresenter(MyAdvertsActivity activity) {
        super(activity);
    }

    public boolean isOnline() {
        return Network.isOnline(activity);
    }

    public void getMyAdverts() {
        App.getParseManager().getMyAdverts();
    }
}
