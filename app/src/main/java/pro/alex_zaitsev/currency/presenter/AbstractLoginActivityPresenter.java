package pro.alex_zaitsev.currency.presenter;

import pro.alex_zaitsev.currency.App;
import pro.alex_zaitsev.currency.R;
import pro.alex_zaitsev.currency.activity.BaseActivity;
import pro.alex_zaitsev.currency.presenter.core.ActivityPresenter;
import pro.alex_zaitsev.currency.utils.Network;
import pro.alex_zaitsev.currency.utils.Toaster;

/**
 * Created by rocknow on 01.03.2015.
 */
public abstract class AbstractLoginActivityPresenter<A extends BaseActivity> extends ActivityPresenter<A> {

    public AbstractLoginActivityPresenter(A activity) {
        super(activity);
    }

    public void signup(String email, String password) {
        if (Network.isOnline(activity)) {
            App.getParseManager().signUp(email, password);
        } else {
            Toaster.makeToastShort(activity, R.string.network_warn);
        }
    }

    public void login(String email, String password) {
        if (Network.isOnline(activity)) {
            App.getParseManager().login(email, password);
        } else {
            Toaster.makeToastShort(activity, R.string.network_warn);
        }
    }

    public void restorePassword(String email) {
        if (Network.isOnline(activity)) {
            App.getParseManager().restorePassword(email);
        } else {
            Toaster.makeToastShort(activity, R.string.network_warn);
        }
    }
}
