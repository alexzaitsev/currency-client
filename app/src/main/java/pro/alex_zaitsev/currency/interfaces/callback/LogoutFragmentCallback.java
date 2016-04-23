package pro.alex_zaitsev.currency.interfaces.callback;

/**
 * Created by rocknow on 12.03.2015.
 */
public interface LogoutFragmentCallback extends Callback {

    String getUserEmail();

    void signOut();

    void onMyAdvertsClick();

    void getMyAdvertsCount();
}
