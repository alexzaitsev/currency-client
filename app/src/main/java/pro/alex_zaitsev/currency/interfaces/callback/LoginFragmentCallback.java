package pro.alex_zaitsev.currency.interfaces.callback;

/**
 * Created by rocknow on 05.03.2015.
 */
public interface LoginFragmentCallback extends Callback {

    boolean isShowInfo();

    void onLoginClick(String email, String password);

    void onSignupClick(String email, String password);

    void onForgotClick(String email);

    String getShowData();
}
