package pro.alex_zaitsev.currency.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.Menu;
import android.view.MenuItem;

import com.afollestad.materialdialogs.MaterialDialog;
import com.anjlab.android.iab.v3.BillingProcessor;
import com.anjlab.android.iab.v3.TransactionDetails;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import pro.alex_zaitsev.currency.App;
import pro.alex_zaitsev.currency.Constants;
import pro.alex_zaitsev.currency.R;
import pro.alex_zaitsev.currency.adapter.BaseFragmentPagerAdapter;
import pro.alex_zaitsev.currency.event.ForgetEvent;
import pro.alex_zaitsev.currency.event.LoginEvent;
import pro.alex_zaitsev.currency.event.SignupEvent;
import pro.alex_zaitsev.currency.fragment.AdvertFragment;
import pro.alex_zaitsev.currency.fragment.LifeTimeFragment;
import pro.alex_zaitsev.currency.fragment.LoginFragment;
import pro.alex_zaitsev.currency.interfaces.callback.AdvertFragmentCallback;
import pro.alex_zaitsev.currency.interfaces.callback.LifeTimeFragmentCallback;
import pro.alex_zaitsev.currency.interfaces.callback.LoginFragmentCallback;
import pro.alex_zaitsev.currency.object.mapper.ParseExceptionMapper;
import pro.alex_zaitsev.currency.object.model.Advert;
import pro.alex_zaitsev.currency.presenter.CreateAdvertActivityPresenter;
import pro.alex_zaitsev.currency.utils.InAppPurchase;
import pro.alex_zaitsev.currency.utils.Toaster;

/**
 * Created by rocknow on 28.02.2015.
 */
public class CreateAdvertActivity extends BaseActivity<CreateAdvertActivityPresenter> implements AdvertFragmentCallback,
        LifeTimeFragmentCallback, LoginFragmentCallback, BillingProcessor.IBillingHandler {

    private static final int POSITION_CREATE_ADVERT = 0;
    private static final int POSITION_LIFE_TIME = 1;
    private static final int POSITION_LOGIN = 2;
    private static final int TIME_CLOSE_KEYBOARD_MS = 150;

    private AdvertFragment advertFragment;
    private LifeTimeFragment lifeTimeFragment;
    private LoginFragment loginFragment;
    private BaseFragmentPagerAdapter fragmentAdapter;
    private Advert advert;

    @Bind(R.id.pager)
    ViewPager viewPager;

    @Override
    protected void setPresenter() {
        presenter = new CreateAdvertActivityPresenter(this);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_advert);
        ButterKnife.bind(this);
        initViews();
        presenter.openInAppPurchase();
    }

    private void initViews() {
        fragmentAdapter = new BaseFragmentPagerAdapter(getSupportFragmentManager(),
                getFragmentList());
        viewPager.setAdapter(fragmentAdapter);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (!presenter.onActivityResult(requestCode, resultCode, data)) {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    @Override
    protected void onDestroy() {
        presenter.closeInAppPurchase();
        super.onDestroy();
    }

    private List<Fragment> getFragmentList() {
        List<Fragment> fragmentList = new ArrayList<>();
        fragmentList.add(advertFragment = new AdvertFragment());
        fragmentList.add(lifeTimeFragment = new LifeTimeFragment());
        if (!presenter.isUserLoggedIn()) {
            fragmentList.add(loginFragment = new LoginFragment());
        }
        return fragmentList;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        int menuResId = 0;
        switch (viewPager.getCurrentItem()) {
            case POSITION_CREATE_ADVERT:
                menuResId = R.menu.forward;
                break;
            case POSITION_LIFE_TIME:
                menuResId = loginFragment == null ? R.menu.back_done : R.menu.back_forward;
                break;
            case POSITION_LOGIN:
                menuResId = R.menu.back_done;
                break;
        }
        if (menuResId > 0) {
            getMenuInflater().inflate(menuResId, menu);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_back:
                viewPager.setCurrentItem(viewPager.getCurrentItem() - 1);
                supportInvalidateOptionsMenu();
                return true;
            case R.id.action_forward:
                if (isCreateFragment() && isAdvertReady()) {
                    App.getAnalytics().onCreateDataFilled();
                    hideKeyboard();
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            viewPager.setCurrentItem(viewPager.getCurrentItem() + 1);
                            supportInvalidateOptionsMenu();
                        }
                    }, TIME_CLOSE_KEYBOARD_MS);
                } else if (isLifetimeFragment()) {
                    App.getAnalytics().onCreatePaymentFilled();
                    viewPager.setCurrentItem(viewPager.getCurrentItem() + 1);
                    supportInvalidateOptionsMenu();
                }
                return true;
            case R.id.action_done:
                save();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        App.getAnalytics().onCreateExit();
        new MaterialDialog.Builder(this)
                .title(R.string.dialog_cancel_title)
                .content(R.string.dialog_cancel_create_message)
                .positiveText(R.string.yes_exit)
                .negativeText(R.string.no_stay)
                .callback(new MaterialDialog.ButtonCallback() {

                    @Override
                    public void onPositive(MaterialDialog dialog) {
                        App.getAnalytics().onCreateExitOk();
                        CreateAdvertActivity.super.onBackPressed();
                    }

                    @Override
                    public void onNegative(MaterialDialog dialog) {
                        App.getAnalytics().onCreateExitCancel();
                    }
                })
                .show();
    }

    private boolean isCreateFragment() {
        return viewPager.getCurrentItem() == POSITION_CREATE_ADVERT &&
                advertFragment != null && advertFragment.isAdded();
    }

    private boolean isLifetimeFragment() {
        return viewPager.getCurrentItem() == POSITION_LIFE_TIME &&
                lifeTimeFragment != null && lifeTimeFragment.isAdded();
    }

    private boolean isAdvertReady() {
        advert = advertFragment.isDataValid() ? advertFragment.getData() : null;
        return advert != null;
    }

    public void hideKeyboard() {
        if (isCreateFragment()) {
            super.hideKeyboard();
        }
    }

    private void save() {
        if (advert != null && presenter.save(advert)) {
            App.getAnalytics().onCreateDoneSuccess();
            finish();
        } else {
            App.getAnalytics().onCreateDoneFail();
        }
    }

    @Override
    public boolean isEditMode() {
        return true;
    }

    @Override
    public boolean isBtnCallVisible() {
        return false;
    }

    @Override
    public void onBtnCallClick() {

    }

    @Override
    public int getCityPosition() {
        return presenter.getCityPosition();
    }

    @Override
    public int getFilterBuySell() {
        return presenter.getFilterBuySell();
    }

    @Override
    public void buy(String sku) {
        presenter.buy(sku);
    }

    @Override
    public boolean isUserLoggedIn() {
        return presenter.isUserLoggedIn();
    }

    @Override
    public boolean isShowInfo() {
        return true;
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
        return getResources().getQuantityString(R.plurals.hours_case,
                advert.getLifeTime(), advert.getLifeTime());
    }

    public void onEvent(LoginEvent event) {
        if (event.isSuccess()) {
            // logged in
            save();
        } else {
            // error
            loginFragment.finishLogin();
            Toaster.makeToastShort(this, R.string.error_incorrect_password);
        }
    }

    public void onEvent(SignupEvent event) {
        if (event.isSuccess()) {
            // signed up
            save();
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

    @Override
    public void onProductPurchased(String productId, TransactionDetails transactionDetails) {
        if (productId.equals(InAppPurchase.SKU_LIFE_TIME_1)) {
            presenter.setLifeTime(InAppPurchase.LIFE_TIME_1);
            presenter.setIsColored(true);
            presenter.setIsTop(true);
        } else if (productId.equals(InAppPurchase.SKU_LIFE_TIME_2)) {
            presenter.setLifeTime(InAppPurchase.LIFE_TIME_2);
            presenter.setIsColored(true);
            presenter.setIsTop(true);
        } else if (productId.equals(InAppPurchase.SKU_IS_TOP)) {
            presenter.setIsColored(true);
            presenter.setIsTop(true);
        } else if (productId.equals(InAppPurchase.SKU_IS_COLORED)) {
            presenter.setIsColored(true);
        }
        lifeTimeFragment.onBought(productId);
        presenter.consumePurchase(productId);
        App.getAnalytics().onBuySuccess();
    }

    @Override
    public void onPurchaseHistoryRestored() {

    }

    @Override
    public void onBillingError(int errorCode, Throwable throwable) {
        if (errorCode == Constants.BILLING_RESPONSE_RESULT_USER_CANCELED) {
            App.getAnalytics().onBuyCancelled();
        } else {
            App.getAnalytics().onBuyFail(errorCode);
            Toaster.makeToastShort(this, R.string.error);
        }
    }

    @Override
    public void onBillingInitialized() {
    }
}
