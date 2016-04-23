package pro.alex_zaitsev.currency.activity;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import de.greenrobot.event.EventBus;
import pro.alex_zaitsev.currency.event.ApiEvent;
import pro.alex_zaitsev.currency.presenter.core.ActivityPresenter;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

/**
 * Created by rocknow on 28.02.2015.
 */
public abstract class BaseActivity<P extends ActivityPresenter> extends ActionBarActivity {

    protected P presenter;

    protected abstract void setPresenter();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setPresenter();
    }

    @Override
    public void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    public void onStop() {
        EventBus.getDefault().unregister(this);
        super.onStop();
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    public void onEvent(ApiEvent event) {}

    protected void setFragment(int contentViewId, Fragment fragment) {
        getSupportFragmentManager().beginTransaction().replace(contentViewId, fragment,
                fragment.getClass().getSimpleName()).
                setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN).commit();
    }

    public void hideKeyboard() {
        InputMethodManager imm = (InputMethodManager) getSystemService(
                Context.INPUT_METHOD_SERVICE);
        View focusedView = getCurrentFocus();
        if (focusedView != null) {
            imm.hideSoftInputFromWindow(focusedView.getWindowToken(), 0);
        }
    }
}
