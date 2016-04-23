package pro.alex_zaitsev.currency.presenter;

import pro.alex_zaitsev.currency.App;
import pro.alex_zaitsev.currency.R;
import pro.alex_zaitsev.currency.activity.ProfileActivity;
import pro.alex_zaitsev.currency.utils.Network;
import pro.alex_zaitsev.currency.utils.Toaster;

/**
 * Created by rocknow on 12.03.2015.
 */
public class ProfileActivityPresenter extends AbstractLoginActivityPresenter<ProfileActivity> {

    public ProfileActivityPresenter(ProfileActivity activity) {
        super(activity);
    }

    public String getUserEmail() {
        return App.getParseManager().getUser() == null ? null : App.getParseManager().getUser().getEmail();
    }

    public boolean signOut() {
        if (Network.isOnline(activity)) {
            App.getParseManager().signOut();
            return true;
        } else {
            Toaster.makeToastShort(activity, R.string.network_warn);
            return false;
        }
    }

    public void getMyAdvertsCount() {
        if (Network.isOnline(activity)) {
            App.getParseManager().getMyAdvertsCount();
        } else {
            Toaster.makeToastShort(activity, R.string.network_warn);
        }
    }
}
