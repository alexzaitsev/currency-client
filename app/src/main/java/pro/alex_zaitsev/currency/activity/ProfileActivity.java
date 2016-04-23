package pro.alex_zaitsev.currency.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;

import pro.alex_zaitsev.currency.App;
import pro.alex_zaitsev.currency.R;
import pro.alex_zaitsev.currency.event.ForgetEvent;
import pro.alex_zaitsev.currency.event.LoginEvent;
import pro.alex_zaitsev.currency.event.MyAdvertCountEvent;
import pro.alex_zaitsev.currency.event.SignupEvent;
import pro.alex_zaitsev.currency.fragment.LoginFragment;
import pro.alex_zaitsev.currency.fragment.LogoutFragment;
import pro.alex_zaitsev.currency.interfaces.callback.LoginFragmentCallback;
import pro.alex_zaitsev.currency.interfaces.callback.LogoutFragmentCallback;
import pro.alex_zaitsev.currency.object.mapper.ParseExceptionMapper;
import pro.alex_zaitsev.currency.presenter.ProfileActivityPresenter;
import pro.alex_zaitsev.currency.utils.Toaster;

/**
 * Created by rocknow on 12.03.2015.
 */
public class ProfileActivity extends BaseUpActivity<ProfileActivityPresenter>
        implements LoginFragmentCallback, LogoutFragmentCallback {

    private static final int REQUEST_MY_ADVERTS = 353;

    private LoginFragment loginFragment;
    private LogoutFragment logoutFragment;

    @Override
    protected void setPresenter() {
        presenter = new ProfileActivityPresenter(this);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        setFragment(TextUtils.isEmpty(getUserEmail()) ?
                (loginFragment = new LoginFragment()) :
                (logoutFragment = new LogoutFragment()));
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_MY_ADVERTS && resultCode == RESULT_FIRST_USER) {
            getMyAdvertsCount();
            setResult(RESULT_FIRST_USER);
        }
    }

    private void setFragment(Fragment fragment) {
        super.setFragment(R.id.content, fragment);
    }

    @Override
    public void getMyAdvertsCount() {
        presenter.getMyAdvertsCount();
    }

    @Override
    public String getUserEmail() {
        return presenter.getUserEmail();
    }

    @Override
    public void signOut() {
        App.getAnalytics().onLogout();
        if (presenter.signOut()) {
            setFragment(loginFragment = new LoginFragment());
        }
    }

    @Override
    public void onMyAdvertsClick() {
        startActivityForResult(new Intent(this, MyAdvertsActivity.class), REQUEST_MY_ADVERTS);
    }

    @Override
    public boolean isShowInfo() {
        return false;
    }

    @Override
    public void onLoginClick(String email, String password) {
        presenter.login(email, password);
    }

    @Override
    public void onSignupClick(String email, String password) {
        presenter.signup(email, password);
    }

    @Override
    public void onForgotClick(String email) {
        presenter.restorePassword(email);
    }

    @Override
    public String getShowData() {
        return "";
    }

    public void onEvent(LoginEvent event) {
        if (event.isSuccess()) {
            // logged in
            hideKeyboard();
            setFragment(logoutFragment = new LogoutFragment());
        } else {
            // error
            loginFragment.finishLogin();
            Toaster.makeToastShort(this, R.string.error_incorrect_password);
        }
    }

    public void onEvent(SignupEvent event) {
        if (event.isSuccess()) {
            // signed up
            hideKeyboard();
            setFragment(logoutFragment = new LogoutFragment());
        } else {
            // error
            loginFragment.finishLogin();
            Toaster.makeToastShort(this,
                    ParseExceptionMapper.getMessage(this, event.getParseException()));
        }
    }

    public void onEvent(ForgetEvent event) {
        loginFragment.finishLogin();
        if (event.isSuccess()) {
            // email sent
            Toaster.makeToastShort(this, R.string.forgot_success);
        } else {
            // error
            Toaster.makeToastShort(this,
                    ParseExceptionMapper.getMessage(this, event.getParseException()));
        }
    }

    public void onEvent(MyAdvertCountEvent event) {
        if (event.isSuccess()) {
            if (logoutFragment != null) {
                logoutFragment.setMyAdvertsCount(event.getCount());
            }
        } else {
            Toaster.makeToastShort(this, R.string.network_warn);
        }
    }
}
